package dev.sorn.orc.api;

import dev.sorn.orc.types.Id;

public interface LlmClient {

    Id modelId();

    Result<String> complete(String prompt);

}
