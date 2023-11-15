package com.jeongyuneo.blogsearchservice.global.util;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.stream.IntStream;

public class CustomSpringExpressionLanguageParser {

    private CustomSpringExpressionLanguageParser() {
        throw new UnsupportedOperationException();
    }

    public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        IntStream.range(0, parameterNames.length)
                .forEach(idx -> context.setVariable(parameterNames[idx], args[idx]));

        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
