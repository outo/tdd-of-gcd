package com.outoonline.gcdquiz.solver;

import java.util.Map;

/**
 *
 */
public interface QuizSolver {

    Map<String, String> solve(Map<String, String> inputs) throws UnableToSolveException;

}
