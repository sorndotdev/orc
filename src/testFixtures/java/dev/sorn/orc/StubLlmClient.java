package dev.sorn.orc;

import dev.sorn.orc.api.LlmClient;
import dev.sorn.orc.api.Result;
import dev.sorn.orc.api.Result.Success;
import dev.sorn.orc.types.Id;

// TODO: Implement stub BDD interface: GIVEN, WHEN, THEN
public class StubLlmClient implements LlmClient {

    @Override
    public Id modelId() {
        return Id.of("stub_llm_client");
    }

    @Override
    public Result<String> complete(String prompt) {
        return Success.of("Stub LLM response");
    }

}
