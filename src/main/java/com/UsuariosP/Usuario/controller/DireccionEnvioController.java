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

import com.UsuariosP.Usuario.model.DireccionEnvio;
import com.UsuariosP.Usuario.service.DireccionEnvioService;

@RestController
@RequestMapping("/api/v1/direccionEnvio")
@CrossOrigin(origins = "*")
public class DireccionEnvioController {

    @Autowired
    private DireccionEnvioService direccionEnvioService;

    @PostMapping("/crear")
    public DireccionEnvio crearDireccionEnvio(@RequestBody DireccionEnvio direccionEnvio){
        return direccionEnvioService.crear(direccionEnvio);
    }

    @GetMapping("/listar")
    public List<DireccionEnvio> listarDireccionEnvio(){
        return direccionEnvioService.listar();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<DireccionEnvio> listarPorUsuario(@PathVariable Long idUsuario){
        return direccionEnvioService.listarPorUsuario(idUsuario);
    }

    @PutMapping("/modificar/{id}")
    public DireccionEnvio modificarDireccionEnvio(@PathVariable Long id, @RequestBody DireccionEnvio direccionEnvio){
        return direccionEnvioService.modificarDireccionEnvio(id, direccionEnvio);
    }

    @PutMapping("/principal/{id}")
    public DireccionEnvio marcarComoPrincipal(@PathVariable Long id){
        return direccionEnvioService.marcarComoPrincipal(id);
    }

    @PutMapping("/desactivar/{id}")
    public DireccionEnvio desactivar(@PathVariable Long id){
        return direccionEnvioService.desactivar(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id){
        direccionEnvioService.eliminar(id);
    }

}
