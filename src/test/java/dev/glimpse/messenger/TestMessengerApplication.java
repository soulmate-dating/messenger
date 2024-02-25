package dev.glimpse.messenger;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestMessengerApplication {

    @Bean
    public CqlSession cqlSession(CassandraContainer<?> cassandraContainer) {
        return CqlSession.builder()
                .addContactPoint(cassandraContainer.getContactPoint())
                .withLocalDatacenter(cassandraContainer.getLocalDatacenter())
                .withKeyspace("messenger")
                .build();
    }

    @Bean
    @ServiceConnection
    CassandraContainer<?> cassandraContainer() {
        return new CassandraContainer<>(DockerImageName.parse("cassandra:latest"))
                .withInitScript("schema.cql")
                .withExposedPorts(9042);
    }


    public static void main(String[] args) {
        SpringApplication.from(MessengerApplication::main)
                .with(TestMessengerApplication.class)
                .run(args);
    }

}
