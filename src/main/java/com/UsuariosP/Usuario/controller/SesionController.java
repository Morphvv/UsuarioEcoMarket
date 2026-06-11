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

import com.UsuariosP.Usuario.model.Sesion;
import com.UsuariosP.Usuario.service.SesionService;

@RestController
@RequestMapping("api/v1/sesion")
@CrossOrigin(origins = "*")

public class SesionController {

    @Autowired
    private SesionService sesionService;

    @PostMapping("/iniciar")
    public Sesion iniciarSesion(@RequestBody Sesion sesion){
        return sesionService.iniciarSesion(sesion);
    }

    @GetMapping("/listar")
    public List <Sesion> listarSesiones(){
        return sesionService.listarS();
    }

    @PutMapping("/modificar/{id}")
    public Sesion modificarSesion(@PathVariable Long id, @RequestBody Sesion sesion){
        return sesionService.modificarSesion(id, sesion);
    }

    @GetMapping("/validar/{id}")
    public Sesion validarSesion(@PathVariable Long id){
        return sesionService.validarSesion(id);
    }

    @PutMapping("/expirar/{id}")
    public Sesion expirarSesion(@PathVariable Long id){
        return sesionService.cerrarSesion(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarSesion(@PathVariable Long id){
        sesionService.eliminar(id);
    }
    
}
