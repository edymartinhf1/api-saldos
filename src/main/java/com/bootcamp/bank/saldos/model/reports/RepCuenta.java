package com.bootcamp.bank.saldos.model.reports;

import com.bootcamp.bank.saldos.model.OperacionCta;
import lombok.Data;

import java.util.List;
@Data
public class RepCuenta {
    private String  id;
    private String  idCliente;
    private String  numeroCuenta;
    private String  fechaCreacion;
    private String  estado;
    private String  tipoCuenta; // AHO: ahorro  , CTE : cuenta corriente , PZF: plazo fijo
    private Boolean flgComisionMantenimiento;
    private Boolean flgLimiteMovMensual;
    private Integer numMaximoMovimientos;
    private Double saldo;
    private List<OperacionCta> operaciones;
}
