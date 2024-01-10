package io.vividcode.onlinejudge.javarunner.model;

import java.util.List;

public record TestCase(List<String> inputArgs, String expectedOutput,
                       TestCaseMatchStrategy testCaseMatchStrategy) {

}
