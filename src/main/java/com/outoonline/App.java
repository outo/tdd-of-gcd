package com.outoonline;

import com.outoonline.gcdquiz.WebPageScrapeAndSubmitQuizSolvingApp;
import com.outoonline.gcdquiz.inputprovision.JSoupWebPageScraperInputProvider;
import com.outoonline.gcdquiz.inputprovision.UnableToProvideInputsException;
import com.outoonline.gcdquiz.solutionsubmission.JSoupWebFormSolutionSubmitter;
import com.outoonline.gcdquiz.solutionsubmission.UnableToSubmitSolutionException;
import com.outoonline.gcdquiz.solver.GreatestCommonDivisorQuizSolver;
import com.outoonline.gcdquiz.solver.UnableToSolveException;
import com.outoonline.gcdquiz.solver.algorithm.EuclideanAlgorithmGreatestCommonDivisorFinder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 */
class App {

    public static final String URL_TO_WEB_PAGE_WITH_INPUTS = "http://www.url-of-your-site.com/quiz/dev";
    public static final String URL_TO_WEB_PAGE_WHICH_ACCEPTS_SOLUTION = "http://www.url-of-your-site.com/submit";

    public static void main(String[] args) throws IOException, UnableToSubmitSolutionException, UnableToSolveException, UnableToProvideInputsException {

        long startedNanoTime = System.nanoTime();

        HashMap<String, String> queries = new HashMap<String, String>() {{
            put("first-number", "#quiz>p>strong:eq(0)");
            put("second-number", "#quiz>p>strong:eq(1)");
        }};

        JSoupWebPageScraperInputProvider webPageScraperInputProvider = new JSoupWebPageScraperInputProvider(URL_TO_WEB_PAGE_WITH_INPUTS, queries);
        GreatestCommonDivisorQuizSolver quizSolver = new GreatestCommonDivisorQuizSolver(
                Arrays.asList(
                        "first-number",
                        "second-number"
                ),
                "divisor",
                new EuclideanAlgorithmGreatestCommonDivisorFinder()
        );
        JSoupWebFormSolutionSubmitter webPageSolutionSubmitter = new JSoupWebFormSolutionSubmitter(URL_TO_WEB_PAGE_WHICH_ACCEPTS_SOLUTION);

        WebPageScrapeAndSubmitQuizSolvingApp app = new WebPageScrapeAndSubmitQuizSolvingApp(
                webPageScraperInputProvider,
                quizSolver,
                webPageSolutionSubmitter
        );

        app.solve();

        System.out.println(webPageSolutionSubmitter.getResponseBody());
        System.out.printf("Solved after %dms\n", (System.nanoTime() - startedNanoTime) / 1000000);
    }
}
