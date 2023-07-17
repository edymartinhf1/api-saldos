package com.bootcamp.bank.saldos.model.reports;

import com.bootcamp.bank.saldos.model.CargoConsumo;
import com.bootcamp.bank.saldos.model.Pago;
import lombok.Data;

import java.util.List;
@Data
public class RepCreditoProducto {
    private String  idCliente;
    private String  tipoCredito;
    private String  numeroCredito;
    private String  numeroTarjeta;
    private String  fechaCreacion;
    private Double  lineaCredito;
    private List<CargoConsumo> cargos;
    private List<Pago> pagos;

}
