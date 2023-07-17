package com.bootcamp.bank.saldos.model.reports;

import lombok.Data;

import java.util.List;
@Data
public class ResumenCuentaDiario2 {
    private String numeroCuenta;
    private List<ResumenCuentaDiaria> movdiarias;
}
