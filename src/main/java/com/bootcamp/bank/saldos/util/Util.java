package com.bootcamp.bank.saldos.util;

import com.bootcamp.bank.saldos.model.FechasBean;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Component
@Log4j2
public class Util {

    private Util() {
    }

    private static SecureRandom random = new SecureRandom();
    public static int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static String getCurrentDateAsString(String format) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(formatter);
    }

    public static Date getCurrentDate() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate fecha = LocalDate.now();
        return Date.from(fecha.atStartOfDay(defaultZoneId).toInstant());
    }

    public static Date getDatefromString(String fecha) {
        Date fechaConvert = null;
        try {
            String formatDate = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
            fechaConvert = sdf.parse(fecha);
        } catch (ParseException ex) {
            log.error(ex.getMessage(), ex);
        }
        return fechaConvert;
    }


    public static FechasBean getObtenerFechasInicioFinMes() {
        FechasBean fecha = new FechasBean();
        LocalDateTime fecInicial = LocalDateTime.now().with(
                TemporalAdjusters.firstDayOfMonth());
        LocalDateTime fecFinal = LocalDateTime.now().with(
                TemporalAdjusters.lastDayOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:00");
        fecha.setFechaInicial(fecInicial);
        fecha.setFechaFinal(fecFinal);
        fecha.setFechaInicialT(formatter.format(fecInicial));
        fecha.setFechaFinT(formatter2.format(fecFinal));
        return fecha;
    }

    public static Integer getNumeroDiasMesActual(){
        return LocalDate.now().lengthOfMonth();
    }

}
