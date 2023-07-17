package com.bootcamp.bank.saldos.service;

import com.bootcamp.bank.saldos.model.reports.ReporteSaldoDiario;
import reactor.core.publisher.Mono;

public interface SaldoResumenService {
    Mono<ReporteSaldoDiario> getSaldosPromediosDiarios(String idCliente);
}
