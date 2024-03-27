package dev.glimpse.messenger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.delegate.CassandraDatabaseDelegate;
import org.testcontainers.delegate.DatabaseDelegate;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import javax.script.ScriptException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@DisabledInAotMode
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TestMessengerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    @Autowired
    private CqlSession cqlSession;

    @Autowired
    private CassandraContainer<?> cassandraContainer;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @AfterEach
    public void setUp() throws IOException, ScriptException {
        cqlSession.execute("DROP KEYSPACE IF EXISTS messenger");
        executeSchemaCreation();
    }

    public class StompClientWrapper<T> {

        private final WebSocketStompClient stompClient;

        public StompClientWrapper() {
            WebSocketClient socketClient = new StandardWebSocketClient();
            SockJsClient sockJsClient = new SockJsClient(List.of(new WebSocketTransport(socketClient)));
            WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter(AbstractIntegrationTest.this.objectMapper));

            this.stompClient = stompClient;
        }

        public CompletableFuture<T> listen(String destinationPath, Class<T> expectedType) {
            CompletableFuture<T> completableFuture = new CompletableFuture<>();
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<StompSession> sessionReference = new AtomicReference<>();
            stompClient.connectAsync(String.format("ws://localhost:%d/ws", port), new StompSessionHandlerAdapter() {
                @Override
                public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
                    sessionReference.set(session);
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                completableFuture.completeExceptionally(e);
                return completableFuture;
            }

            StompSession session = sessionReference.get();
            if (session != null) {
                session.subscribe(destinationPath, new StompFrameHandler() {
                    @NonNull
                    @Override
                    public Type getPayloadType(@NonNull StompHeaders headers) {
                        return expectedType;
                    }

                    @Override
                    public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                        completableFuture.complete(expectedType.cast(payload));
                    }
                });
            } else {
                completableFuture.completeExceptionally(new RuntimeException("Stomp session is null"));
            }

            return completableFuture;
        }
    }

    private void executeSchemaCreation() throws IOException, ScriptException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("schema.cql");
        String cql = IOUtils.toString(Objects.requireNonNull(resource), StandardCharsets.UTF_8);
        DatabaseDelegate databaseDelegate = new CassandraDatabaseDelegate(cassandraContainer);
        ScriptUtils.executeDatabaseScript(databaseDelegate, "schema.cql", cql);
    }

}
