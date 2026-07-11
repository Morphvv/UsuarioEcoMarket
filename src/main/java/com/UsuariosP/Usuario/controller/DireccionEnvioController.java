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

import com.UsuariosP.Usuario.model.DireccionEnvio;
import com.UsuariosP.Usuario.service.DireccionEnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/direccionEnvio")
@CrossOrigin(origins = "*")
@Tag(name = "Direcciones de envio", description = "Gestion de direcciones de envio")
public class DireccionEnvioController {

    @Autowired
    private DireccionEnvioService direccionEnvioService;

    @PostMapping("/crear")
    @Operation(summary = "Crear una direccion de envio")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "Direccion creada"), @ApiResponse(responseCode = "400", description = "Datos invalidos") })
    public ResponseEntity<DireccionEnvio> crearDireccionEnvio(@Valid @RequestBody DireccionEnvio direccionEnvio){
        return ResponseEntity.status(HttpStatus.CREATED).body(direccionEnvioService.crear(direccionEnvio));
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas las direcciones")
    @ApiResponse(responseCode = "200", description = "Lista de direcciones")
    public ResponseEntity<List<DireccionEnvio>> listarDireccionEnvio(){
        return ResponseEntity.ok(direccionEnvioService.listar());
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar direcciones por usuario")
    @ApiResponse(responseCode = "200", description = "Lista de direcciones del usuario")
    public ResponseEntity<List<DireccionEnvio>> listarPorUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(direccionEnvioService.listarPorUsuario(idUsuario));
    }

    @PutMapping("/modificar/{id}")
    @Operation(summary = "Modificar direccion de envio")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Direccion modificada"), @ApiResponse(responseCode = "404", description = "Direccion no encontrada") })
    public ResponseEntity<DireccionEnvio> modificarDireccionEnvio(@PathVariable Long id, @Valid @RequestBody DireccionEnvio direccionEnvio){
        return ResponseEntity.ok(direccionEnvioService.modificarDireccionEnvio(id, direccionEnvio));
    }

    @PutMapping("/principal/{id}")
    @Operation(summary = "Marcar direccion como principal")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Direccion marcada como principal"), @ApiResponse(responseCode = "404", description = "Direccion no encontrada") })
    public ResponseEntity<DireccionEnvio> marcarComoPrincipal(@PathVariable Long id){
        DireccionEnvio resultado = direccionEnvioService.marcarComoPrincipal(id);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/desactivar/{id}")
    @Operation(summary = "Desactivar direccion de envio")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Direccion desactivada"), @ApiResponse(responseCode = "404", description = "Direccion no encontrada") })
    public ResponseEntity<DireccionEnvio> desactivar(@PathVariable Long id){
        DireccionEnvio resultado = direccionEnvioService.desactivar(id);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar direccion de envio")
    @ApiResponse(responseCode = "204", description = "Direccion eliminada")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        direccionEnvioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
