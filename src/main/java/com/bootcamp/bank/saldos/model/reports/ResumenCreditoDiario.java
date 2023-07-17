package com.bootcamp.bank.saldos.model.reports;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ResumenCreditoDiario {
    private LocalDate dia;
    private String numeroCredito;
    private Double consumos;
    private Double pagos;
    private Double saldo;
    private Double promedioDiario;
}
