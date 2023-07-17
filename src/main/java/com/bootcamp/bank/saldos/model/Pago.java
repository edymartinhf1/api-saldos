package com.bootcamp.bank.saldos.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Pago {
    private String id;
    private String idCliente;
    private String numeroCredito;
    private LocalDateTime fechaPago;
    private Double importe;
}
