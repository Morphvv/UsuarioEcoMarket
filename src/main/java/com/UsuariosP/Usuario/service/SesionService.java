package com.UsuariosP.Usuario.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UsuariosP.Usuario.exception.RecursoNoEncontradoException;
import com.UsuariosP.Usuario.model.Sesion;
import com.UsuariosP.Usuario.repository.SesionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    public Sesion iniciarSesion (Sesion sesion){
        sesion.setFechaInicio(LocalDateTime.now());
        sesion.setEstadoSesion("ACTIVA");

        return sesionRepository.save(sesion);
    }

    public List <Sesion> listarS(){
        return sesionRepository.findAll();
    }

    public Sesion modificarSesion(Long id, Sesion sesion){
        Sesion existente = sesionRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe una sesion con id " + id));

        existente.setTokenSesion(sesion.getTokenSesion());
        existente.setFechaInicio(sesion.getFechaInicio());
        existente.setFechaExpiracion(sesion.getFechaExpiracion());
        existente.setEstadoSesion(sesion.getEstadoSesion());
        
        return sesionRepository.save(existente);
    }

    public Sesion validarSesion(Long id){
        Sesion existente = sesionRepository.findById(id).orElse(null);

        if (existente != null && existente.getEstadoSesion().equals("ACTIVA") && existente.getFechaExpiracion().isAfter(LocalDateTime.now())) {
            return existente;
        }
        return null;
    }

    public Sesion cerrarSesion(Long id){
        Sesion existente = sesionRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setEstadoSesion("INACTIVA");
        return sesionRepository.save(existente);
    }

    public void eliminar(Long id){
        sesionRepository.deleteById(id);
    }


    
}
