package io.vividcode.onlinejudge.javarunner.model;

import java.util.List;

public record Problem(String problemId,
                      JavaEntryPoint entryPoint,
                      List<TestCase> testCases) {

}
