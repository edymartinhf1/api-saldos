package com.bootcamp.bank.saldos.clients;

import com.bootcamp.bank.saldos.model.CargoConsumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class ClientApiConsumos {
    @Autowired
    @Qualifier("clientConsumos")
    private WebClient webClient;


    /**
     * Permite obtener consumos por id cliente del api-creditos-consumos
     * @param idCliente
     * @return
     */
    public Flux<CargoConsumo> getConsumosPorId(String idCliente) {
        return webClient.get()
                .uri("/creditos/tarjetas/cargos/cliente/" + idCliente)
                .retrieve()
                .bodyToFlux(CargoConsumo.class);
    }


    /**
     * Permite obtener consumos por numero credito del api-creditos-consumos
     * @param numeroCredito
     * @return
     */
    public Flux<CargoConsumo> getConsumosNumeroCredito(String numeroCredito) {
        return webClient.get()
                .uri("/creditos/tarjetas/cargos/numero-credito/" + numeroCredito)
                .retrieve()
                .bodyToFlux(CargoConsumo.class);
    }

    /**
     * Permite obtener cosnsumos por numero credito y fecha del api-creditos-consumos
     * @param numeroCredito
     * @param fechaInicial
     * @param fechaFinal
     * @return
     */
    public Flux<CargoConsumo> getConsumosNumeroCreditoPorFecha(String numeroCredito,String fechaInicial,String fechaFinal) {
        return webClient.get()
                .uri("/creditos/tarjetas/cargos/numerocredito/"+numeroCredito+"/fechainicio/"+fechaInicial+"/fechafin/"+fechaFinal)
                .retrieve()
                .bodyToFlux(CargoConsumo.class);
    }

}

