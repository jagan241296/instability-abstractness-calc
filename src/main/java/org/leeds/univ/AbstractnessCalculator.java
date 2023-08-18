package org.leeds.univ;

import java.lang.reflect.Modifier;
import java.util.Map;

public class AbstractnessCalculator {

    public float calculateAbstractness(String fullyQualifiedPackageName, Map<String, Class<?>> classNameToInstanceMap) {
        if (classNameToInstanceMap.size() == 0) {
            return -1;
        }

        float nAbs = 0;
        float nTotal = 0;
        for (var entry : classNameToInstanceMap.entrySet()) {
            if (entry.getKey().startsWith(fullyQualifiedPackageName)) {
                var cls = entry.getValue();
                nAbs += checkForAbstractClass(cls);
                nTotal += 1;
            }
        }
        return nAbs / nTotal;
    }

    private int checkForAbstractClass(Class<?> cls) {
        return Modifier.isAbstract(cls.getModifiers()) || cls.isInterface() ? 1 : 0;
    }
}
