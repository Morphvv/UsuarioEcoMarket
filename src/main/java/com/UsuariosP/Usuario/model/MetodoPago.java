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

    private String tipoPago;
    private String proveedorPago;
    private String tokenPago;
    private String ultimosDigitos;
    private String alias;
    private Date fechaExpiracion;
    private String titular;
    private Boolean activo;
    private Boolean principal;
}

