package com.bootcamp.bank.saldos.clients;

import com.bootcamp.bank.saldos.model.Cuenta;
import com.bootcamp.bank.saldos.model.TarjetaDebito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ClientApiCuentas {
    @Autowired
    @Qualifier("clientCuentas")
    private WebClient webClient;

    /**
     * Permite obtener cuentas por id cliente del api-cuentas
     * @param idCliente
     * @return
     */
    public Flux<Cuenta> getCuentas(String idCliente) {
        return webClient.get()
                .uri("/cuentas/cliente/" + idCliente)
                .retrieve()
                .bodyToFlux(Cuenta.class);
    }


    public Mono<TarjetaDebito> getTarjetaDebitoPorNumero(String numeroTarjetaDebito) {
        return webClient.get()
                .uri("/tarjeta/debito/numero/"+ numeroTarjetaDebito)
                .retrieve()
                .bodyToMono(TarjetaDebito.class);
    }

    public Mono<Cuenta> getCuentaPorNumero(String numeroCuenta) {
        return webClient.get()
                .uri("/cuentas/numero-cuenta/" + numeroCuenta)
                .retrieve()
                .bodyToMono(Cuenta.class);
    }

}

