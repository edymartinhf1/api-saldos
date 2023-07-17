package com.bootcamp.bank;

import com.bootcamp.bank.saldos.service.SaldoResumenService;
import com.bootcamp.bank.saldos.service.SaldoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ApiSaldosApplicationTests {
	@Autowired
	private SaldoResumenService saldoResumenService;
	@Autowired
	private SaldoService saldoService;
	@Test
	void contextLoads() {
		assertThat(saldoResumenService).isNotNull();
		assertThat(saldoService).isNotNull();
	}

}
