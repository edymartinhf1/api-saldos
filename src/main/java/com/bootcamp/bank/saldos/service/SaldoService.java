package com.bootcamp.bank.saldos.service;

import com.bootcamp.bank.saldos.model.SaldoResponse;
import reactor.core.publisher.Mono;

public interface SaldoService {
    Mono<SaldoResponse> getSaldos(String id);

}
