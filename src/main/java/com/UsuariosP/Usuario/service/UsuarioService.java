package com.UsuariosP.Usuario.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.repository.UsuarioRepository;
import com.UsuariosP.Usuario.exception.RecursoNoEncontradoException;


@Service
@Transactional
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Crear
    public Usuario crear(Usuario usuario){
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setEstadoUsuario("ACTIVO");
        log.info("Creando usuario con rut: {}", usuario.getRut());
        return usuarioRepository.save(usuario);
    }

    //Guardar
    public Usuario guardarUsuario(Usuario usuario){
        if (usuario.getCuentaUsuario() != null){
            usuario.getCuentaUsuario().setUsuario(usuario);
        }
        log.info("Guardando usuario con rut: {}", usuario.getRut());
        return usuarioRepository.save(usuario);
    }

    //Listar
    public List<Usuario> listar(){
        log.info("Listando todos los usuarios");
        return usuarioRepository.findAll();
    }

    //Modificar
    public Usuario modificarUsuario(Long id, Usuario usuario){
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con ese rut " + id));
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setEmail(usuario.getEmail());
        existente.setTelefono(usuario.getTelefono());
        existente.setEstadoUsuario(usuario.getEstadoUsuario());
        log.info("Modificando usuario con rut: {}", id);
        return usuarioRepository.save(existente);
    }

    //Buscar por el rut
    public Usuario buscarPorId(Long id){
        log.info("Buscando usuario con rut: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con rut " + id));
    }

    //Desactivar por rut
    public Usuario desactivar(Long id){
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con rut " + id));
        existente.setEstadoUsuario("INACTIVO");
        log.info("Desactivando usuario con rut: {}", id);
        return usuarioRepository.save(existente);
    }

    //Eliminar por rut
    public void eliminar(Long id){
        log.info("Eliminando usuario con rut: {}", id);
        usuarioRepository.deleteById(id);
    }

}
