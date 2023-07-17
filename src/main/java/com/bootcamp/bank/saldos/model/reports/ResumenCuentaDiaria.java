package com.bootcamp.bank.saldos.model.reports;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ResumenCuentaDiaria {
    private LocalDate dia;
    private String numeroCuenta;
    private Double depositos;
    private Double retiros;
    private Double saldo;
    private Double promedioDiario;
}
