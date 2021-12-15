package org.csed332.project.team2.utils;

import org.csed332.project.team2.utils.WarningCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WarningConditionTest {

    private static WarningCondition w_incr, w_decr, w_morethan, w_lessthan, w_teta;
    private static double threshold = 50, teta = 5;

    @BeforeAll
    public static void initialize() {
        w_incr = new WarningCondition(WarningCondition.Mode.INCREASE);
        w_decr = new WarningCondition(WarningCondition.Mode.DECREASE);
        w_morethan = new WarningCondition(WarningCondition.Mode.MORE_THAN, threshold);
        w_lessthan = new WarningCondition(WarningCondition.Mode.LESS_THAN, threshold);
        w_teta = new WarningCondition(WarningCondition.Mode.INCREASE);
        w_teta.setTeta(5);
    }

    /// increase
    @Test
    public void increasePositiveTest() {
        Assertions.assertTrue(w_incr.shouldWarn(0, 1));
    }

    @Test
    public void increaseNegativeTest() {
        Assertions.assertFalse(w_incr.shouldWarn(1, 0));
        Assertions.assertFalse(w_incr.shouldWarn(0, 0));
    }

    /// decrease
    @Test
    public void decreasePositiveTest() {
        Assertions.assertTrue(w_decr.shouldWarn(1, 0));
    }

    @Test
    public void decreaseNegativeTest() {
        Assertions.assertFalse(w_decr.shouldWarn(0, 1));
        Assertions.assertFalse(w_decr.shouldWarn(0, 0));
    }

    /// increase and decrease with teta
    @Test
    public void tetaPositiveTest() {
        Assertions.assertTrue(w_teta.shouldWarn(0, teta + 1));
    }

    @Test
    public void tetaNegativeTest() {
        Assertions.assertFalse(w_teta.shouldWarn(0, teta));
        Assertions.assertFalse(w_teta.shouldWarn(0, 0));
        Assertions.assertFalse(w_teta.shouldWarn(0, teta - 1));
        Assertions.assertFalse(w_teta.shouldWarn(0, -teta));
    }


    /// more than
    @Test
    public void moreThanPositiveTest() {
        Assertions.assertTrue(w_morethan.shouldWarn(threshold - 1, threshold + 1));
        Assertions.assertTrue(w_morethan.shouldWarn(0, threshold));
        Assertions.assertTrue(w_morethan.shouldWarn(threshold, threshold));
        Assertions.assertTrue(w_morethan.shouldWarn(threshold + 1, threshold));
    }

    @Test
    public void moreThanNegativeTest() {
        Assertions.assertFalse(w_morethan.shouldWarn(threshold + 1, threshold - 1));
        Assertions.assertFalse(w_morethan.shouldWarn(threshold, 0));
        Assertions.assertFalse(w_morethan.shouldWarn(0, 0));
    }

    /// less than
    @Test
    public void lessThanPositiveTest() {
        Assertions.assertTrue(w_lessthan.shouldWarn(threshold + 1, threshold - 1));
        Assertions.assertTrue(w_lessthan.shouldWarn(threshold + 1, threshold));
        Assertions.assertTrue(w_lessthan.shouldWarn(threshold, threshold));
        Assertions.assertTrue(w_lessthan.shouldWarn(threshold - 1, threshold));
    }

    @Test
    public void lessThanNegativeTest() {
        Assertions.assertFalse(w_lessthan.shouldWarn(threshold - 1, threshold + 1));
        Assertions.assertFalse(w_lessthan.shouldWarn(0, threshold + 1));
        Assertions.assertFalse(w_lessthan.shouldWarn(100, 100));
    }
}
