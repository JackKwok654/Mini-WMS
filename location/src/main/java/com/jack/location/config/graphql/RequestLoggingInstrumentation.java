package com.jack.location.config.graphql;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestLoggingInstrumentation extends SimpleInstrumentation {
    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        long startMillis = System.currentTimeMillis();
        var execution = parameters.getExecutionInput();

        return new SimpleInstrumentationContext<>() {
            @SneakyThrows
            @Override
            public void onCompleted(ExecutionResult executionResult, Throwable t) {
                long endMillis = System.currentTimeMillis();

                if (t != null) {
                    log.info("GraphQL {} failed: {}", execution, t.getMessage(), t);
                } else {
                    log.info("GraphQL {} completed in {}ms", execution, endMillis - startMillis);
                }
            }
        };
    }
}
