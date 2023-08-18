package org.leeds.univ;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InstabilityCalculator {

    public float calculateInstability(String fullyQualifiedPackageName, Map<String, Class<?>> classNameToInstanceMap) {
        Map<Class<?>, Float> perClassFanInMap = new HashMap<>();
        Map<Class<?>, Float> perClassFanOutMap = new HashMap<>();

        var eligibleClasses = getEligibleClassesForInstability(classNameToInstanceMap);

        // loop thru each class
        for (var entry : classNameToInstanceMap.entrySet()) {
            var cls = entry.getValue();
            Arrays.stream(cls.getDeclaredFields())
                    .map(this::getFieldType)
                    .filter(i -> eligibleClasses.get(i) != null)
                    .filter(i -> !isDependencyWithinPackage(fullyQualifiedPackageName, i, cls))
                    .forEach(field -> {
                        perClassFanOutMap.put(cls, perClassFanOutMap.getOrDefault(cls, 0F) + 1);
                        perClassFanInMap.put(field, perClassFanInMap.getOrDefault(field, 0F) + 1);
                    });
        }

        return calculateInstabilityFactor(fullyQualifiedPackageName, perClassFanInMap, perClassFanOutMap, classNameToInstanceMap);
    }

    private boolean isDependencyWithinPackage(String fullyQualifiedPackageName, Class<?> fieldClass, Class<?> callingClass) {
        return fieldClass.getName().startsWith(fullyQualifiedPackageName) && callingClass.getName().startsWith(fullyQualifiedPackageName);
    }

    private Class<?> getFieldType(Field field) {
        var typeToCheck = field.getType();
        if (Iterable.class.isAssignableFrom(field.getType()) || Array.class.isAssignableFrom(field.getType())) {
            var genericType = (ParameterizedType) field.getGenericType();
            typeToCheck = (Class<?>) genericType.getActualTypeArguments()[0];
        }
        return typeToCheck;
    }

    private float calculateInstabilityFactor(String fullyQualifiedPackageName,
                                             Map<Class<?>, Float> perClassFanInMap,
                                             Map<Class<?>, Float> perClassFanOutMap,
                                             Map<String, Class<?>> classNameToInstanceMap) {
        var fanOutTotal = 0F;
        var fanInTotal = 0F;
        for (var entry : classNameToInstanceMap.entrySet()) {
            if (entry.getKey().startsWith(fullyQualifiedPackageName)) {
                fanOutTotal += perClassFanOutMap.getOrDefault(entry.getValue(), 0F);
                fanInTotal += perClassFanInMap.getOrDefault(entry.getValue(), 0F);
            }
        }
        var denominator = fanOutTotal + fanInTotal;
        if (denominator == 0)
            return 0;
        return fanOutTotal / denominator;
    }

    private Map<Class<?>, Long> getEligibleClassesForInstability(Map<String, Class<?>> classNameToInstanceMap) {
        return classNameToInstanceMap.values()
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
