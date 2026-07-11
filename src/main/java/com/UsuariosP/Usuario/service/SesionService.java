package com.UsuariosP.Usuario.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UsuariosP.Usuario.exception.RecursoNoEncontradoException;
import com.UsuariosP.Usuario.model.Sesion;
import com.UsuariosP.Usuario.repository.SesionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SesionService {

    private static final Logger log = LoggerFactory.getLogger(SesionService.class);

    @Autowired
    private SesionRepository sesionRepository;

    public Sesion iniciarSesion(Sesion sesion){
        sesion.setFechaInicio(LocalDateTime.now());
        sesion.setEstadoSesion("ACTIVA");
        log.info("Iniciando sesion para usuario: {}", sesion.getIdUsuario());
        return sesionRepository.save(sesion);
    }

    public List<Sesion> listarS(){
        log.info("Listando todas las sesiones");
        return sesionRepository.findAll();
    }

    public Sesion modificarSesion(Long id, Sesion sesion){
        Sesion existente = sesionRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe una sesion con id " + id));

        existente.setTokenSesion(sesion.getTokenSesion());
        existente.setFechaInicio(sesion.getFechaInicio());
        existente.setFechaExpiracion(sesion.getFechaExpiracion());
        existente.setEstadoSesion(sesion.getEstadoSesion());
        log.info("Modificando sesion con id: {}", id);
        return sesionRepository.save(existente);
    }

    public Sesion validarSesion(Long id){
        Sesion existente = sesionRepository.findById(id).orElse(null);

        if (existente != null && existente.getEstadoSesion().equals("ACTIVA") && existente.getFechaExpiracion().isAfter(LocalDateTime.now())) {
            log.info("Sesion {} valida", id);
            return existente;
        }
        log.warn("Sesion {} invalida o expirada", id);
        return null;
    }

    public Sesion cerrarSesion(Long id){
        Sesion existente = sesionRepository.findById(id).orElse(null);
        if (existente == null) {
            log.warn("No se encontro sesion con id: {}", id);
            return null;
        }
        existente.setEstadoSesion("INACTIVA");
        log.info("Cerrando sesion con id: {}", id);
        return sesionRepository.save(existente);
    }

    public void eliminar(Long id){
        log.info("Eliminando sesion con id: {}", id);
        sesionRepository.deleteById(id);
    }


}
