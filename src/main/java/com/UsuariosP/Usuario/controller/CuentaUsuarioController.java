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

import com.UsuariosP.Usuario.model.CuentaUsuario;
import com.UsuariosP.Usuario.service.CuentaUsuarioService;

@RestController
@RequestMapping("api/v1/cuentaUsuario")
@CrossOrigin(origins = "*")

public class CuentaUsuarioController {

    @Autowired
    private CuentaUsuarioService cuentaUsuarioService;

    @PostMapping("/crear")
    public CuentaUsuario crearCuentaUsuario(@RequestBody CuentaUsuario cuentaUsuario){
        return cuentaUsuarioService.crear(cuentaUsuario);
    }

    @GetMapping("/listar")
    public List <CuentaUsuario> listarCuentaUsuario(){
        return cuentaUsuarioService.listar();
    }

    @PutMapping("/modificar/{id}")
    public CuentaUsuario modificarCuentaUsuario(@PathVariable Long id, @RequestBody CuentaUsuario cuentaUsuario){
        return cuentaUsuarioService.modificarCuentaU(id, cuentaUsuario);
    }

    @PostMapping("/autenticar")
    public CuentaUsuario autenticarCuentaUsuario(@RequestBody CuentaUsuario cuentaUsuario){
        return cuentaUsuarioService.autenticar(cuentaUsuario);
    }

    @PutMapping("/desactivar/{id}")
    public CuentaUsuario desactivarCuentaUsuario(@PathVariable Long id){
        return cuentaUsuarioService.desactivar(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarCuentaUsuario(@PathVariable Long id){
        cuentaUsuarioService.eliminar(id);
    }
    
}
