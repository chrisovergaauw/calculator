package nl.overgaauw.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ArithmeticServiceTest {

    @Test
    public void testDivideByZero(){
        BigDecimal leftOperand = new BigDecimal(10);
        BigDecimal rightOperand = new BigDecimal(0);
        char operator = '/';

        assertThrows(ArithmeticException.class, () -> {
             ArithmeticService.performSimpleCalculation(leftOperand, operator, rightOperand);
        });
    }

}

