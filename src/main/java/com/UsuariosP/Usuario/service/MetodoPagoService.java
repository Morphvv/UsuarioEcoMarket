package com.UsuariosP.Usuario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UsuariosP.Usuario.model.MetodoPago;
import com.UsuariosP.Usuario.repository.MetodoPagoRepository;

import jakarta.transaction.Transactional;
import com.UsuariosP.Usuario.exception.RecursoNoEncontradoException;

@Service
@Transactional
public class MetodoPagoService {

    private static final Logger log = LoggerFactory.getLogger(MetodoPagoService.class);

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    public MetodoPago crear(MetodoPago metodoPago){
        boolean activoFueNulo = metodoPago.getActivo() == null;
        if (activoFueNulo) {
            metodoPago.setActivo(true);
        }
        if (metodoPago.getPrincipal() == null) {
            metodoPago.setPrincipal(!activoFueNulo && Boolean.TRUE.equals(metodoPago.getActivo()));
        }
        log.info("Creando metodo de pago para usuario: {}", metodoPago.getUsuario() != null ? metodoPago.getUsuario().getRut() : "sin usuario");
        return metodoPagoRepository.save(metodoPago);
    }

    public List<MetodoPago> listar(){
        log.info("Listando todos los metodos de pago");
        return metodoPagoRepository.findAll();
    }

    public List<MetodoPago> listarPorUsuario(Long idUsuario){
        log.info("Listando metodos de pago del usuario: {}", idUsuario);
        return metodoPagoRepository.findByUsuarioRutAndActivoTrue(idUsuario);
    }

    public MetodoPago modificarMetodoPago(Long id, MetodoPago metodoPago){
        MetodoPago existente = metodoPagoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("No existe un metodo de pago con id " + id));

        existente.setTipoPago(metodoPago.getTipoPago());
        existente.setProveedorPago(metodoPago.getProveedorPago());
        existente.setTokenPago(metodoPago.getTokenPago());
        existente.setUltimosDigitos(metodoPago.getUltimosDigitos());
        existente.setAlias(metodoPago.getAlias());
        existente.setFechaExpiracion(metodoPago.getFechaExpiracion());
        existente.setTitular(metodoPago.getTitular());
        existente.setActivo(metodoPago.getActivo());
        existente.setPrincipal(metodoPago.getPrincipal());
        log.info("Modificando metodo de pago con id: {}", id);
        return metodoPagoRepository.save(existente);
    }

    public MetodoPago marcarComoPrincipal(Long id){
        MetodoPago existente = metodoPagoRepository.findById(id).orElse(null);
        if (existente == null) {
            log.warn("No se encontro metodo de pago con id: {}", id);
            return null;
        }

        List<MetodoPago> metodos = metodoPagoRepository.findByUsuarioRut(existente.getUsuario().getRut());
        for (MetodoPago metodo : metodos) {
            if (metodo.getIdMetodoPago() != existente.getIdMetodoPago()) {
                metodo.setPrincipal(false);
                metodoPagoRepository.save(metodo);
            }
        }
        existente.setPrincipal(true);
        log.info("Metodo de pago {} marcado como principal", id);
        return metodoPagoRepository.save(existente);
    }

    public MetodoPago desactivar(Long id){
        MetodoPago existente = metodoPagoRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setActivo(false);
            log.info("Desactivando metodo de pago con id: {}", id);
            return metodoPagoRepository.save(existente);
        }
        log.warn("No se encontro metodo de pago con id: {}", id);
        return null;
    }

    public void eliminar(Long id){
        log.info("Eliminando metodo de pago con id: {}", id);
        metodoPagoRepository.deleteById(id);
    }
}
