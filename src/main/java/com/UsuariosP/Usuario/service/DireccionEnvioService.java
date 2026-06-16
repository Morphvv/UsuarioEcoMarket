package com.UsuariosP.Usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UsuariosP.Usuario.model.DireccionEnvio;
import com.UsuariosP.Usuario.repository.DireccionEnvioRepository;

import jakarta.transaction.Transactional;
import com.UsuariosP.Usuario.exception.RecursoNoEncontradoException;

@Service
@Transactional
public class DireccionEnvioService {

    @Autowired
    private DireccionEnvioRepository direccionEnvioRepository;

    public DireccionEnvio crear(DireccionEnvio direccionEnvio){
        direccionEnvio.setActiva(true);

        if (direccionEnvio.getDireccionPrincipal() == null) {
            direccionEnvio.setDireccionPrincipal(false);
        }
        return direccionEnvioRepository.save(direccionEnvio);
    }

    public List<DireccionEnvio> listar(){
        return direccionEnvioRepository.findAll();
    }

    public List<DireccionEnvio> listarPorUsuario(Long idUsuario){
        return direccionEnvioRepository.findByUsuarioRutAndActivaTrue(idUsuario);
    }

    public DireccionEnvio modificarDireccionEnvio(Long id, DireccionEnvio direccionEnvio){
        DireccionEnvio existente = direccionEnvioRepository.findById(id).orElse(null);
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe una direccion con id " + id));

        if (existente != null) {
            existente.setCalle(direccionEnvio.getCalle());
            existente.setNumero(direccionEnvio.getNumero());
            existente.setComuna(direccionEnvio.getComuna());
            existente.setCiudad(direccionEnvio.getCiudad());
            existente.setRegion(direccionEnvio.getRegion());
            existente.setCodigoPostal(direccionEnvio.getCodigoPostal());
            existente.setReferencia(direccionEnvio.getReferencia());
            existente.setDireccionPrincipal(direccionEnvio.getDireccionPrincipal());
            existente.setActiva(direccionEnvio.getActiva());

            return direccionEnvioRepository.save(existente);
        }
        return null;
    }

    public DireccionEnvio marcarComoPrincipal(Long id){
        DireccionEnvio existente = direccionEnvioRepository.findById(id).orElse(null);
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe una direccion con id " + id));
            
        if (existente != null){
            List<DireccionEnvio> direcciones = direccionEnvioRepository.findByUsuarioRut(existente.getUsuario().getRut());
            for (DireccionEnvio direccion : direcciones) {
                direccion.setDireccionPrincipal(false);
                direccionEnvioRepository.save(direccion);
            }
            existente.setDireccionPrincipal(true);
            return direccionEnvioRepository.save(existente);
        }
        return null;
    }

    public DireccionEnvio desactivar(Long id){
        DireccionEnvio existente = direccionEnvioRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setActiva(false);
            return direccionEnvioRepository.save(existente);
        }
        return null;
    }

    public void eliminar(Long id){
        direccionEnvioRepository.deleteById(id);
    }
}
