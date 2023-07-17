package com.bootcamp.bank.saldos.model.reports;

import com.bootcamp.bank.saldos.model.Cliente;
import com.bootcamp.bank.saldos.model.CreditoProducto;
import com.bootcamp.bank.saldos.model.Cuenta;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReporteSaldoDiario {
    private String idCliente;
    private Cliente cliente;
    private List<Cuenta> cuentas;
    private List<CreditoProducto> creditos;
    private List<ResumenDiario> resumenDiario;
    private List<Map<String, List<ResumenCuentaDiaria>>>  resumenCtasBancariasDiario;
    private List<Map<String, List<ResumenCreditoDiario>>>  resumenProductosCreditosDiario;

}
