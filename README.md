//Funciona en java 21
//Juan Pablo Jofre
puerto 9090

Usuario swagger: http://localhost:9090/swagger-ui/index.html

Crear un usuario body 
{"rut":18543219,"nombre":"Camila","apellido":"Rojas","email":"camila.rojas@gmail.com","telefono":"956781234"}

Crear cuenta usuario body
{"nombreUsuario":"crojas18","email":"crojas18@gmail.com","password":"Camila2026!","usuario":{"rut":18543219}}

Crear direccion envio body
{"usuario":{"rut":18543219},"calle":"Pasaje Los Boldos","numero":"1421","comuna":"Maipu","ciudad":"Santiago","region":"Metropolitana","codigoPostal":"9250000","referencia":"Portón negro, timbre 3","direccionPrincipal":true}

Crear metodo de pago body
{"usuario":{"rut":18543219},"tipoPago":"CREDITO","proveedorPago":"Visa","tokenPago":"tok_vis_4532119","ultimosDigitos":"3782","alias":"Visa BCI","fechaExpiracion":"2028-09-30","titular":"Camila Rojas"}

Crear sesion body 
{"idUsuario":18543219,"tokenSesion":"eyJhbGciOiJIUzI1NiJ9.crojas18","fechaExpiracion":"2026-07-05T23:59:59"}


