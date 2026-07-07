package br.com.medibridge.medi_bridge.catalog.infra.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class ProblemDetailFactory {

    private ProblemDetailFactory() {
    }

    public static ProblemDetail create(
            HttpStatus status,
            String type,
            String title,
            String detail,
            Map<String, Object> properties
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create(type));
        problem.setTitle(title);

        if (properties != null && !properties.isEmpty()) {
            properties.forEach(problem::setProperty);
        }

        problem.setProperty("timestamp", OffsetDateTime.now(ZoneOffset.UTC));

        return problem;
    }
}
