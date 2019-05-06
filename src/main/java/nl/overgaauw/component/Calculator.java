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
    private boolean resetOnNextNumInput;

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
            result = getFormattedResult(result);
            calculationDisplay.set(result.toPlainString());
        } catch (ArithmeticException e) {
            leftOperand = "Not a Number";
            result = null;
            calculationDisplay.set(leftOperand);
        }
        return result;
    }

    private BigDecimal getFormattedResult(BigDecimal result) {
        result = result.setScale(15, RoundingMode.HALF_UP);
        result = result.stripTrailingZeros();
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

        calculationDisplay.set("hopefully nobody ever sees this :-)");
        calculationDisplay.set(leftOperand); // little trickery to trigger ui/display blinks on operator input
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

        if (resetOnNextNumInput) {
            resetCalculator();
        }

        addIntToCurrentOperand(i);
        allClearState.setValue(false);
    }

    public void handleDotInput() {
        setCurrentOperandRaw(addDecimal(getCurrentOperandAsString()));
    }

    private String addDecimal(String operand) {
        if (operand.isEmpty()) {
            operand = "0.";
        } else if (!operand.contains(".")) {
            operand += ".";
        }
        return operand;
    }

    public void handleEqualsInput() {
        performCalculation();
        resetOnNextNumInput = true;
    }

    private void addIntToCurrentOperand(int i) {
        String currentOperand = getCurrentOperandAsString();
        currentOperand += i;
        setCurrentOperand(currentOperand);
    }

    /**
     * Negates the value of the currently active operand.
     */
    public void negateCurrentOperand() {
        String currentOperand = getCurrentOperandAsString();
        String negatedOperand = negateOperand(currentOperand);
        setCurrentOperand(negatedOperand);
    }

    private String negateOperand(String operand) {
        return operand.startsWith("-") ? operand.substring(1) : "-" + operand;
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
        resetOperandsAndBooleans();
        InitDisplayAndClearState();
    }

    private void InitDisplayAndClearState() {
        if (this.calculationDisplay == null) {
            this.calculationDisplay = new SimpleStringProperty(leftOperand);
            this.allClearState = new SimpleBooleanProperty(false);
        } else {
            this.calculationDisplay.setValue(leftOperand);
            this.allClearState.setValue(false);
        }
    }

    private void resetOperandsAndBooleans() {
        leftOperand = "0";
        rightOperand = "";
        leftOperandIsCurrent = true;
        resetOnNextNumInput = false;
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

    private BigDecimal getCurrentOperandAsBD() {
        return leftOperandIsCurrent ? getLeftOperand() : getRightOperand();
    }

    private String getCurrentOperandAsString() {
        return leftOperandIsCurrent ? leftOperand : rightOperand;
    }

    private void setCurrentOperand(String operand) {
        operand = clean(operand);
        setCurrentOperandRaw(operand);
    }

    private void setCurrentOperandRaw(String operand) {
        if (leftOperandIsCurrent) {
            this.leftOperand = operand;
        } else {
            this.rightOperand = operand;
        }

        this.calculationDisplay.setValue(operand);
    }

    private void setCurrentOperand(BigDecimal operand) {
        setCurrentOperand(getFormattedResult(operand).toPlainString());
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

    public void handlePercentageInput() {
        BigDecimal currentOperand = getCurrentOperandAsBD();
        setCurrentOperand(currentOperand.multiply(new BigDecimal(0.01)));
    }
}
