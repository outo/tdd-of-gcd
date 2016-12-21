package com.outoonline.gcdquiz.solutionsubmission;

import java.util.Map;

/**
 *
 */
public interface SolutionSubmitter {
    void submitSolution(Map<String, String> solution) throws UnableToSubmitSolutionException;
}
