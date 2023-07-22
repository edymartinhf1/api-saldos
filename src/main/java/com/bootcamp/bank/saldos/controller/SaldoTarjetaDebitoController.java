package com.bootcamp.bank.saldos.controller;

import com.bootcamp.bank.saldos.model.reports.ReporteCuentaPrincipalDebito;
import com.bootcamp.bank.saldos.service.SaldoTarjetaDebitoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Clase Consultar el saldo de la cuenta principal asociada a la tarjeta de débito
 * Entregable 3
 */
@RestController
@RequestMapping("/saldos/tarjeta-debito")
@Log4j2
@RequiredArgsConstructor
public class SaldoTarjetaDebitoController {

    private final SaldoTarjetaDebitoService saldoTarjetaDebitoService;

    /**
     * Permite obtener el saldo de la cuenta principal vinculada a una tarjeta de debito
     * @param numeroTarjetaDebito
     * @return
     */
    @GetMapping("/{numeroTarjetaDebito}")
    public Mono<ReporteCuentaPrincipalDebito> getSaldoByTarjetaDebito(@PathVariable  String numeroTarjetaDebito) {
        log.info("-- getSaldoByTarjetaDebito --");
        return saldoTarjetaDebitoService.getSaldoByTarjeta(numeroTarjetaDebito);
    }

}
