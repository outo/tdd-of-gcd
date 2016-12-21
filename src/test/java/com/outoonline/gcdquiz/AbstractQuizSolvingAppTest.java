package com.outoonline.gcdquiz;

import com.outoonline.gcdquiz.inputprovision.InputProvider;
import com.outoonline.gcdquiz.inputprovision.UnableToProvideInputsException;
import com.outoonline.gcdquiz.solutionsubmission.SolutionSubmitter;
import com.outoonline.gcdquiz.solutionsubmission.UnableToSubmitSolutionException;
import com.outoonline.gcdquiz.solver.QuizSolver;
import com.outoonline.gcdquiz.solver.UnableToSolveException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
  *
  */
@RunWith(MockitoJUnitRunner.class)
public class AbstractQuizSolvingAppTest {

    private AbstractQuizSolvingApp abstractQuizSolvingApp;

    @Mock
    private InputProvider inputProviderMock;

    @Mock
    private QuizSolver quizSolverMock;

    @Mock
    private SolutionSubmitter solutionSubmitterMock;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private static final Map<String, String> EXPECTED_INPUTS = new HashMap<String, String>() {{
        put("number-1-ref", "number-1-val");
        put("number-2-ref", "number-2-val");
    }};
    private static final Map<String, String> EXPECTED_OUTPUTS = new HashMap<String, String>() {{
        put("solution-part-1", "solution-val-1");
    }};

    @Before
    public void setUp() throws Exception {
        abstractQuizSolvingApp = new AbstractQuizSolvingApp(
                inputProviderMock,
                quizSolverMock,
                solutionSubmitterMock
        ){};
    }

    @Test
    public void shouldAttemptToGatherInputs() throws Exception {
        //execute
        abstractQuizSolvingApp.solve();

        //verify
        verify(inputProviderMock).getInputs();
    }

    @Test
    public void shouldIndicateInabilityToGatherInputs() throws Exception {
        //mock
        when(inputProviderMock.getInputs()).thenThrow(new UnableToProvideInputsException());

        //verify
        expectedException.expect(UnableToProvideInputsException.class);

        //execute
        abstractQuizSolvingApp.solve();
    }

    @Test
    public void shouldAttemptSolvingWithAllInputs() throws Exception {
        //mock
        when(inputProviderMock.getInputs()).thenReturn(EXPECTED_INPUTS);

        //execute
        abstractQuizSolvingApp.solve();

        //verify
        verify(quizSolverMock).solve(EXPECTED_INPUTS);
    }

    @Test
    public void shouldIndicateInabilityToSolve() throws Exception {
        //mock
        when(inputProviderMock.getInputs()).thenReturn(EXPECTED_INPUTS);
        when(quizSolverMock.solve(any())).thenThrow(new UnableToSolveException());

        //verify
        expectedException.expect(UnableToSolveException.class);

        //execute
        abstractQuizSolvingApp.solve();
    }

    @Test
    public void shouldAttemptToSubmitSolution() throws Exception {
        //mock
        when(inputProviderMock.getInputs()).thenReturn(EXPECTED_INPUTS);
        when(quizSolverMock.solve(any())).thenReturn(EXPECTED_OUTPUTS);

        //execute
        abstractQuizSolvingApp.solve();

        //verify
        verify(solutionSubmitterMock).submitSolution(EXPECTED_OUTPUTS);
    }

    @Test
    public void shouldIndicateInabilityToSubmitSolution() throws Exception {
        //mock
        when(inputProviderMock.getInputs()).thenReturn(EXPECTED_INPUTS);
        when(quizSolverMock.solve(any())).thenReturn(EXPECTED_OUTPUTS);
        doThrow(new UnableToSubmitSolutionException()).when(solutionSubmitterMock).submitSolution(any());

        //verify
        expectedException.expect(UnableToSubmitSolutionException.class);

        //execute
        abstractQuizSolvingApp.solve();
    }

}