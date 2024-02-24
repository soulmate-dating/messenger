package dev.glimpse.messenger;

import com.datastax.oss.driver.api.core.CqlSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.delegate.CassandraDatabaseDelegate;
import org.testcontainers.delegate.DatabaseDelegate;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TestMessengerApplication.class)
public abstract class AbstractIntegrationTest {

    private final Resource schema = new ClassPathResource("schema.cql");

    @Autowired
    private CqlSession cqlSession;

    @Autowired
    private CassandraContainer<?> cassandraContainer;

    @AfterEach
    public void setUp() throws IOException, ScriptException {
        cqlSession.execute("DROP KEYSPACE IF EXISTS messenger");
        executeSchemaCreation();
    }

    private void executeSchemaCreation() throws IOException, ScriptException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("schema.cql");
        if (resource == null) {
            throw new ScriptUtils.ScriptLoadException(
                    "Could not load classpath init script Resource not found."
            );
        }
        String cql = IOUtils.toString(resource, StandardCharsets.UTF_8);
        DatabaseDelegate databaseDelegate = new CassandraDatabaseDelegate(cassandraContainer);
        ScriptUtils.executeDatabaseScript(databaseDelegate, "schema.cql", cql);
    }

}
