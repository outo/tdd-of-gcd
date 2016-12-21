package com.outoonline.gcdquiz.solver.algorithm;

/**
 *
 */
public class EuclideanAlgorithmGreatestCommonDivisorFinder implements GreatestCommonDivisorFinder {
    public int findGreatestCommonDivisor(int numberA, int numberB) {
        int bigger = Math.max(numberA, numberB);
        int smaller = Math.min(numberA, numberB);

        int remainder;
        while (true) {
            remainder = Math.floorMod(bigger, smaller);
            bigger = smaller;

            if (remainder == 0) {
                return smaller;
            }
            smaller = remainder;

        }
    }
}
