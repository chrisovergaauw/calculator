package nl.overgaauw.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class ArithmeticServiceTest {

    @Test
    public void testDivideByZero(){
        BigDecimal leftOperand = new BigDecimal(10);
        BigDecimal rightOperand = new BigDecimal(0);
        char operator = '/';

        BigDecimal result = ArithmeticService.performSimpleCalculation(leftOperand, operator, rightOperand);
        assertNull(result);
    }

}

