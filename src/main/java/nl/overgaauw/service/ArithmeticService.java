package nl.overgaauw.service;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Performs basic computations with provided operands and operator
 */
public class ArithmeticService {

    public static BigDecimal performSimpleCalculation(BigDecimal leftOperand, char operator, BigDecimal rightOperand) throws ArithmeticException {
        BigDecimal result = new BigDecimal(0);

        if (leftOperand == null) {
            throw new ArithmeticException("Operands cannot be null!");
        } else if (rightOperand == null) {
            return leftOperand;
        }

        switch (operator) {

            case '/':
                    result = leftOperand.divide(rightOperand, 16, RoundingMode.HALF_UP);
                break;
            case '*':
                result = leftOperand.multiply(rightOperand);
                break;
            case '-':
                result = leftOperand.subtract(rightOperand);
                break;
            case '+':
                result = leftOperand.add(rightOperand);
                break;
        }

        System.out.println(String.format("%s %s %s = %s", leftOperand, operator, rightOperand, result));

        return result;
    }

}
