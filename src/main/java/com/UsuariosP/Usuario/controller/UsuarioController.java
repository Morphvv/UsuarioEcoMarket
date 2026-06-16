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

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/usuarios")
@CrossOrigin(origins = "*")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    public Usuario crearUsuario(@Valid @RequestBody Usuario usuario){
        return usuarioService.crear(usuario);
    }

    @GetMapping("listar")
    public List<Usuario> listarUsuarios(){
        return usuarioService.listar();
    }

    @GetMapping("/buscar/{id}")
    public Usuario buscarUsuario(@PathVariable Long id){
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/modificar/{id}")
    public Usuario modificarUsuario(@Valid @PathVariable Long id, @RequestBody Usuario usuario){
        return usuarioService.modificarUsuario(id, usuario);
    }

    @PutMapping("/desactivar/{id}")
    public Usuario desactivarUsuario(@PathVariable Long id){
        return usuarioService.desactivar(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminar(id);
    }
    

}
