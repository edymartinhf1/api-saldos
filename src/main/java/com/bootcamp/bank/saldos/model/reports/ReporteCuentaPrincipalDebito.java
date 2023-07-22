package com.bootcamp.bank.saldos.model.reports;

import com.bootcamp.bank.saldos.model.Cliente;
import com.bootcamp.bank.saldos.model.Cuenta;
import lombok.Data;

@Data
public class ReporteCuentaPrincipalDebito {
    private String idCliente;
    private Cliente cliente;
    private Cuenta cuenta;
    private String numeroCuentaPrincipal;
    private Double depositos;
    private Double retiros;
    private Double saldos;
}
