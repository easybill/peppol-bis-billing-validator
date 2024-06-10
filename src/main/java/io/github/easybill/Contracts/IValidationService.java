package io.github.easybill.Contracts;

import io.github.easybill.Dtos.ValidationResult;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface IValidationService {
    @NonNull
    ValidationResult validateXml(@NonNull String xml) throws Exception;

    boolean isLoadedSchematronValid();
}
