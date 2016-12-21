package com.outoonline.gcdquiz;

import com.outoonline.gcdquiz.inputprovision.WebPageScraperInputProvider;
import com.outoonline.gcdquiz.solutionsubmission.UnableToSubmitSolutionException;
import com.outoonline.gcdquiz.solutionsubmission.WebFormSolutionSubmitter;
import com.outoonline.gcdquiz.solver.QuizSolver;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class WebPageScrapeAndSubmitQuizSolvingApp extends AbstractQuizSolvingApp {

    private final WebPageScraperInputProvider webPageScraperInputProvider;
    private final WebFormSolutionSubmitter webFormSolutionSubmitter;

    public WebPageScrapeAndSubmitQuizSolvingApp(WebPageScraperInputProvider webPageScraperInputProvider, QuizSolver quizSolver, WebFormSolutionSubmitter webFormSolutionSubmitter) {
        super(webPageScraperInputProvider, quizSolver, webFormSolutionSubmitter);
        this.webPageScraperInputProvider = webPageScraperInputProvider;
        this.webFormSolutionSubmitter = webFormSolutionSubmitter;
    }

    @Override
    void submitSolution(Map<String, String> outputs) throws UnableToSubmitSolutionException {
        try {
            Map<String, String> cookiesToBeSet = webPageScraperInputProvider.getCookiesToBeSet();
            webFormSolutionSubmitter.submitSolution(cookiesToBeSet, outputs);
        } catch (IOException e) {
            throw new UnableToSubmitSolutionException(e);
        }
    }
}
