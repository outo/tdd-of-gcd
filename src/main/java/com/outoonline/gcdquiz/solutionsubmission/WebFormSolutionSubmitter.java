package com.outoonline.gcdquiz.solutionsubmission;

import java.util.Map;

/**
 *
 */
public interface WebFormSolutionSubmitter extends SolutionSubmitter {

    String getResponseBody();

    void submitSolution(Map<String, String> cookies, Map<String, String> solution) throws UnableToSubmitSolutionException;

}
