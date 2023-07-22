package com.bootcamp.bank.saldos.controller;

import com.bootcamp.bank.saldos.model.reports.ReporteSaldoDiario;
import com.bootcamp.bank.saldos.service.SaldoResumenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Clase Saldo Resumen
 * Entregable 2
 */
@RestController
@RequestMapping("/saldos/resumen")
@Log4j2
@RequiredArgsConstructor
public class SaldosResumenController {

    private final SaldoResumenService saldoResumenService;
    /**
     * Para un cliente se debe generar un resumen con los saldos promedio diarios del mes en curso de todos los productos de crédito o cuentas bancarias que posee.
     * @param idCliente
     * @return
     */
    @GetMapping("/promedio/{idCliente}")
    public Mono<ReporteSaldoDiario> getSaldosPromediosDiarios(@PathVariable String idCliente){
        return saldoResumenService.getSaldosPromediosDiarios(idCliente);
    }
}
