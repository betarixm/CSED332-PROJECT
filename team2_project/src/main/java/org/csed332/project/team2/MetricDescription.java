package org.csed332.project.team2;

import org.csed332.project.team2.metrics.Metric;

import java.util.HashMap;
import java.util.Map;

public class MetricDescription {

    final static String NO_DESC = "No description available.";

    final static Map<Metric.Type, String> descriptions = Map.of(
            Metric.Type.LINES_OF_CODE, "This is the number of lines of code of the whole project."
    );

    static String get(Metric.Type type) {
        return descriptions.getOrDefault(type, NO_DESC);
    }
}
