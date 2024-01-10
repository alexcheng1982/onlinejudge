package io.vividcode.onlinejudge.javarunner.model;

import java.util.List;

public record JavaEntryPoint(String javaClassName, String methodName,
                             List<Class<?>> parameters, Class<?> returnType) {

}
