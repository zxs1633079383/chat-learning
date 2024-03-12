package com.zlc.chat.chat.common.common.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--9:11
 * 3. 目的:
 */

public class SpElTUtils {
    private static  final ExpressionParser PARSER = new SpelExpressionParser();
    private static  final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();



    public static String getMethodKey(Method method){
        return method.getDeclaringClass() + "#" + method.getName();
    }

    public static String parSeSpEL(Method method, Object[] args, String key) {
        String[] params = Optional.ofNullable(PARAMETER_NAME_DISCOVERER.getParameterNames(method)).orElse(new String[]{});
        EvaluationContext context = new StandardEvaluationContext();  //el需要解析的上下文
        for (int i = 0; i < params.length ; i++) {
            context.setVariable(params[i],args[i]);
        }
        Expression expression = PARSER.parseExpression(key);
        return expression.getValue(context,String.class);

    }
}
