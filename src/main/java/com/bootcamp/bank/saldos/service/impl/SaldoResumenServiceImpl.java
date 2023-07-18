package com.bootcamp.bank.saldos.service.impl;

import com.bootcamp.bank.saldos.clients.*;
import com.bootcamp.bank.saldos.exception.BusinessException;
import com.bootcamp.bank.saldos.model.*;
import com.bootcamp.bank.saldos.model.reports.*;
import com.bootcamp.bank.saldos.service.SaldoResumenService;
import com.bootcamp.bank.saldos.service.SaldoService;
import com.bootcamp.bank.saldos.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaldoResumenServiceImpl implements SaldoResumenService {

    private final ClientApiClientes clientApiClientes;

    private final ClientApiCreditos clientApiCreditos;

    private final ClientApiCuentas clientApiCuentas;

    private final ClientApiConsumos clientApiConsumos;

    private final ClientApiPagos clientApiPagos;

    private final ClientApiOperaciones clientApiOperaciones;

    private final SaldoService saldoService;

    /**
     * Promedios Diarios
     * @param id
     * @return
     */
    @Override
    public Mono<ReporteSaldoDiario> getSaldosPromediosDiarios(String id) {
        // verificar id cliente
        // obtener los productos credito por id cliente
        // obtener los consumos y los pagos por producto credito
        // obtener las cuentas bancarias por id cliente
        // obtener las operaciones de cargo y abono por cuenta bancaria
        // generar dias de mes actual en curso
        // calcular promedio diario cuentas bancarias
        // calcular promedio diario productos credito
        return clientApiClientes.getClientes(id)
                .switchIfEmpty(Mono.error(()->new BusinessException("No existe cliente con el id "+id)))
                .map(cliente-> {
                    ReporteSaldoDiario response=new ReporteSaldoDiario();
                    response.setCliente(cliente);
                    response.setIdCliente(cliente.getId());
                    return response;
                }).flatMap(saldo-> {
                    return  clientApiCreditos.getCreditos(id)
                            .flatMap(credito->{
                                if (log.isDebugEnabled()) {
                                    log.info("numero credito " + credito.getNumeroCredito());
                                }
                                return Mono.zip(getConsumos(credito.getNumeroCredito()),getPagos(credito.getNumeroCredito()))
                                        .map(tuple->{
                                            credito.setConsumosCredito(tuple.getT1());
                                            credito.setPagosCredito(tuple.getT2());
                                           return credito;
                                        });
                            })
                            .collectList()
                            .map(creditos->{
                                saldo.setCreditos(creditos);
                                return saldo;
                            });

                }).flatMap(saldo->{
                    return clientApiCuentas.getCuentas(id)
                            .flatMap(cuentab->{
                                if (log.isDebugEnabled()) {
                                    log.info("numero cuenta" + cuentab.getNumeroCuenta());
                                }
                                return getOperaciones(cuentab.getNumeroCuenta())
                                        .map(operaciones->{
                                            cuentab.setOperacionesCta(operaciones);
                                            return cuentab;
                                        });
                            })
                            .collectList()
                            .map(cuentas->{
                               saldo.setCuentas(cuentas);
                               return saldo;
                            });

                }).map( saldo->{
                   return asignaDiasPorMes(saldo);
                }).map( saldo ->{
                    return calcularSaldosCuentaDiarios(saldo);
                }).map(saldo->{
                    return calcularSaldosCreditosDiarios(saldo);
                });

    }


    /**
     * Asignar dias del mes actual
     * @param response
     * @return
     */
    public ReporteSaldoDiario asignaDiasPorMes(ReporteSaldoDiario response){
        YearMonth ym = YearMonth.now() ;
        LocalDate start = ym.atDay( 1 ) ;
        LocalDate end = ym.plusMonths( 1 ).atDay( 1 ) ;
        List< LocalDate > dates = start.datesUntil( end ).toList() ;

        List<ResumenDiario> dias= dates
                .stream()
                .map(n->{
                    ResumenDiario resumenDiario =new ResumenDiario();
                    resumenDiario.setDiaMes(n);
                    resumenDiario.setCreditos(response.getCreditos());
                    resumenDiario.setCuentas(response.getCuentas());
                    return resumenDiario;
                })
                .toList();

        response.setResumenDiario(dias);
        return obtenerTransaccionesPorDia(response);
    }


    /**
     * Calcular Promedio diario
     * @param response
     * @return
     */
    public ReporteSaldoDiario obtenerTransaccionesPorDia(ReporteSaldoDiario response){

        List<ResumenDiario> resumen=response.getResumenDiario().stream().map(resumenDiario->{

            LocalDate diaMes = resumenDiario.getDiaMes();
            // saldo resumen diario por cada cuenta bancaria
            List<Cuenta> cuentasConPromedio= resumenDiario.getCuentas().stream().map(cuenta->{
               Cuenta cuentaN=new Cuenta();
               cuentaN.setOperacionesCta(cuenta.getOperacionesCta());
               CuentaCalculos cuentaCalculos = this.calcularOperacionesPorCtaPordia(diaMes,cuenta.getOperacionesCta());
               cuentaN.setNumeroCuenta(cuenta.getNumeroCuenta());
               cuentaN.setDepositos(cuentaCalculos.getDepositos());
               cuentaN.setRetiros(cuentaCalculos.getRetiros());
               return cuentaN;
            }).toList();
            // saldo resumen diario por cada producto credito
            List<CreditoProducto> creditosconPromedio = resumenDiario.getCreditos().stream().map(credito->{
                CreditoProducto creditoN=new CreditoProducto();
                creditoN.setNumeroCredito(credito.getNumeroCredito());
                creditoN.setPagosCredito(credito.getPagosCredito());
                creditoN.setConsumosCredito(credito.getConsumosCredito());
                CreditoCalculos creditoCalculos = this.calcularOperacionesCreditosPordia(diaMes,credito.getConsumosCredito(),credito.getPagosCredito());
                creditoN.setConsumos(creditoCalculos.getConsumos());
                creditoN.setPagos(creditoCalculos.getPagos());
                return creditoN;
            }).toList();

            resumenDiario.setCreditos(creditosconPromedio);
            resumenDiario.setCuentas(cuentasConPromedio);

            return resumenDiario;
        }).collect(Collectors.toList());

        response.setResumenDiario(resumen);
        return response;
    }

    public CuentaCalculos calcularOperacionesPorCtaPordia(LocalDate dia,List<OperacionCta> operaciones){
        Double depositos = operaciones.stream()
                .filter(c-> c.getTipoOperacion().equals("DEP"))
                .filter(e-> e.getFechaOperacion().toLocalDate().isEqual(dia))
                .map(e->e.getImporte())
                .reduce(0.0,(a,b)->a+b);
        Double retiros = operaciones.stream()
                .filter(c-> c.getTipoOperacion().equals("RET"))
                .filter(e-> e.getFechaOperacion().toLocalDate().isEqual(dia))
                .map(e->e.getImporte())
                .reduce(0.0,(a,b)->a+b);

        CuentaCalculos cuentaCalculos=new CuentaCalculos();
        cuentaCalculos.setDepositos(depositos);
        cuentaCalculos.setRetiros(retiros);
        return cuentaCalculos;
    }

    public CreditoCalculos calcularOperacionesCreditosPordia(LocalDate dia,List<CargoConsumo> cargos, List<Pago> pagos){
        Double sumConsumos = cargos.stream()
                .filter(c-> c.getFechaConsumo().toLocalDate().isEqual(dia))
                .map(e->e.getImporte())
                .reduce(0.0,(a,b)->a+b);
        Double sumPagos = pagos.stream()
                .filter(c->c.getFechaPago().toLocalDate().isEqual(dia))
                .map(e->e.getImporte())
                .reduce(0.0,(a,b)->a+b);
        CreditoCalculos creditoCalculos=new CreditoCalculos();
        creditoCalculos.setConsumos(sumConsumos);
        creditoCalculos.setPagos(sumPagos);
        return creditoCalculos;
    }

    /**
     * saldos por dia
     * @param reporteSaldoDiario
     * @return
     */
    public ReporteSaldoDiario calcularSaldosCuentaDiarios(ReporteSaldoDiario reporteSaldoDiario){

        List<ResumenDiario> resumenDiarioLst= reporteSaldoDiario.getResumenDiario();

        List<List<ResumenCuentaDiaria>> resumenCtaDiaria = resumenDiarioLst
                .stream()
                .map( resumen->{
                    return resumen.getCuentas()
                    .stream()
                    .map(cuenta->{
                        ResumenCuentaDiaria resumenCta=new ResumenCuentaDiaria();
                        resumenCta.setDia(resumen.getDiaMes());
                        resumenCta.setNumeroCuenta(cuenta.getNumeroCuenta());
                        resumenCta.setRetiros(cuenta.getRetiros());
                        resumenCta.setDepositos(cuenta.getDepositos());
                        return resumenCta;
                    }).collect(Collectors.toList());
                })
                .toList();


        List<ResumenCuentaDiaria> flats =
                resumenCtaDiaria.stream()
                        .flatMap(List::stream)
                        .toList();

        Map<String, List<ResumenCuentaDiaria>> listasPorCuenta = flats.stream()
                .collect(groupingBy(e->e.getNumeroCuenta()));

        Set<String> sets=listasPorCuenta.keySet();
        List<Map<String, List<ResumenCuentaDiaria>>> listaCuentasPorDia=sets.stream().map(n->{
            Map<String, List<ResumenCuentaDiaria>> listasP=new HashMap<>();
            List<ResumenCuentaDiaria> movDiaCuenta = listasPorCuenta.get(n);
            List<ResumenCuentaDiaria> list= this.calcularSaldoPordia(movDiaCuenta);
            listasP.put(n,list);
            return listasP;
        }).toList();

        reporteSaldoDiario.setResumenCtasBancariasDiario(listaCuentasPorDia);
        log.info(" >>>> "+listaCuentasPorDia.toString());

        return reporteSaldoDiario;
    }


    public ReporteSaldoDiario calcularSaldosCreditosDiarios(ReporteSaldoDiario reporteSaldoDiario){

        List<ResumenDiario> resumenDiarioLst= reporteSaldoDiario.getResumenDiario();

        List<List<ResumenCreditoDiario>> resumenCreditoDiario = resumenDiarioLst
                .stream()
                .map( resumen->{
                    List<ResumenCreditoDiario> lista = resumen.getCreditos()
                            .stream()
                            .map(credito->{
                                ResumenCreditoDiario resumenCta=new ResumenCreditoDiario();
                                resumenCta.setDia(resumen.getDiaMes()); // dia
                                resumenCta.setNumeroCredito(credito.getNumeroCredito());
                                resumenCta.setConsumos(credito.getConsumos());
                                resumenCta.setPagos(credito.getPagos());
                                return resumenCta;
                            }).toList();
                    return lista;
                })
                .toList();


        List<ResumenCreditoDiario> flats =
                resumenCreditoDiario.stream()
                        .flatMap(List::stream)
                        .toList();

        Map<String, List<ResumenCreditoDiario>> listasPorCuenta = flats.stream()
                .collect(groupingBy(e->e.getNumeroCredito()));

        Set<String> sets=listasPorCuenta.keySet();
        List<Map<String, List<ResumenCreditoDiario>>> listaCuentasPorDia=sets.stream().map(n->{
            Map<String, List<ResumenCreditoDiario>> listasP=new HashMap<>();
            List<ResumenCreditoDiario> movDiaCuenta = listasPorCuenta.get(n);
            List<ResumenCreditoDiario> list= this.calcularCreditoSaldoPordia(movDiaCuenta);
            listasP.put(n,list);
            return listasP;
        }).toList();

        reporteSaldoDiario.setResumenProductosCreditosDiario (listaCuentasPorDia);
        log.info(" >>>> "+listaCuentasPorDia.toString());

        return reporteSaldoDiario;
    }


    public List<ResumenCuentaDiaria> calcularSaldoPordia(List<ResumenCuentaDiaria> lista){
        Integer numeroDiasMes=Util.getNumeroDiasMesActual();
        List<ResumenCuentaDiaria> collect = IntStream.rangeClosed(0, lista.size()-1)
                .mapToObj(i -> {
                    Double saldoAnterior=0.00;
                    if (i>0) {
                        ResumenCuentaDiaria previo = lista.get(i - 1);
                        saldoAnterior=previo.getSaldo();
                    } else {
                        saldoAnterior=0.00;
                    }

                    ResumenCuentaDiaria resum  = lista.get(i);
                    resum.setSaldo(saldoAnterior+resum.getDepositos()-resum.getRetiros());
                    resum.setPromedioDiario(resum.getSaldo()>0?resum.getSaldo()/numeroDiasMes:0.00);
                    return resum;
                })
                .toList();

        return collect;
    }

    public List<ResumenCreditoDiario> calcularCreditoSaldoPordia(List<ResumenCreditoDiario> lista){
        Integer numeroDiasMes=Util.getNumeroDiasMesActual();
        List<ResumenCreditoDiario> collect = IntStream.rangeClosed(0, lista.size()-1)
                .mapToObj(i -> {
                    Double saldoAnterior=0.00;
                    if (i>0) {
                        ResumenCreditoDiario previo = lista.get(i - 1);
                        saldoAnterior=previo.getSaldo();
                    } else {
                        saldoAnterior=0.00;
                    }
                    ResumenCreditoDiario resum  = lista.get(i);
                    resum.setSaldo(saldoAnterior+resum.getPagos() -resum.getConsumos());
                    resum.setPromedioDiario(resum.getSaldo()>0?resum.getSaldo()/numeroDiasMes:0.00);
                    return resum;
                })
                .toList();

        return collect;
    }


    public Mono<List<Pago>> getPagos(String numerCredito){
        FechasBean fechas = Util.getObtenerFechasInicioFinMes();
        return clientApiPagos.getPagosNumeroCreditoPorFecha(numerCredito,fechas.getFechaInicialT(),fechas.getFechaFinT()).collectList();

    }

    public Mono<List<CargoConsumo>> getConsumos(String numeroCredito){
        FechasBean fechas = Util.getObtenerFechasInicioFinMes();
        return clientApiConsumos.getConsumosNumeroCreditoPorFecha(numeroCredito,fechas.getFechaInicialT(),fechas.getFechaFinT()).collectList();
    }

    public Mono<List<OperacionCta>> getOperaciones(String numeroCuenta){
        FechasBean fechas = Util.getObtenerFechasInicioFinMes();
        return clientApiOperaciones.getOperacionesPorNumeroCuentaPorFecha(numeroCuenta,fechas.getFechaInicialT(),fechas.getFechaFinT()).collectList();
    }




}

