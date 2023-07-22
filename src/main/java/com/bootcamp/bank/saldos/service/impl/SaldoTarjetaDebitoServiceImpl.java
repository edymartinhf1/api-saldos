package com.bootcamp.bank.saldos.service.impl;

import com.bootcamp.bank.saldos.clients.*;
import com.bootcamp.bank.saldos.exception.BusinessException;
import com.bootcamp.bank.saldos.model.reports.ReporteCuentaPrincipalDebito;
import com.bootcamp.bank.saldos.service.SaldoTarjetaDebitoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class SaldoTarjetaDebitoServiceImpl implements SaldoTarjetaDebitoService {

    private final ClientApiOperaciones clientApiOperaciones;

    private final ClientApiCuentas clientApiCuentas;

    private final ClientApiClientes clientApiClientes;

    /**
     * Buscar tarjeta de debito
     * Buscar cuenta principal de tarjeta de debito
     * Buscar cliente relacionado a cuenta principal
     * Obtener depositos y retiros de cuenta, calcular saldo
     * @param numeroTarjetaDebito
     * @return
     */
    @Override
    public Mono<ReporteCuentaPrincipalDebito> getSaldoByTarjeta(String numeroTarjetaDebito) {
        log.info(" SaldoTarjetaDebitoServiceImpl - getSaldoByTarjeta ");
        return clientApiCuentas.getTarjetaDebitoPorNumero(numeroTarjetaDebito)
                .switchIfEmpty(Mono.error(()->new BusinessException("No existe tarjeta de debito con el numero  "+numeroTarjetaDebito)))
                .flatMap(tarjetaDebito->{
                    return clientApiCuentas.getCuentaPorNumero(tarjetaDebito.getNumeroCuentaPrincipal())
                            .flatMap(cuenta->{
                                return clientApiClientes.getClientes(cuenta.getIdCliente())
                                        .flatMap( cliente->{
                                            log.info(" tarjetadebito " +tarjetaDebito.toString());
                                            return Mono.zip(getOperacionesPorTipo(tarjetaDebito.getNumeroCuentaPrincipal(),"DEP"), getOperacionesPorTipo(tarjetaDebito.getNumeroCuentaPrincipal(),"RET"), (deposito,retiro)->{
                                                ReporteCuentaPrincipalDebito reporteCuentaPrincipalDebito=new ReporteCuentaPrincipalDebito();
                                                reporteCuentaPrincipalDebito.setIdCliente(tarjetaDebito.getIdCliente());
                                                reporteCuentaPrincipalDebito.setNumeroCuentaPrincipal(tarjetaDebito.getNumeroCuentaPrincipal());
                                                reporteCuentaPrincipalDebito.setDepositos(deposito);
                                                reporteCuentaPrincipalDebito.setCliente(cliente);
                                                reporteCuentaPrincipalDebito.setCuenta(cuenta);
                                                reporteCuentaPrincipalDebito.setRetiros(retiro);
                                                Double saldos= deposito+(retiro*-1);
                                                reporteCuentaPrincipalDebito.setSaldos(saldos);
                                                return  reporteCuentaPrincipalDebito;
                                            });

                                        });

                            });




                });

    }


    public Mono<Double> getOperacionesPorTipo(String numeroCuenta,String tipo) {
        return clientApiOperaciones
                .getOperacionesPorNumeroCuenta(numeroCuenta,tipo)
                .reduce(0.00, (acum,e)->acum+e.getImporte());
    }
}
