package com.UsuariosP.Usuario.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UsuariosP.Usuario.model.CuentaUsuario;
import com.UsuariosP.Usuario.repository.CuentaUsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class CuentaUsuarioService {

    private static final Logger log = LoggerFactory.getLogger(CuentaUsuarioService.class);

    @Autowired
    private CuentaUsuarioRepository cuentaUsuarioRepository;

    public CuentaUsuario crear(CuentaUsuario cuentaUsuario){
        cuentaUsuario.setRol("CLIENTE");
        cuentaUsuario.setEstadoCuenta("ACTIVA");
        cuentaUsuario.setUltimoAcceso(LocalDateTime.now());
        log.info("Creando cuenta para usuario: {}", cuentaUsuario.getEmail());
        return cuentaUsuarioRepository.save(cuentaUsuario);
    }

    public List<CuentaUsuario> listar(){
        log.info("Listando todas las cuentas de usuario");
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
            log.info("Modificando cuenta con id: {}", id);
            return cuentaUsuarioRepository.save(existente);
        }
        log.warn("No se encontro cuenta con id: {}", id);
        return null;
    }

    public CuentaUsuario autenticar(CuentaUsuario cuentaUsuario){
        CuentaUsuario existente = cuentaUsuarioRepository.findByEmail(cuentaUsuario.getEmail()).orElse(null);

        if (existente != null && existente.getPassword().equals(cuentaUsuario.getPassword())) {
            existente.setUltimoAcceso(LocalDateTime.now());
            log.info("Autenticacion exitosa para: {}", cuentaUsuario.getEmail());
            return cuentaUsuarioRepository.save(existente);
        }
        log.warn("Autenticacion fallida para: {}", cuentaUsuario.getEmail());
        return null;
    }

    public CuentaUsuario desactivar(Long id){
        CuentaUsuario existente = cuentaUsuarioRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setEstadoCuenta("INACTIVA");
            log.info("Desactivando cuenta con id: {}", id);
            return cuentaUsuarioRepository.save(existente);
        }
        log.warn("No se encontro cuenta con id: {}", id);
        return null;
    }

    public void eliminar(Long id){
        log.info("Eliminando cuenta con id: {}", id);
        cuentaUsuarioRepository.deleteById(id);
    }
}
