package nl.overgaauw.service;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Performs basic computations with provided operands and operator (* / - +)
 * operands and operators are to be provided sequentially.
 * see handleXInput methods
 */
public class ArithmeticService {

    public SimpleStringProperty equation;
    public SimpleBooleanProperty allClear;


    private String leftOperand;
    private String rightOperand;
    private char operator;
    private boolean leftOperandIsCurrent = true;
    private boolean negateNextNum = false;

    public ArithmeticService() {
        this.leftOperand = "";
        this.rightOperand = "";
        this.equation = new SimpleStringProperty("0");
        this.allClear = new SimpleBooleanProperty(false);
    }

    public void handleOperatorInput(char operator) {
        if (!rightOperand.isEmpty()) {
            // calculate sub-result (example usecase : 1 + 1 + 1)
            performCalculation();
        }
        this.operator = operator;
        rightOperand = "";
        leftOperandIsCurrent = false;
    }

    public void handleMinusInput() {
        if (leftOperand.isEmpty()) {
            // - pressed first results in 0 - X
            leftOperand = "0";
            this.operator = '-';
            leftOperandIsCurrent = false;
        } else {
            if (leftOperandIsCurrent) {
                handleOperatorInput('-');
            } else {
                negateNextNum = true;
            }
        }
    }

    public void handleNumInput(int i) {
        if (negateNextNum) {
            i *= -1;
            negateNextNum = false;
        }

        if (leftOperandIsCurrent) {
            leftOperand += i;
            equation.set(leftOperand);
        } else {
            rightOperand += i;
            equation.set(rightOperand);
        }
        allClear.setValue(false);
    }

    public void handleDotInput() {
        if (leftOperandIsCurrent) {
            leftOperand += leftOperand.isEmpty() ? "0." : ".";
            equation.set(leftOperand);
        } else {
            rightOperand += rightOperand.isEmpty() ? "0." : ".";
            equation.set(rightOperand);
        }
    }

    public void invertCurrentOperand() {
        if (leftOperandIsCurrent) {
            leftOperand = leftOperand.startsWith("-") ? leftOperand.substring(1) : "-" + leftOperand;
            equation.set(leftOperand);
        } else {
            rightOperand = rightOperand.startsWith("-") ? rightOperand.stripLeading() : "-" + rightOperand;
            equation.set(rightOperand);
        }
    }


    public BigDecimal performCalculation() {
        BigDecimal result = new BigDecimal(leftOperand);

        if (!leftOperandIsCurrent && !rightOperand.isEmpty()) {
            BigDecimal firstOperand = new BigDecimal(clean(leftOperand));
            BigDecimal secondOperand = new BigDecimal(rightOperand);
            switch (operator) {

                case '/':
                    result = firstOperand.divide(secondOperand, 16, RoundingMode.HALF_UP);
                    break;
                case '*':
                    result = firstOperand.multiply(secondOperand);
                    break;
                case '-':
                    result = firstOperand.subtract(secondOperand);
                    break;
                case '+':
                    result = firstOperand.add(secondOperand);
                    break;
            }

            printCalculationToCMD(result);
            rightOperand = "";
            leftOperandIsCurrent = true;
        }

        result = result.setScale(16, RoundingMode.FLOOR);
        result = result.stripTrailingZeros();
        equation.set(result.toPlainString());
        leftOperand = String.valueOf(result);

        return result;
    }

    private String clean(String operand) {
        return operand.replaceAll("\\.+$", "");
    }

    private void printCalculationToCMD(BigDecimal result) {
        System.out.println(String.format("%s %s %s = %s", leftOperand, operator, rightOperand, result.toPlainString()));
    }

    public void clearAndReturnAllClearState() {
        if (allClear.getValue()) {
            leftOperand = "";
            rightOperand = "";
            leftOperandIsCurrent = true;
            allClear.setValue(false);
        } else {
            if (leftOperandIsCurrent) {
                leftOperand = "";
            } else {
                rightOperand = "";
            }
            allClear.setValue(true);
            negateNextNum = false;
        }
        equation.set("0");
    }
}
