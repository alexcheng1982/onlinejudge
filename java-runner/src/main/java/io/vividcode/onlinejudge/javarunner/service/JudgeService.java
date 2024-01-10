package io.vividcode.onlinejudge.javarunner.service;

import io.vividcode.onlinejudge.javarunner.model.JudgeInput;
import io.vividcode.onlinejudge.javarunner.model.TestCaseMatchStrategy.DefaultTestCaseMatchStrategy;
import io.vividcode.onlinejudge.javarunner.model.result.CodeRuntimeError;
import io.vividcode.onlinejudge.javarunner.model.result.CompilationFailed;
import io.vividcode.onlinejudge.javarunner.model.result.Results;
import io.vividcode.onlinejudge.javarunner.model.result.Success;
import io.vividcode.onlinejudge.javarunner.model.result.TestCaseFailed;
import io.vividcode.onlinejudge.javarunner.service.classloader.SolutionRunnerService;
import io.vividcode.onlinejudge.javarunner.service.compilation.CompilationService;
import io.vividcode.onlinejudge.javarunner.service.converter.ConverterRegistry;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JudgeService {

  @Autowired
  CompilationService compilationService;

  @Autowired
  SolutionRunnerService solutionRunnerService;

  @Autowired
  ConverterRegistry converterRegistry;

  private static final Logger LOGGER = LoggerFactory.getLogger(JudgeService.class);

  public Results judge(JudgeInput input) {
    try {
      var compilationResult = compilationService.compile(input.userInput().source());
      if (!compilationResult.success()) {
        return new Results(
            List.of(new CompilationFailed(compilationResult.errors()))
        );
      }
      var entryPoint = input.problem().entryPoint();
      var parameters = entryPoint.parameters().toArray(new Class<?>[0]);
      var results = input.problem().testCases().stream().map(testCase -> {
        try {
          if (!Objects.equals(entryPoint.parameters().size(),
              testCase.inputArgs().size())) {
            return new CodeRuntimeError(
                new IllegalArgumentException("Invalid test case input"));
          }
          int argsCount = entryPoint.parameters().size();
          Object[] args = new Object[argsCount];
          for (int i = 0; i < argsCount; i++) {
            args[i] = converterRegistry.tryConvertFromString(
                entryPoint.parameters().get(i), testCase.inputArgs().get(i)).orElse(null);
          }
          var result = solutionRunnerService.runTestCase(compilationResult.packageName(),
              entryPoint.javaClassName(), entryPoint.methodName(), parameters, args);
          var strategy =
              testCase.testCaseMatchStrategy() != null ? testCase.testCaseMatchStrategy()
                  : new DefaultTestCaseMatchStrategy(converterRegistry,
                      entryPoint.returnType());
          if (strategy.match(testCase.expectedOutput(), result)) {
            return Success.INSTANCE;
          }
          return new TestCaseFailed(testCase, result);
        } catch (Exception e) {
          LOGGER.error("Failed to judge test case {}", testCase, e);
          return new CodeRuntimeError(e);
        }
      }).toList();
      return new Results(results);
    } catch (Exception e) {
      LOGGER.error("Failed to judge input {}", input, e);
      return new Results(List.of(new CodeRuntimeError(e)));
    }
  }
}
