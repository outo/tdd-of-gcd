package com.outoonline.gcdquiz.solver;

import com.outoonline.gcdquiz.solver.algorithm.GreatestCommonDivisorFinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 *
 */
public class GreatestCommonDivisorQuizSolver implements QuizSolver {

    private List<String> inputKeys;
    private final String outputKey;
    private final GreatestCommonDivisorFinder greatestCommonDivisorFinder;

    public GreatestCommonDivisorQuizSolver(List<String> orderedInputKeys, String outputKey, GreatestCommonDivisorFinder greatestCommonDivisorFinder) {
        this.inputKeys = orderedInputKeys;
        this.outputKey = outputKey;
        this.greatestCommonDivisorFinder = greatestCommonDivisorFinder;
    }

    @Override
    public Map<String, String> solve(Map<String, String> inputs) throws UnableToSolveException {
        int greatestCommonDenominator = greatestCommonDivisorFinder.findGreatestCommonDivisor(
                parseInt(inputs.get(inputKeys.get(0))),
                parseInt(inputs.get(inputKeys.get(1))));

        return new HashMap<String, String>() {{
            put(outputKey, String.valueOf(greatestCommonDenominator));
        }};
    }
}
