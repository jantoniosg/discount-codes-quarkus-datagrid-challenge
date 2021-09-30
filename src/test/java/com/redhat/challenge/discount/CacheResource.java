package com.redhat.challenge.discount;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/**
 * Use this class to use test containers and make the unit test pass with Infinispan 12 which is RDGH 8.2
 * You don't need to change this class, just uncomment the @QuarkusTestResource annotation and make
 * sure docker is running locally for testcontainers
 */
public class CacheResource implements QuarkusTestResourceLifecycleManager {

    private static GenericContainer INFINISPAN = null;
    private static final Integer INFINISPAN_PORT = 11222;

    @Override
    public Map<String, String> start() {

        INFINISPAN =
                new GenericContainer("infinispan/server:12.1.7.Final")
                        .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Infinispan Server.*started in.*\\s"))
                        .withStartupTimeout(Duration.ofMillis(20000))
                .withEnv("USER","admin")
                .withEnv("PASS","password");

        INFINISPAN.start();
        final String hosts = INFINISPAN.getContainerIpAddress() + ":" + INFINISPAN.getMappedPort(INFINISPAN_PORT);

        return Collections.singletonMap("quarkus.infinispan-client.server-list", hosts);
    }

    @Override
    public void stop() {
        INFINISPAN.stop();
    }
}
