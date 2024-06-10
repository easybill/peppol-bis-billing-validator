package io.github.easybill.Dtos;

import com.helger.schematron.svrl.jaxb.FailedAssert;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ValidationResult {

    @NonNull
    private final SchematronOutputType report;

    @NonNull
    private final String xmlReport;

    @NonNull
    private final List<FailedAssert> errors;

    public ValidationResult(
        @NonNull SchematronOutputType report,
        @NonNull String xmlReport,
        @NonNull List<FailedAssert> errors
    ) {
        this.report = report.clone();
        this.xmlReport = xmlReport;
        this.errors = errors.stream().toList();
    }

    public boolean isValid() {
        return (long) errors.size() == 0;
    }

    public @NonNull SchematronOutputType getReport() {
        return report.clone();
    }

    public @NonNull String getXmlReport() {
        return xmlReport;
    }

    public @NonNull List<FailedAssert> getErrors() {
        return errors.stream().toList();
    }
}
