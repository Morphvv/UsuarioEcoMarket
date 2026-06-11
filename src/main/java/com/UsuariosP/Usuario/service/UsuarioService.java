package com.UsuariosP.Usuario.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Crear
    public Usuario crear (Usuario usuario){
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setEstadoUsuario("ACTIVO");
        return usuarioRepository.save(usuario);

    }
    
    //Guardar
    public Usuario guardarUsuario(Usuario usuario){
        if (usuario.getCuentaUsuario() != null){
            usuario.getCuentaUsuario().setUsuario(usuario);
        }
        return usuarioRepository.save(usuario);
    }

    //Listar
    public List <Usuario> listar(){
        return usuarioRepository.findAll();
    }

    //Modificar
    public Usuario modificarUsuario(Long id, Usuario usuario){
        Usuario existente = usuarioRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(usuario.getNombre());
            existente.setApellido(usuario.getApellido());
            existente.setEmail(usuario.getEmail());
            existente.setTelefono(usuario.getTelefono());
            existente.setEstadoUsuario(usuario.getEstadoUsuario());
        }
        return usuarioRepository.save(existente);
    }

    //Buscar por el rut
    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id).orElse(null);
    }

    //Desactivar por rut
    public Usuario desactivar(Long id){
        Usuario existente = usuarioRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setEstadoUsuario("INACTIVO");
            return usuarioRepository.save(existente);
        }
        return null;
    }

    //Eliminar por rut 
    public void eliminar(Long id){
        usuarioRepository.deleteById(id);
    }

}
