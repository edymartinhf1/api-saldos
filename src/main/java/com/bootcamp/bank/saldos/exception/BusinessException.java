package com.bootcamp.bank.saldos.exception;

/**
 * Clase Exception de Negocio
 */
public class BusinessException extends RuntimeException{
    private String errorCode;
    private String messageError;

    public BusinessException(String messageError) {
        super(messageError);
        this.messageError = messageError;
    }

    public BusinessException(String errorCode,String messageError) {
        super(messageError);
        this.errorCode =errorCode;
        this.messageError = messageError;
    }


}