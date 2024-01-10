package io.vividcode.onlinejudge.javarunner.model.result;

import io.vividcode.onlinejudge.javarunner.model.TestCase;

public record TestCaseFailed(TestCase testCase, Object actualResult) implements Result {

}
