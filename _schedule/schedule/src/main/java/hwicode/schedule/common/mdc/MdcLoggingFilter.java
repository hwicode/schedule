package hwicode.schedule.common.mdc;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter implements Filter {

    public static final String QUERY_COUNT = "query_count";
    public static final String START_TIME = "start_time";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        postConstructMdc();
        chain.doFilter(request, response);
        preDestroyMdc();
    }

    private void postConstructMdc() {
        final UUID uuid = UUID.randomUUID();
        MDC.put("request_id", uuid.toString());
        MDC.put(QUERY_COUNT, String.valueOf(0));
        MDC.put(START_TIME, String.valueOf(System.currentTimeMillis()));
    }

    private void preDestroyMdc() {
        long start = Long.parseLong(MDC.get(START_TIME));
        long end = System.currentTimeMillis();
        log.info("number of queries executed : {}, total time spent : {}ms",
                MDC.get(QUERY_COUNT), end - start);

        MDC.clear();
    }
}
