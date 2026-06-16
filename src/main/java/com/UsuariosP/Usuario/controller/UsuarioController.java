package com.UsuariosP.Usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Usuario crearUsuario(@Valid @RequestBody Usuario usuario){
        return usuarioService.crear(usuario);
    }

    @GetMapping("listar")
    @Operation(summary = "Listar todos los usuarios")
    public List<Usuario> listarUsuarios(){
        return usuarioService.listar();
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Buscar usuario por rut")
    public Usuario buscarUsuario(@PathVariable Long id){
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/modificar/{id}")
    @Operation(summary = "Modificar usuario por rut")
    public Usuario modificarUsuario(@Valid @PathVariable Long id, @RequestBody Usuario usuario){
        return usuarioService.modificarUsuario(id, usuario);
    }

    @PutMapping("/desactivar/{id}")
    @Operation(summary = "Desactivar usuario por rut")
    public Usuario desactivarUsuario(@PathVariable Long id){
        return usuarioService.desactivar(id);
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar usuario por rut")
    public void eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminar(id);
    }
    

}
