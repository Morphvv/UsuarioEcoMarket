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

import com.UsuariosP.Usuario.model.CuentaUsuario;
import com.UsuariosP.Usuario.service.CuentaUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/cuentaUsuario")
@CrossOrigin(origins = "*")
@Tag(name = "Cuenta de usuario", description = "Gestion de cuentas")

public class CuentaUsuarioController {

    @Autowired
    private CuentaUsuarioService cuentaUsuarioService;

    @PostMapping("/crear")
    @Operation(summary = "Crear una cuenta de usuario")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "Cuenta creada"), @ApiResponse(responseCode = "400", description = "Datos invalidos") })
    public ResponseEntity<CuentaUsuario> crearCuentaUsuario(@Valid @RequestBody CuentaUsuario cuentaUsuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaUsuarioService.crear(cuentaUsuario));
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas las cuentas")
    @ApiResponse(responseCode = "200", description = "Lista de cuentas")
    public ResponseEntity<List<CuentaUsuario>> listarCuentaUsuario(){
        return ResponseEntity.ok(cuentaUsuarioService.listar());
    }

    @PutMapping("/modificar/{id}")
    @Operation(summary = "Modificar cuenta de usuario")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Cuenta modificada"), @ApiResponse(responseCode = "404", description = "Cuenta no encontrada") })
    public ResponseEntity<CuentaUsuario> modificarCuentaUsuario(@PathVariable Long id, @Valid @RequestBody CuentaUsuario cuentaUsuario){
        CuentaUsuario resultado = cuentaUsuarioService.modificarCuentaU(id, cuentaUsuario);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/autenticar")
    @Operation(summary = "Autenticar cuenta de usuario")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Autenticacion exitosa"), @ApiResponse(responseCode = "401", description = "Credenciales invalidas") })
    public ResponseEntity<CuentaUsuario> autenticarCuentaUsuario(@RequestBody CuentaUsuario cuentaUsuario){
        CuentaUsuario resultado = cuentaUsuarioService.autenticar(cuentaUsuario);
        if (resultado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/desactivar/{id}")
    @Operation(summary = "Desactivar cuenta de usuario")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Cuenta desactivada"), @ApiResponse(responseCode = "404", description = "Cuenta no encontrada") })
    public ResponseEntity<CuentaUsuario> desactivarCuentaUsuario(@PathVariable Long id){
        CuentaUsuario resultado = cuentaUsuarioService.desactivar(id);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar cuenta de usuario")
    @ApiResponse(responseCode = "204", description = "Cuenta eliminada")
    public ResponseEntity<Void> eliminarCuentaUsuario(@PathVariable Long id){
        cuentaUsuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
