package com.UsuariosP.Usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.UsuariosP.Usuario.model.MetodoPago;
import com.UsuariosP.Usuario.service.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/metodoPago")
@CrossOrigin(origins = "*")
@Tag(name = "Metodos de pago", description = "Gestion de metodos de pago")

public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @PostMapping("/crear")
    @Operation(summary = "Crear un metodo de pago")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "Metodo de pago creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos") })
    public ResponseEntity<MetodoPago> crearMetodoPago(@Valid @RequestBody MetodoPago metodoPago){
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoPagoService.crear(metodoPago));
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos los metodos de pago")
    @ApiResponse(responseCode = "200", description = "Lista de metodos de pago")
    public ResponseEntity<List<MetodoPago>> listarMetodoPago(){
        return ResponseEntity.ok(metodoPagoService.listar());
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar metodos de pago por usuario")
    @ApiResponse(responseCode = "200", description = "Lista de metodos de pago del usuario")
    public ResponseEntity<List<MetodoPago>> listarPorUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(metodoPagoService.listarPorUsuario(idUsuario));
    }

    @PutMapping("/modificar/{id}")
    @Operation(summary = "Modificar metodo de pago")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Metodo de pago modificado"), @ApiResponse(responseCode = "404", description = "Metodo de pago no encontrado") })
    public ResponseEntity<MetodoPago> modificarMetodoPago(@PathVariable Long id, @Valid @RequestBody MetodoPago metodoPago){
        return ResponseEntity.ok(metodoPagoService.modificarMetodoPago(id, metodoPago));
    }

    @PutMapping("/principal/{id}")
    @Operation(summary = "Marcar metodo de pago como principal")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Metodo de pago marcado como principal"), @ApiResponse(responseCode = "404", description = "Metodo de pago no encontrado") })
    public ResponseEntity<MetodoPago> marcarPrincipal(@PathVariable Long id){
        MetodoPago resultado = metodoPagoService.marcarComoPrincipal(id);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/desactivar/{id}")
    @Operation(summary = "Desactivar metodo de pago")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Metodo de pago desactivado"), @ApiResponse(responseCode = "404", description = "Metodo de pago no encontrado") })
    public ResponseEntity<MetodoPago> desactivar(@PathVariable Long id){
        MetodoPago resultado = metodoPagoService.desactivar(id);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar metodo de pago")
    @ApiResponse(responseCode = "204", description = "Metodo de pago eliminado")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        metodoPagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
