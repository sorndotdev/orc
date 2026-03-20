package dev.sorn.orc.api;

import dev.sorn.orc.types.Result;

public interface Tool<I, O> {

    Result<O> execute(I input);

}
