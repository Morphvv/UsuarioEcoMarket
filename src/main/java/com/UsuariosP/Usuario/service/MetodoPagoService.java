package com.UsuariosP.Usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UsuariosP.Usuario.model.MetodoPago;
import com.UsuariosP.Usuario.repository.MetodoPagoRepository;

import jakarta.transaction.Transactional;
import com.UsuariosP.Usuario.exception.RecursoNoEncontradoException;

@Service
@Transactional
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    public MetodoPago crear(MetodoPago metodoPago){
        metodoPago.setActivo(true);

        if (metodoPago.getPrincipal() == null) {
            metodoPago.setPrincipal(false);
        }

        return metodoPagoRepository.save(metodoPago);
    }

    public List<MetodoPago> listar(){
        return metodoPagoRepository.findAll();
    }

    public List<MetodoPago> listarPorUsuario(Long idUsuario){
        return metodoPagoRepository.findByUsuarioRutAndActivoTrue(idUsuario);
    }

    public MetodoPago modificarMetodoPago(Long id, MetodoPago metodoPago){
        MetodoPago existente = metodoPagoRepository.findById(id).orElse(null);
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un metodo de pago con id " + id));

        if (existente != null) {
            existente.setTipoPago(metodoPago.getTipoPago());
            existente.setProveedorPago(metodoPago.getProveedorPago());
            existente.setTokenPago(metodoPago.getTokenPago());
            existente.setUltimosDigitos(metodoPago.getUltimosDigitos());
            existente.setAlias(metodoPago.getAlias());
            existente.setFechaExpiracion(metodoPago.getFechaExpiracion());
            existente.setTitular(metodoPago.getTitular());
            existente.setActivo(metodoPago.getActivo());
            existente.setPrincipal(metodoPago.getPrincipal());

            return metodoPagoRepository.save(existente);
        }
        return null;
    }

    public MetodoPago marcarComoPrincipal(Long id){
        MetodoPago existente = metodoPagoRepository.findById(id).orElse(null);
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un metodo de pago con id " + id));

        if (existente != null){
            List<MetodoPago> metodos = metodoPagoRepository.findByUsuarioRut(existente.getUsuario().getRut());
            for (MetodoPago metodo : metodos) {
                metodo.setPrincipal(false);
                metodoPagoRepository.save(metodo);
            }
            existente.setPrincipal(true);
            return metodoPagoRepository.save(existente);
        }

        return null;
    }

    public MetodoPago desactivar(Long id){
        MetodoPago existente = metodoPagoRepository.findById(id).orElse(null);
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un metodo de pago con id " + id));

        if (existente != null) {
            existente.setActivo(false);
            return metodoPagoRepository.save(existente);
        }
        return null;
    }

    public void eliminar(Long id){
        metodoPagoRepository.deleteById(id);
    }
}
