package com.bootcamp.bank.saldos.service.impl;

import com.bootcamp.bank.saldos.clients.*;
import com.bootcamp.bank.saldos.exception.BusinessException;
import com.bootcamp.bank.saldos.model.SaldoResponse;
import com.bootcamp.bank.saldos.service.SaldoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
@Log4j2
@RequiredArgsConstructor
public class SaldoServiceImpl implements SaldoService {

    private final ClientApiClientes clientApiClientes;

    private final ClientApiCreditos clientApiCreditos;

    private final ClientApiCuentas clientApiCuentas;

    private final ClientApiConsumos clientApiConsumos;

    private final ClientApiPagos clientApiPagos;

    private final ClientApiOperaciones clientApiOperaciones;

    /**
     * Permite obtener saldos por id cliente
     * @param id
     * @return
     */
    @Override
    public Mono<SaldoResponse> getSaldos(String id) {
        // verificar el cliente por id
        // obtener los productos creditos por cliente id
        // obtener los pagos y consumos por cada producto de credito
        // obtener las cuentas bancarias por cliente id
        // obtener los depositos y retiros por cuenta  cuenta bancaria

        return clientApiClientes.getClientes(id)
                .switchIfEmpty(Mono.error(()->new BusinessException("No existe cliente con el id "+id)))
                .map(cliente-> {
                    SaldoResponse saldo = new SaldoResponse();
                    saldo.setIdCliente(id);
                    saldo.setCliente(cliente);
                    return saldo;
                }).flatMap(saldoResponse->{
                    return clientApiCreditos.getCreditos(id)
                            .flatMap(cred->{
                                if (log.isDebugEnabled()) {
                                    log.info("numero credito " + cred.getNumeroCredito());
                                }
                                return Mono.zip(getConsumnos(cred.getNumeroCredito()), getPagos(cred.getNumeroCredito()), (consumos,pagos)->{
                                    cred.setPagos(pagos);
                                    cred.setConsumos(consumos);
                                    cred.setSaldo(cred.getLineaCredito()+(cred.getConsumos()*-1)+cred.getPagos());
                                    return  cred;
                                });

                            })
                            .collectList()
                            .map(listaCreditos->{
                                SaldoResponse saldo =new SaldoResponse();
                                saldo.setCliente(saldoResponse.getCliente());
                                saldo.setIdCliente(id);
                                saldo.setCreditos(listaCreditos);
                                return saldo;
                            });
                }).flatMap(saldoRes->{
                    return clientApiCuentas.getCuentas(id)
                            .flatMap(cue->{
                                log.info("numero cuenta "+cue.getNumeroCuenta());
                                return Mono.zip(getOperacionesPorTipo(cue.getNumeroCuenta(),"DEP"), getOperacionesPorTipo(cue.getNumeroCuenta(),"RET"), (deposito,retiro)->{
                                    cue.setDepositos(deposito);
                                    cue.setRetiros(retiro);
                                    return  cue;
                                });
                            })
                            .collectList()
                            .map(cta->{
                                SaldoResponse saldo =new SaldoResponse();
                                saldo.setCliente(saldoRes.getCliente());
                                saldo.setCreditos(saldoRes.getCreditos());
                                saldo.setIdCliente(id);
                                saldo.setCuentas(cta);
                                return saldo;
                            });
                });
    }


    /**
     * Permite calcular el total de consumos por numero de credito
     * @param numeroCredito
     * @return
     */
    public Mono<Double> getConsumnos(String numeroCredito) {
        return clientApiConsumos
                .getConsumosNumeroCredito(numeroCredito)
                .reduce(0.00,(acum,e)->acum+e.getImporte());
    }

    /**
     * Permite obtener el total de pagos por numero de credito
     * @param numeroCredito
     * @return
     */
    public Mono<Double> getPagos(String numeroCredito) {
        return clientApiPagos
                .getPagosNumeroCredito(numeroCredito)
                .reduce(0.00,(acum,e)->acum+e.getImporte());
    }

    /**
     * Permite obtener el total de operaciones por numero de cuenta y por tipo
     * @param numeroCuenta
     * @param tipo
     * @return
     */
    public Mono<Double> getOperacionesPorTipo(String numeroCuenta,String tipo) {
        return clientApiOperaciones
                .getOperacionesPorNumeroCuenta(numeroCuenta,tipo)
                .reduce(0.00, (acum,e)->acum+e.getImporte());
    }


}
