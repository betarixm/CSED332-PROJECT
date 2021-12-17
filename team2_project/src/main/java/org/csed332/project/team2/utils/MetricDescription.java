package org.csed332.project.team2.utils;

import org.csed332.project.team2.metrics.Metric;

import java.util.Map;

/**
 * Defines description for each metric.
 */
public class MetricDescription {

    final static String NO_DESC = "No description available.";

    final static Map<Metric.Type, String> descriptions = Map.of(
            Metric.Type.LINES_OF_CODE, "This is the number of lines of code of the whole project.",
            Metric.Type.HALSTEAD, "The vocabulary is the number of unique operators and operands. \n" +
                    "The volume depends on the vocabulary and the total number of all operators and operands.\n" +
                    "The difficulty measure is related to the difficulty of the program to write or understand.\n" +
                    "The effort is calculated by multiplying volume and difficulty and can be translated to actual coding time.",
            Metric.Type.CYCLOMATIC, "This is the number of lines of code of the whole project."
    );

    /**
     * Get description for given type of metric.
     *
     * @param type the type
     * @return the description
     */
    public static String get(Metric.Type type) {
        return descriptions.getOrDefault(type, NO_DESC);
    }
}
