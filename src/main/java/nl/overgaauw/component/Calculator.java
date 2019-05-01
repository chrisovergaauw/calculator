package nl.overgaauw.component;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static nl.overgaauw.service.ArithmeticService.performSimpleCalculation;

/**
 * The Calculator class keeps track of the calculator state and it is the bridge between
 * the user input (provided via Controller classes) and
 * the Arithmetic service (which does the actual computation)
 */
public class Calculator {


    public SimpleStringProperty calculationDisplay;
    public SimpleBooleanProperty allClearState;

    private String rightOperand;
    private String leftOperand;
    private char operator;

    private boolean leftOperandIsCurrent;
    private boolean negateNextNum;

    public Calculator() {
        resetCalculator();
    }

    /**
     * performs a calculation with the current values of leftOperand, rightOperand and operator.
     *
     * @return the result of the computation, or <code>null</code> if an Arithmetic Exception occurred internally
     */
    public BigDecimal performCalculation() {
        BigDecimal result;

        try {
            result = performSimpleCalculation(getLeftOperand(), operator, getRightOperand());
            leftOperand = result.toPlainString();
            result = result.setScale(15, RoundingMode.HALF_UP);
            result = result.stripTrailingZeros();
            calculationDisplay.set(result.toPlainString());
        } catch (ArithmeticException e) {
            leftOperand = "Not a Number";
            result = null;
            calculationDisplay.set(leftOperand);
        }
        return result;
    }

    public void handleOperatorInput(char operator) {
        if (!rightOperand.isEmpty()) {
            // calculate sub-result
            performCalculation();
        }
        this.operator = operator;
        leftOperandIsCurrent = false;
        rightOperand = "";
    }

    public void handleMinusInput() {
        if (leftOperandIsCurrent) {
            handleOperatorInput('-');
        } else {
            negateNextNum = true;
        }
    }

    public void handleNumInput(int i) {
        if (negateNextNum) {
            i *= -1;
            negateNextNum = false;
        }

        addIntToCurrentOperand(i);
        allClearState.setValue(false);
    }

    public void handleDotInput() {
        if (leftOperandIsCurrent) {
            leftOperand += leftOperand.isEmpty() ? "0." : ".";
            calculationDisplay.set(leftOperand);
        } else {
            rightOperand += rightOperand.isEmpty() ? "0." : ".";
            calculationDisplay.set(rightOperand);
        }
    }

    private void addIntToCurrentOperand(int i) {
        if (leftOperandIsCurrent) {
            leftOperand += i;
            leftOperand = clean(leftOperand);
            calculationDisplay.set(leftOperand);
        } else {
            rightOperand += i;
            rightOperand = clean(rightOperand);
            calculationDisplay.set(rightOperand);
        }
    }

    /**
     * Negates the value of the currently active operand.
     */
    public void negateCurrentOperand() {
        if (leftOperandIsCurrent) {
            leftOperand = leftOperand.startsWith("-") ? leftOperand.substring(1) : "-" + leftOperand;
            calculationDisplay.set(leftOperand);
        } else {
            rightOperand = rightOperand.startsWith("-") ? rightOperand.stripLeading() : "-" + rightOperand;
            calculationDisplay.set(rightOperand);
        }
    }

    /**
     * Resets the current Operand when {@link Calculator#allClearState}'s value is false,
     * otherwise resets entire calculator.
     */
    public void clearAndReturnAllClearState() {
        if (allClearState.getValue()) {
            resetCalculator();
        } else {
            resetCurrentOperand();
        }
    }

    /**
     * Sets the calculator to the initial state.
     */
    private void resetCurrentOperand() {
        if (leftOperandIsCurrent) {
            leftOperand = "0";
        } else {
            rightOperand = "";
        }
        allClearState.setValue(true);
        negateNextNum = false;

        calculationDisplay.set("0");
    }


    private void resetCalculator() {
        leftOperand = "0";
        rightOperand = "";
        leftOperandIsCurrent = true;

        this.calculationDisplay = new SimpleStringProperty(leftOperand);
        this.allClearState = new SimpleBooleanProperty(false);
    }

    /**
     * Strips trailing dots and leading zeros.
     *
     * @param operand a String representation of a number
     * @return the String representation without trailing dots and leading zeros.
     */
    private String clean(String operand) {
        return operand
                .replaceAll("\\.+$", "") // strip trailing dot
                .replaceAll("^0+(?!$)", "");  // strip leading zeros
    }

    /**
     * @return the {@code BigDecimal} representing the leftOperand, or <code>null</code>
     * if {@code leftOperand} is not a valid
     * representation of a {@code BigDecimal}.
     */
    private BigDecimal getLeftOperand() {
        return getBDOperand(leftOperand);
    }

    /**
     * @return the {@code BigDecimal} representing the rightOperand, or <code>null</code>
     * if {@code rightOperand} is not a valid
     * representation of a {@code BigDecimal}.
     */
    private BigDecimal getRightOperand() {
        return getBDOperand(rightOperand);
    }

    /**
     * Translates the string representation of a {@code BigDecimal}
     * into a {@code BigDecimal}.
     *
     * @param operandString a string representation of a {@code BigDecimal}
     * @return The {@code BigDecimal} representing the {@code operandString}, or <code>null</code>
     * if {@code operandString} is not a valid
     * representation of a {@code BigDecimal}.
     */
    private BigDecimal getBDOperand(String operandString) {
        try {
            return new BigDecimal(clean(operandString));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
