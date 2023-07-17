package com.bootcamp.bank.saldos.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FechasBean {
    private LocalDateTime fechaInicial;
    private LocalDateTime fechaFinal;
    private String fechaInicialT;
    private String fechaFinT;
}
