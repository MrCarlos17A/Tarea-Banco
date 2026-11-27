package pe.demo.solicitudes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class AuditFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger("audit");
    @Value("${APP_VERSION:dev}")
    private String version;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String rid = req.getHeader("X-Request-ID");
        if (rid == null || rid.isBlank()) rid = UUID.randomUUID().toString();
        res.setHeader("X-Request-ID", rid);
        MDC.put("request_id", rid);
        long t = System.currentTimeMillis();
        try {
            chain.doFilter(req, res);
        } finally {
            log.info("{\"request_id\":\"{}\",\"method\":\"{}\",\"path\":\"{}\",\"status\":{},\"ms\":{},\"version\":\"{}\"}",
                    rid, req.getMethod(), req.getRequestURI(), res.getStatus(), System.currentTimeMillis() - t, version);
            MDC.clear();
        }
    }
}
