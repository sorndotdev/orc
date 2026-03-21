package dev.sorn.orc;

import dev.sorn.orc.api.JsonHttpClient;
import dev.sorn.orc.api.LlmClient;
import dev.sorn.orc.clients.DefaultJsonHttpClient;
import dev.sorn.orc.clients.OllamaClient;
import dev.sorn.orc.types.Id;

import java.net.URI;

public class OrcApplication {

    public static void main(String[] args) {
        final var jsonHttpClient = (JsonHttpClient) new DefaultJsonHttpClient();
        final var llmClient = (LlmClient) new OllamaClient(
            Id.of("codellama"),
            jsonHttpClient,
            URI.create("http://localhost:11434"));
        final var result = llmClient
            .complete("Explain SOLID principles")
            .fold(value -> value, err -> err, () -> "EMPTY");
        System.out.println("RESULT");
        System.out.println(result);
    }

}
