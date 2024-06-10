package io.github.easybill.Services;

import com.helger.commons.io.ByteArrayWrapper;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.schematron.sch.SchematronResourceSCH;
import com.helger.schematron.svrl.SVRLMarshaller;
import com.helger.schematron.svrl.jaxb.FailedAssert;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import io.github.easybill.Contracts.IValidationService;
import io.github.easybill.Dtos.ValidationResult;
import jakarta.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class ValidationService implements IValidationService {

    private final SchematronResourceSCH schematron;

    ValidationService() {
        schematron =
            new SchematronResourceSCH(
                new ClassPathResource("PEPPOL-BIS-Billing-3.0.sch")
            );

        if (!schematron.isValidSchematron()) {
            throw new RuntimeException("Schematron validation failed");
        }
    }

    @Override
    public @NonNull ValidationResult validateXml(@NonNull String xml)
        throws Exception {
        var report = schematron.applySchematronValidationToSVRL(
            new ByteArrayWrapper(xml.getBytes(StandardCharsets.UTF_8), false)
        );

        if (report == null) {
            throw new Exception("validation failed. the SVRL output is null");
        }

        String reportXML = new SVRLMarshaller().getAsString(report);

        if (reportXML == null) {
            throw new Exception("validation output is null");
        }

        return new ValidationResult(
            report,
            reportXML,
            this.getFailedAssertList(report)
        );
    }

    @Override
    public boolean isLoadedSchematronValid() {
        return schematron.isValidSchematron();
    }

    private List<FailedAssert> getFailedAssertList(
        @NonNull SchematronOutputType outputType
    ) {
        return outputType
            .getActivePatternAndFiredRuleAndFailedAssert()
            .stream()
            .filter(element -> element instanceof FailedAssert)
            .map(element -> (FailedAssert) element)
            .toList();
    }
}
