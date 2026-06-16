package com.UsuariosP.Usuario.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UsuariosP.Usuario.model.CuentaUsuario;
import com.UsuariosP.Usuario.repository.CuentaUsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class CuentaUsuarioService {
    
    @Autowired
    private CuentaUsuarioRepository cuentaUsuarioRepository;

    public CuentaUsuario crear(CuentaUsuario cuentaUsuario){
        cuentaUsuario.setRol("CLIENTE");
        cuentaUsuario.setEstadoCuenta("ACTIVA");
        cuentaUsuario.setUltimoAcceso(LocalDateTime.now());
        
        return cuentaUsuarioRepository.save(cuentaUsuario);
    }

    public List <CuentaUsuario> listar(){
        return cuentaUsuarioRepository.findAll();
    }

    public CuentaUsuario modificarCuentaU(Long id, CuentaUsuario cuentaUsuario){
        CuentaUsuario existente = cuentaUsuarioRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombreUsuario(cuentaUsuario.getNombreUsuario());
            existente.setEmail(cuentaUsuario.getEmail());
            existente.setPassword(cuentaUsuario.getPassword());
            existente.setRol(cuentaUsuario.getRol());
            existente.setEstadoCuenta(cuentaUsuario.getEstadoCuenta());
            existente.setUltimoAcceso(cuentaUsuario.getUltimoAcceso());
            
            return cuentaUsuarioRepository.save(existente);


        }
        return null;
    }

    public CuentaUsuario autenticar(CuentaUsuario cuentaUsuario){
        CuentaUsuario existente = cuentaUsuarioRepository.findByEmail(cuentaUsuario.getEmail()).orElse(null);

        if (existente != null && existente.getPassword().equals(cuentaUsuario.getPassword())) {
            existente.setUltimoAcceso(java.time.LocalDateTime.now());
            return cuentaUsuarioRepository.save(existente);
        }
        return null;
    }
    
    public CuentaUsuario desactivar(Long id){
        CuentaUsuario existente = cuentaUsuarioRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setEstadoCuenta("INACTIVA");
            return cuentaUsuarioRepository.save(existente);
        }
        return null;
    }

    public void eliminar(Long id){
        cuentaUsuarioRepository.deleteById(id);
    }
}
