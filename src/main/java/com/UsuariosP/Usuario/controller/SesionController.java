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
    public Sesion iniciarSesion(@Valid @RequestBody Sesion sesion){
        return sesionService.iniciarSesion(sesion);
    }

    @GetMapping("/listar")
    public List <Sesion> listarSesiones(){
        return sesionService.listarS();
    }

    @PutMapping("/modificar/{id}")
    public Sesion modificarSesion(@Valid @PathVariable Long id, @RequestBody Sesion sesion){
        return sesionService.modificarSesion(id, sesion);
    }

    @GetMapping("/validar/{id}")
    public ResponseEntity<Sesion> validarSesion(@PathVariable Long id){
        Sesion sesion = sesionService.validarSesion(id);
        if(sesion == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(sesion);
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
