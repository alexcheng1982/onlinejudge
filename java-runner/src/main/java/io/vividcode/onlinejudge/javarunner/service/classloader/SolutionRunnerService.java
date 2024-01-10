package io.vividcode.onlinejudge.javarunner.service.classloader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SolutionRunnerService {

  private final Path classes;
  private final URLClassLoader classLoader;

  private static final Logger LOGGER = LoggerFactory.getLogger(
      SolutionRunnerService.class);

  public SolutionRunnerService() throws IOException {
    classes = Files.createTempDirectory("classes_");
    classLoader = new URLClassLoader(new URL[]{classes.toUri().toURL()},
        ClassLoader.getSystemClassLoader());
    LOGGER.info("Solution classes output to {}", classes);
  }

  public Path getClasses() {
    return classes;
  }

  private Class<?> loadClass(String packageName, String javaClassName)
      throws ClassNotFoundException {
    String className = "%s.%s".formatted(packageName, javaClassName);
    return Class.forName(className, true, classLoader);
  }

  private Object createInstance(String packageName)
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<?> clazz = loadClass(packageName, "SolutionCreator");
    var method = clazz.getDeclaredMethod("create");
    return method.invoke(null);
  }

  public Object runTestCase(String packageName, String javaClassName, String methodName,
      Class<?>[] parameters, Object[] args)
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    var clazz = loadClass(packageName, javaClassName);
    var instance = createInstance(packageName);
    var method = clazz.getDeclaredMethod(methodName, parameters);
    method.setAccessible(true);
    return method.invoke(instance, args);
  }
}
