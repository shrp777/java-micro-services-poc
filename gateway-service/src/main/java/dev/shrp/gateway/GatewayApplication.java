package dev.shrp.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

// tag::code[]
@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    //doit être saisie avant la route /tasks
    // tag::route-locator[]
    @Bean
    public RouteLocator oneTask(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
        String httpUri = uriConfiguration.getHttpbin();
        return builder.routes()
                .route(p -> p
                .path("/tasks/{id}")
                .uri(httpUri))
                .build();
    }
    // end::route-locator[]

    // tag::route-locator[]
    @Bean
    public RouteLocator tasks(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
        String httpUri = uriConfiguration.getHttpbin();
        return builder.routes()
                .route(p -> p
                .path("/tasks")
                .uri(httpUri))
                .build();
    }
    // end::route-locator[]

    // tag::fallback[]
    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }
    // end::fallback[]

}

// tag::uri-configuration[]
@ConfigurationProperties
class UriConfiguration {

    @Value("${tasks_service}")
    private String tasks_service;

    //private String httpbin = "http://" + System.getenv("tasks_service") + ":8080";
    //
    // OU
    //
    private String httpbin = "http://" + tasks_service + ":8080";

     public String getHttpbin() {
        return httpbin;
    }

    public void setHttpbin(String httpbin) {
        //test
        this.httpbin = httpbin;
    }
}
// end::uri-configuration[]
// end::code[]
