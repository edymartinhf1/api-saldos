package com.bootcamp.bank.saldos.model.reports;

import com.bootcamp.bank.saldos.model.CreditoProducto;
import com.bootcamp.bank.saldos.model.Cuenta;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResumenDiario {
    private LocalDate diaMes;
    private List<Cuenta> cuentas;
    private List<CreditoProducto> creditos;





}
