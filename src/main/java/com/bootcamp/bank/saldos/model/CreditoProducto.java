package com.bootcamp.bank.saldos.model;

import lombok.Data;

import java.util.List;

@Data
public class CreditoProducto {
    private String  idCliente;
    private String  tipoCredito;
    private String  numeroCredito;
    private String  numeroTarjeta;
    private String  fechaCreacion;
    private Double  lineaCredito;
    private Double  saldo;
    private Double  consumos;
    private Double  pagos;
    private List<CargoConsumo> consumosCredito;
    private List<Pago> pagosCredito;
    private Double promedioCreditoDiarioMes;

}