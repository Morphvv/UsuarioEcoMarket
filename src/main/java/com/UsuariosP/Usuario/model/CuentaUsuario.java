package com.UsuariosP.Usuario.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cuentas_usuario")
public class CuentaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato de email es invalido")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private String estadoCuenta;

    private LocalDateTime ultimoAcceso;

    @OneToOne
    @JoinColumn(name = "usuario_rut") 
    private Usuario usuario;
}

