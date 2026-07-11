package com.UsuariosP.Usuario.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "metodos_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMetodoPago;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotBlank(message = "El tipo de pago es obligatorio")
    private String tipoPago;

    @NotBlank(message = "El proveedor de pago es obligatorio")
    private String proveedorPago;
    @NotBlank(message = "El token de pago es obligatorio")
    private String tokenPago;
    @Size(min = 4, max = 4, message = "Los ultimos digitos deben ser exactamente 4")
    private String ultimosDigitos;
    private String alias;
    private Date fechaExpiracion;
    private String titular;
    private Boolean activo;
    private Boolean principal;
}

