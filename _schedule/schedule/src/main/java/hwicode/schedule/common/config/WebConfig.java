package hwicode.schedule.common.config;

import hwicode.schedule.common.login.LoginArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginArgumentResolver loginArgumentResolver;
    private final String serverDomain;

    public WebConfig(LoginArgumentResolver loginArgumentResolver,
                     @Value("${server.domain}") String serverDomain) {
        this.loginArgumentResolver = loginArgumentResolver;
        this.serverDomain = serverDomain;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(serverDomain)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.AUTHORIZATION);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginArgumentResolver);
    }
}
