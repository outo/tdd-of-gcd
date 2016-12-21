package com.outoonline.gcdquiz;

import com.outoonline.gcdquiz.inputprovision.WebPageScraperInputProvider;
import com.outoonline.gcdquiz.solutionsubmission.UnableToSubmitSolutionException;
import com.outoonline.gcdquiz.solutionsubmission.WebFormSolutionSubmitter;
import com.outoonline.gcdquiz.solver.QuizSolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
  *
  */
@RunWith(MockitoJUnitRunner.class)
public class WebPageScrapeAndSubmitQuizSolvingAppTest {

    private WebPageScrapeAndSubmitQuizSolvingApp webPageScrapeAndSubmitQuizSolvingApp;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private WebPageScraperInputProvider webPageScraperInputProviderMock;

    @Mock
    private QuizSolver quizSolverMock;

    @Mock
    private WebFormSolutionSubmitter webFormSolutionSubmitterMock;

    private final static Map<String, String> EXPECTED_COOKIES = new HashMap<String, String>() {
        {
            put("cookie-name", "cookie-value");
        }
    };

    @Before
    public void setUp() throws Exception {
        webPageScrapeAndSubmitQuizSolvingApp = new WebPageScrapeAndSubmitQuizSolvingApp(
                webPageScraperInputProviderMock,
                quizSolverMock,
                webFormSolutionSubmitterMock
        );
    }

    @Test
    public void shouldSubmitSolutionWithCookiesFromInitialScrape() throws Exception {
        when(webPageScraperInputProviderMock.getCookiesToBeSet()).thenReturn(EXPECTED_COOKIES);

        webPageScrapeAndSubmitQuizSolvingApp.solve();

        verify(webFormSolutionSubmitterMock).submitSolution(eq(EXPECTED_COOKIES), anyMapOf(String.class, String.class));
    }

    @Test
    public void shouldIndicateProblemDuringSubmission() throws Exception {
        doThrow(new UnableToSubmitSolutionException()).when(webFormSolutionSubmitterMock).submitSolution(anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        expectedException.expect(UnableToSubmitSolutionException.class);
        webPageScrapeAndSubmitQuizSolvingApp.submitSolution(new HashMap<>());
    }

    @Test
    public void shouldIndicateProblemDuringRetrievingCookiesToBeSet() throws Exception {
        doThrow(new IOException()).when(webPageScraperInputProviderMock).getCookiesToBeSet();
        expectedException.expect(UnableToSubmitSolutionException.class);
        webPageScrapeAndSubmitQuizSolvingApp.submitSolution(new HashMap<>());
    }

}