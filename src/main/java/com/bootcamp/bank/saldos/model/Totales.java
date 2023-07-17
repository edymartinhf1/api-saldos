package com.bootcamp.bank.saldos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Totales {
    private Double totalConsumos;
    private Double toalPagos;
}
