package nl.overgaauw.service;

import nl.overgaauw.component.Calculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    Calculator calculator = new Calculator();

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
    public void testNegativeSecondOperandInAddition() {
        performAndCheckSimpleCalculation("1+-1", "0");
    }

    @Test
    public void testNegativeSecondOperandInMultiplication() {
        performAndCheckSimpleCalculation("1*-1", "-1");
    }

    @Test
    public void testNegativeFirstOperandInAddition() {
        performAndCheckSimpleCalculation("-1+1", "0");
    }

    @Test
    public void testNegativeFirstOperandInMultiplication() {
        performAndCheckSimpleCalculation("-1*1", "-1");
    }

    @Test
    public void testReversabilityOfOperation() {
        performAndCheckSimpleCalculation("1/3*3", "1");
    }

    @Test
    public void testMultipleCalculations() {
        performAndCheckSimpleCalculation("1+1=1+1", "2");
    }

    @Test
    public void testMinusFollowedByOperator() {
        performAndCheckSimpleCalculation("1-+1", "2");
    }

    private void performAndCheckSimpleCalculation(String calculation, String expectedString) {
        BigDecimal expected = new BigDecimal(expectedString);
        char[] inputSequence = calculation.toCharArray();

        for (char symbol : inputSequence) {
            if (Character.isDigit(symbol)) {
                calculator.handleNumInput(Character.getNumericValue(symbol));
            } else if (symbol == '-') {
                calculator.handleMinusInput();
            } else if (symbol == '.') {
                calculator.handleDotInput();
            } else if (symbol == '=') {
                calculator.handleEqualsInput();
            } else {
                calculator.handleOperatorInput(symbol);
            }
        }

        BigDecimal actualAnswer = calculator.performCalculation();
        if (expected.compareTo(actualAnswer) != 0) {
            // We already know the answer is not what we expect, the next statement is for readability of the error msg
            assertEquals(expected, actualAnswer);
        }
    }
}
