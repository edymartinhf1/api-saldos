package com.bootcamp.bank.saldos.controller;

import com.bootcamp.bank.saldos.model.SaldoResponse;
import com.bootcamp.bank.saldos.service.SaldoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Clase Obtencion Saldos
 * Entregable 1
 */
@RestController
@RequestMapping("/saldos")
@Log4j2
@RequiredArgsConstructor
public class SaldosController {

    private final SaldoService saldoService;

    /**
     * Permite obtener saldos por idCliente
     * @return
     */
    @GetMapping("/{id}")
    public Mono<SaldoResponse> getSaldos(@PathVariable String id){

        return saldoService.getSaldos(id);
    }



}
