package com.bootcamp.bank.saldos.clients;

import com.bootcamp.bank.saldos.model.Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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
}

