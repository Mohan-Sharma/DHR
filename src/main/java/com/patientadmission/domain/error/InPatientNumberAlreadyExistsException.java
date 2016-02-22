package com.patientadmission.domain.error;

import org.nthdimenzion.ddd.infrastructure.exception.ErrorDetails;
import org.nthdimenzion.ddd.infrastructure.exception.IBaseException;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 21/4/13
 * Time: 4:09 PM
 */
public class InPatientNumberAlreadyExistsException extends RuntimeException implements IBaseException {

    private final ErrorDetails errorDetails;

    public InPatientNumberAlreadyExistsException(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    @Override
    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    @Override
    public String toString() {
        return errorDetails.toString();
    }
}
