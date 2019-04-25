package nl.overgaauw.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;


public class ArithmeticServiceTest {

    ArithmeticService arithmeticService = new ArithmeticService();

    @Test
    public void testSimpleAddition() {
        performAndCheckSimpleCalculation("1+1", "2");
    }

    @Test
    public void testSimpleSubtraction() {
        performAndCheckSimpleCalculation("1-1", "0");
    }

    @Test
    public void testSimpleDivision() {
        performAndCheckSimpleCalculation("10/1", "10");
    }

    @Test
    public void testSimpleMultiplication() {
        performAndCheckSimpleCalculation("10*1", "10");
    }

    @Test
    public void testNegativeSecondOperand() {
        performAndCheckSimpleCalculation("1+-1", "0");
    }

    @Test
    public void testNegativeSecondOperandMultiplication() {
        performAndCheckSimpleCalculation("1*-1", "-1");
    }

    @Test
    public void testNegativeFirstOperand() {
        performAndCheckSimpleCalculation("-1+1", "0");
    }
    @Test
    public void testNegativeFirstOperandMultiplication() {
        performAndCheckSimpleCalculation("-1*1", "-1");
    }

    @Test
    public void testReverseOperation() {
        performAndCheckSimpleCalculation("1/3*3", "1");
    }

    private void performAndCheckSimpleCalculation(String calculation, String expectedString) {
        BigDecimal expected = new BigDecimal(expectedString);
        char[] inputSequence = calculation.toCharArray();

        for (char symbol : inputSequence) {
            if (Character.isDigit(symbol)) {
                arithmeticService.handleNumInput(Character.getNumericValue(symbol));
            } else if (symbol == '-') {
                arithmeticService.handleMinusInput();
            }else if (symbol == '.') {
                arithmeticService.handleDotInput();
            } else {
                arithmeticService.handleOperatorInput(symbol);
            }
        }

        BigDecimal actualAnswer = arithmeticService.performCalculation();
        if (expected.compareTo(actualAnswer) != 0) {
            // We already know the answer is not what we expect, the next statement is for readability of the error msg
            assertEquals(expected, actualAnswer);
        }
    }
}

