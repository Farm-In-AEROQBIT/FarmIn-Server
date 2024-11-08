package com.farmin.farminserver.common.exception;

import lombok.Getter;
import com.farmin.farminserver.common.error.ErrorCodeIfs;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionItf {
    private final ErrorCodeIfs errorCodeIfs;
    private final String errorDescription;

    public ApiException(ErrorCodeIfs errorCodeItf){
        super(errorCodeItf.getDescription());
        this.errorCodeIfs = errorCodeItf;
        this.errorDescription = errorCodeItf.getDescription();
    }
    public ApiException(ErrorCodeIfs errorCodeItf,String errorDescription){
        super(errorDescription);
        this.errorCodeIfs = errorCodeItf;
        this.errorDescription = errorDescription;
    }
    public ApiException(ErrorCodeIfs errorCodeItf,Throwable throwable){
        super(throwable);
        this.errorCodeIfs = errorCodeItf;
        this.errorDescription = errorCodeItf.getDescription();
    }
    public ApiException(ErrorCodeIfs errorCodeItf,Throwable throwable,String errorDescription){
        super(throwable);
        this.errorCodeIfs = errorCodeItf;
        this.errorDescription = errorDescription;
    }
}