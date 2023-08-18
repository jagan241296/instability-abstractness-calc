package org.leeds.univ;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ClassPathLoader {

    private final Map<String, URL> filePathMap;
    private final Map<String, Class<?>> classNameToInstanceMap;
    private URLClassLoader urlClassLoader;

    ClassPathLoader() {
        filePathMap = new HashMap<>();
        classNameToInstanceMap = new HashMap<>();
    }

    public Map<String, Class<?>> constructClassesToLoadToClasspath(String absolutePath) throws IOException {

        try (var fileStream = Files.walk(Paths.get(absolutePath))) {

            var fileUrlArray = fileStream
                    .filter(i -> i.getFileName().toString().endsWith(".class"))
                    .map(file -> addClassToMap(absolutePath, filePathMap, file))
                    .toArray(URL[]::new);
            loadUrlsToClassLoader(fileUrlArray);
            loadClassesToClasspath();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return getClassNameToInstanceMap();
    }

    private void loadClassesToClasspath() throws ClassNotFoundException {
        for (var entry : filePathMap.entrySet()) {
            var cls = Class.forName(entry.getKey(), true, urlClassLoader);
            getClassNameToInstanceMap().put(entry.getKey(), cls);
        }
    }

    private void loadUrlsToClassLoader(URL[] fileUrlArray) {
        urlClassLoader = new URLClassLoader(fileUrlArray, ClassLoader.getSystemClassLoader());
    }

    private static URL addClassToMap(String absolutePath, Map<String, URL> filePathMap, Path pathToFile) {
        String trimmedClassName = getFullyQualifiedClassName(absolutePath, pathToFile);

        // form URL
        try {
            var fileUrl = new URL("file:///" + absolutePath + "\\");
            filePathMap.put(trimmedClassName, fileUrl);
            return fileUrl;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFullyQualifiedClassName(String absolutePath, Path pathToFile) {
        var actualFilePath = pathToFile.toString().substring(absolutePath.length() + 1);
        var actualClassName = actualFilePath.split("\\.")[0];
        return actualClassName.replace("\\", ".");
    }

    public Map<String, Class<?>> getClassNameToInstanceMap() {
        return classNameToInstanceMap;
    }
}
