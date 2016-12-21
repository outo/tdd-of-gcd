package com.outoonline.gcdquiz.solver;

import com.outoonline.gcdquiz.solver.algorithm.GreatestCommonDivisorFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
  *
  */
@RunWith(MockitoJUnitRunner.class)
public class GreatestCommonDivisorQuizSolverTest {

    private QuizSolver quizSolver;

    private static final String KEY_WITH_NAME_1 = "key-with-name-1";
    private static final String KEY_WITH_NAME_2 = "key-with-name-2";
    private static final String OUTPUT_KEY = "divisor";

    @Mock
    private GreatestCommonDivisorFinder greatestCommonDivisorFinderMock;

    @Test
    public void shouldInvokeSolverImplementation() throws Exception {
        quizSolver = new GreatestCommonDivisorQuizSolver(
                asList(KEY_WITH_NAME_1, KEY_WITH_NAME_2),
                OUTPUT_KEY,
                greatestCommonDivisorFinderMock
        );
        quizSolver.solve(new HashMap<String, String>() {{
            put(KEY_WITH_NAME_1, "1");
            put(KEY_WITH_NAME_2, "2");
        }});

        verify(greatestCommonDivisorFinderMock).findGreatestCommonDivisor(eq(1), eq(2));
    }

    @Test
    public void shouldUseGivenInputKeys() throws Exception {
        quizSolver = new GreatestCommonDivisorQuizSolver(
                asList(KEY_WITH_NAME_1, KEY_WITH_NAME_2),
                OUTPUT_KEY,
                greatestCommonDivisorFinderMock);

        quizSolver.solve(new HashMap<String, String>() {{
            put("to-be-ignored-1", "1");
            put(KEY_WITH_NAME_1, "2");
            put(KEY_WITH_NAME_2, "3");
            put("to-be-ignored-2", "4");
        }});

        verify(greatestCommonDivisorFinderMock).findGreatestCommonDivisor(eq(2), eq(3));
    }

    @Test
    public void shouldUseSpecifiedOutputKey() throws Exception {
        quizSolver = new GreatestCommonDivisorQuizSolver(
                asList(KEY_WITH_NAME_1, KEY_WITH_NAME_2),
                OUTPUT_KEY,
                greatestCommonDivisorFinderMock);
    }
}