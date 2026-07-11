package com.UsuariosP.Usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.UsuariosP.Usuario.model.Sesion;
import com.UsuariosP.Usuario.service.SesionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/sesion")
@CrossOrigin(origins = "*")
@Tag(name = "Sesiones", description = "Gestion de sesiones")

public class SesionController {

    @Autowired
    private SesionService sesionService;

    @PostMapping("/iniciar")
    @Operation(summary = "Iniciar una sesion")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "Sesion iniciada"), @ApiResponse(responseCode = "400", description = "Datos invalidos") })
    public ResponseEntity<Sesion> iniciarSesion(@Valid @RequestBody Sesion sesion){
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionService.iniciarSesion(sesion));
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas las sesiones")
    @ApiResponse(responseCode = "200", description = "Lista de sesiones")
    public ResponseEntity<List<Sesion>> listarSesiones(){
        return ResponseEntity.ok(sesionService.listarS());
    }

    @PutMapping("/modificar/{id}")
    @Operation(summary = "Modificar sesion")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Sesion modificada"), @ApiResponse(responseCode = "404", description = "Sesion no encontrada") })
    public ResponseEntity<Sesion> modificarSesion(@PathVariable Long id, @Valid @RequestBody Sesion sesion){
        return ResponseEntity.ok(sesionService.modificarSesion(id, sesion));
    }

    @GetMapping("/validar/{id}")
    @Operation(summary = "Validar sesion activa")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Sesion valida"), @ApiResponse(responseCode = "401", description = "Sesion invalida o expirada") })
    public ResponseEntity<Sesion> validarSesion(@PathVariable Long id){
        Sesion sesion = sesionService.validarSesion(id);
        if (sesion == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(sesion);
    }

    @PutMapping("/expirar/{id}")
    @Operation(summary = "Cerrar sesion")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Sesion cerrada"), @ApiResponse(responseCode = "404", description = "Sesion no encontrada") })
    public ResponseEntity<Sesion> expirarSesion(@PathVariable Long id){
        Sesion resultado = sesionService.cerrarSesion(id);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar sesion")
    @ApiResponse(responseCode = "204", description = "Sesion eliminada")
    public ResponseEntity<Void> eliminarSesion(@PathVariable Long id){
        sesionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
