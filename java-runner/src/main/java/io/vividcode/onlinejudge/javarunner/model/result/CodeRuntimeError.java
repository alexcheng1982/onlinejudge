package io.vividcode.onlinejudge.javarunner.model.result;

public record CodeRuntimeError(Throwable throwable) implements Result {

}
