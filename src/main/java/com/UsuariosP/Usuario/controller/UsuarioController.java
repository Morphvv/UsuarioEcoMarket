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

import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuario", description = "Gestion de usuarios")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    @Operation(summary = "Crear un usuario")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "Usuario creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos") })
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(usuario));
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Buscar usuario por rut")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Usuario encontrado"), @ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PutMapping("/modificar/{id}")
    @Operation(summary = "Modificar usuario por rut")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Usuario modificado"), @ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
    public ResponseEntity<Usuario> modificarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario){
        return ResponseEntity.ok(usuarioService.modificarUsuario(id, usuario));
    }

    @PutMapping("/desactivar/{id}")
    @Operation(summary = "Desactivar usuario por rut")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Usuario desactivado"), @ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.desactivar(id));
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar usuario por rut")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
