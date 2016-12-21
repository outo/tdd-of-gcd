package com.outoonline.gcdquiz;

import com.outoonline.gcdquiz.inputprovision.InputProvider;
import com.outoonline.gcdquiz.inputprovision.UnableToProvideInputsException;
import com.outoonline.gcdquiz.solutionsubmission.SolutionSubmitter;
import com.outoonline.gcdquiz.solutionsubmission.UnableToSubmitSolutionException;
import com.outoonline.gcdquiz.solver.QuizSolver;
import com.outoonline.gcdquiz.solver.UnableToSolveException;

import java.util.Map;

/**
 *
 */
public abstract class AbstractQuizSolvingApp {

    private final InputProvider inputProvider;
    private final QuizSolver quizSolver;
    private final SolutionSubmitter solutionSubmitter;

    AbstractQuizSolvingApp(InputProvider inputProvider, QuizSolver quizSolver, SolutionSubmitter solutionSubmitter) {
        this.inputProvider = inputProvider;
        this.quizSolver = quizSolver;
        this.solutionSubmitter = solutionSubmitter;
    }

    private Map<String, String> getInputs() throws UnableToProvideInputsException {
        return inputProvider.getInputs();
    }

    private Map<String, String> solve(Map<String, String> inputs) throws UnableToSolveException {
        return quizSolver.solve(inputs);
    }

    void submitSolution(Map<String, String> outputs) throws UnableToSubmitSolutionException {
        solutionSubmitter.submitSolution(outputs);
    }

    public void solve() throws UnableToProvideInputsException, UnableToSolveException, UnableToSubmitSolutionException {
        Map<String, String> inputs = getInputs();
        Map<String, String> outputs = solve(inputs);
        submitSolution(outputs);
    }
}
