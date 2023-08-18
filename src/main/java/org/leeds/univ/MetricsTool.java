package org.leeds.univ;

import java.io.IOException;
import java.util.Map;

public class MetricsTool {

    private final ClassPathLoader classPathLoader;
    private final AbstractnessCalculator abstractnessCalculator;

    private final InstabilityCalculator instabilityCalculator;

    MetricsTool() {
        this.classPathLoader = new ClassPathLoader();
        this.abstractnessCalculator = new AbstractnessCalculator();
        this.instabilityCalculator = new InstabilityCalculator();
    }

    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            return;
        }

        String absolutePath = args[0];
        String packageName = args[1];

        if (absolutePath.isBlank() || packageName.isBlank()) {
            return;
        }

        MetricsTool metricsTool = new MetricsTool();
        var instanceMap = metricsTool.constructClassesToLoadToClasspath(absolutePath);

        // calculate Abstractness
        metricsTool.calculateAbstractness(packageName, instanceMap);
        System.out.println();

        // Calculate Instability for given package
        metricsTool.calculateInstability(packageName, instanceMap);
    }

    private void calculateInstability(String packageName, Map<String, Class<?>> instanceMap) {
        var instabilityFactor = instabilityCalculator.calculateInstability(packageName, instanceMap);
        System.out.printf("Instability factor for package : " + packageName + " = %.2f", instabilityFactor);
    }

    private void calculateAbstractness(String packageName, Map<String, Class<?>> instanceMap) {
        var abstractness = abstractnessCalculator.calculateAbstractness(packageName, instanceMap);
        System.out.printf("Abstractness of package " + packageName + " = %.2f", abstractness);
    }

    private Map<String, Class<?>> constructClassesToLoadToClasspath(String absolutePath) throws IOException {
        return classPathLoader.constructClassesToLoadToClasspath(absolutePath);
    }
}