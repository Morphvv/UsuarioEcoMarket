package com.UsuariosP.Usuario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UsuariosP.Usuario.model.DireccionEnvio;
import com.UsuariosP.Usuario.repository.DireccionEnvioRepository;

import jakarta.transaction.Transactional;
import com.UsuariosP.Usuario.exception.RecursoNoEncontradoException;

@Service
@Transactional
public class DireccionEnvioService {

    private static final Logger log = LoggerFactory.getLogger(DireccionEnvioService.class);

    @Autowired
    private DireccionEnvioRepository direccionEnvioRepository;

    public DireccionEnvio crear(DireccionEnvio direccionEnvio){
        direccionEnvio.setActiva(true);
        if (direccionEnvio.getDireccionPrincipal() == null) {
            direccionEnvio.setDireccionPrincipal(false);
        }
        log.info("Creando direccion de envio para usuario: {}", direccionEnvio.getUsuario() != null ? direccionEnvio.getUsuario().getRut() : "sin usuario");
        return direccionEnvioRepository.save(direccionEnvio);
    }

    public List<DireccionEnvio> listar(){
        log.info("Listando todas las direcciones de envio");
        return direccionEnvioRepository.findAll();
    }

    public List<DireccionEnvio> listarPorUsuario(Long idUsuario){
        log.info("Listando direcciones de envio del usuario: {}", idUsuario);
        return direccionEnvioRepository.findByUsuarioRutAndActivaTrue(idUsuario);
    }

    public DireccionEnvio modificarDireccionEnvio(Long id, DireccionEnvio direccionEnvio){
        DireccionEnvio existente = direccionEnvioRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe una direccion con id " + id));

        existente.setCalle(direccionEnvio.getCalle());
        existente.setNumero(direccionEnvio.getNumero());
        existente.setComuna(direccionEnvio.getComuna());
        existente.setCiudad(direccionEnvio.getCiudad());
        existente.setRegion(direccionEnvio.getRegion());
        existente.setCodigoPostal(direccionEnvio.getCodigoPostal());
        existente.setReferencia(direccionEnvio.getReferencia());
        existente.setDireccionPrincipal(direccionEnvio.getDireccionPrincipal());
        existente.setActiva(direccionEnvio.getActiva());
        log.info("Modificando direccion con id: {}", id);
        return direccionEnvioRepository.save(existente);
    }

    public DireccionEnvio marcarComoPrincipal(Long id){
        DireccionEnvio existente = direccionEnvioRepository.findById(id).orElse(null);
        if (existente == null) {
            log.warn("No se encontro direccion con id: {}", id);
            return null;
        }

        List<DireccionEnvio> direcciones = direccionEnvioRepository.findByUsuarioRut(existente.getUsuario().getRut());
        for (DireccionEnvio direccion : direcciones) {
            if (!direccion.getIdDireccion().equals(existente.getIdDireccion())) {
                direccion.setDireccionPrincipal(false);
                direccionEnvioRepository.save(direccion);
            }
        }
        existente.setDireccionPrincipal(true);
        log.info("Direccion {} marcada como principal", id);
        return direccionEnvioRepository.save(existente);
    }

    public DireccionEnvio desactivar(Long id){
        DireccionEnvio existente = direccionEnvioRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setActiva(false);
            log.info("Desactivando direccion con id: {}", id);
            return direccionEnvioRepository.save(existente);
        }
        log.warn("No se encontro direccion con id: {}", id);
        return null;
    }

    public void eliminar(Long id){
        log.info("Eliminando direccion con id: {}", id);
        direccionEnvioRepository.deleteById(id);
    }
}
