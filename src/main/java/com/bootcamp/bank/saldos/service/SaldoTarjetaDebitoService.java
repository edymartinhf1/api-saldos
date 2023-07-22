package com.bootcamp.bank.saldos.service;

import com.bootcamp.bank.saldos.model.reports.ReporteCuentaPrincipalDebito;
import reactor.core.publisher.Mono;

public interface SaldoTarjetaDebitoService {
    Mono<ReporteCuentaPrincipalDebito> getSaldoByTarjeta(String numeroTarjetaDebito);
}
