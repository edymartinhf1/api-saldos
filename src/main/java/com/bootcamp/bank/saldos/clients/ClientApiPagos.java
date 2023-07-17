package com.bootcamp.bank.saldos.clients;

import com.bootcamp.bank.saldos.model.Pago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class ClientApiPagos {

    @Autowired
    @Qualifier("clientPagos")
    private WebClient webClient;

    /**
     * Permite obtener pagos por numero de credito del api-creditos-pagos
     * @param numeroCredito
     * @return
     */
    public Flux<Pago> getPagosNumeroCredito(String numeroCredito) {
        return webClient.get()
                .uri("/creditos/pago/numero-credito/" + numeroCredito)
                .retrieve()
                .bodyToFlux(Pago.class);
    }


    /**
     * Permite obtener pagos por numero de credito y fechas del api-creditos-pagos
     * @param numeroCredito
     * @param fechaInicial
     * @param fechaFinal
     * @return
     */
    public Flux<Pago> getPagosNumeroCreditoPorFecha(String numeroCredito, String fechaInicial, String fechaFinal) {
        return webClient.get()
                .uri("/creditos/pago//numerocredito/"+numeroCredito+"/fechainicio/"+fechaInicial+"/fechafin/"+fechaFinal)
                .retrieve()
                .bodyToFlux(Pago.class);
    }
}
