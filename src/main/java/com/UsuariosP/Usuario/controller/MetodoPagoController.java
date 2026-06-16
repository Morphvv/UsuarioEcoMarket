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

import com.UsuariosP.Usuario.model.MetodoPago;
import com.UsuariosP.Usuario.service.MetodoPagoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/metodoPago")
@CrossOrigin(origins = "*")
@Tag(name = "Metodos de pago", description = "Gestion de metodos de pago")

public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @PostMapping("/crear")
    public MetodoPago crearMetodoPago(@Valid @RequestBody MetodoPago metodoPago){
        return metodoPagoService.crear(metodoPago);
    }

    @GetMapping("/listar")
    public List <MetodoPago> listarMetodoPago(){
        return metodoPagoService.listar();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List <MetodoPago> listarPorUsuario(@PathVariable Long idUsuario){
        return metodoPagoService.listarPorUsuario(idUsuario);
    }

    @PutMapping("/modificar/{id}")
    public MetodoPago modificarMetodoPago(@Valid @PathVariable Long id, @RequestBody MetodoPago metodoPago){
        return metodoPagoService.modificarMetodoPago(id, metodoPago);
    }
    
    @PutMapping("/principal/{id}")
    public MetodoPago marcarPrincipal(@PathVariable Long id){
        return metodoPagoService.marcarComoPrincipal(id);
    }

    @PutMapping("/desactivar/{id}")
    public MetodoPago desactivar(@PathVariable Long id){
        return metodoPagoService.desactivar(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id){
        metodoPagoService.eliminar(id);
    }
}
