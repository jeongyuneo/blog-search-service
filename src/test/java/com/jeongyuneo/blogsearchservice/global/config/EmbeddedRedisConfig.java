package com.jeongyuneo.blogsearchservice.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@TestConfiguration
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int port;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws Exception {
        redisServer = RedisServer.builder()
                .port(isRunning(executeGrepProcessCommand(port)) ? findAvailablePort() : port)
                .build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    public int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            if (!isRunning(executeGrepProcessCommand(port))) {
                return port;
            }
        }
        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String[] shell = {"/bin/sh", "-c", "netstat -nat | grep LISTEN | grep " + port};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) throws IOException {
        StringBuilder pidInfo = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        }
        return !pidInfo.toString().isEmpty();
    }
}
