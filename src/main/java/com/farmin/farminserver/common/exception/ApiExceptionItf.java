package com.farmin.farminserver.common.exception;

import com.farmin.farminserver.common.error.ErrorCodeIfs;

public interface ApiExceptionItf{
    ErrorCodeIfs getErrorCodeIfs();
    String getErrorDescription();
}
