package com.outoonline.gcdquiz.solver.algorithm;

import com.outoonline.gcdquiz.solver.UnableToSolveException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
  *
  */
@RunWith(Parameterized.class)
public class EuclideanAlgorithmGreatestCommonDivisorFinderTest {

    private GreatestCommonDivisorFinder greatestCommonDivisorFinder;

    @Before
    public void setUp() throws Exception {
        greatestCommonDivisorFinder = new EuclideanAlgorithmGreatestCommonDivisorFinder();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"should return any of the numbers if both same", 5, 5, 5},
                {"should return 1 for two different prime numbers", 5, 7, 1},
                {"should return the lower number if the bigger one is multiple of the lower", 5, 25, 5},
                {"should return greatest common denominator for two different, non-prime numbers", 462, 1071, 21}
        });
    }

    private final String narrative;
    private final int numberA;
    private final int numberB;
    private final int expectedResult;

    public EuclideanAlgorithmGreatestCommonDivisorFinderTest(String narrative, int numberA, int numberB, int expectedResult) {
        this.narrative = narrative;
        this.numberA = numberA;
        this.numberB = numberB;
        this.expectedResult = expectedResult;
    }

    @Test
    public void test() throws UnableToSolveException {
        assertThat(narrative, greatestCommonDivisorFinder.findGreatestCommonDivisor(numberA, numberB), is(equalTo(expectedResult)));
    }


}