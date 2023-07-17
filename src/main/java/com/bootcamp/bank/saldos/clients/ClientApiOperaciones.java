package com.bootcamp.bank.saldos.clients;

import com.bootcamp.bank.saldos.model.OperacionCta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class ClientApiOperaciones {
    @Autowired
    @Qualifier("clientOperaciones")
    private WebClient webClient;

    /**
     * Permite obtener operaciones de cuenta por numero de cuenta  y tipoOperacion del api-cuentas-operaciones
     *
     * @param numeroCuenta
     * @return
     */
    public Flux<OperacionCta> getOperacionesPorNumeroCuenta(String numeroCuenta, String tipoOperacion) {
        return webClient.get()
                .uri("/operaciones/cuentas/cuenta/" + numeroCuenta + "/tipo/" + tipoOperacion)
                .retrieve()
                .bodyToFlux(OperacionCta.class);
    }


    /**
     * Permite obtener operaciones de cuenta por numero de cuenta y fecha del api-cuentas-operaciones
     * @param numeroCuenta
     * @param fechaInicial
     * @param fechaFinal
     * @return
     */
    public Flux<OperacionCta> getOperacionesPorNumeroCuentaPorFecha(String numeroCuenta, String fechaInicial, String fechaFinal) {
        return webClient.get()
                .uri("/operaciones/cuentas/numerocuenta/"+numeroCuenta+"/fechainicio/"+fechaInicial+"/fechafin/"+fechaFinal)
                .retrieve()
                .bodyToFlux(OperacionCta.class);
    }
}
