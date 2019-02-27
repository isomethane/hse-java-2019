package ru.hse.inclass;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class StackCalculatorTest {
    @Test
    void testAdd() {
        @SuppressWarnings("unchecked")
        var mockList = (List<Integer>) mock(ArrayList.class);
        var calculator = new StackCalculator(mockList);
        when(mockList.get(anyInt())).thenReturn(2, 1, 5, 3, 8);
        when(mockList.size()).thenReturn(2, 2, 1, 1, 2, 2, 1, 1, 1);

        assertEquals(8, calculator.calculate("1 2 + 5 +")); // 1 + 2 + 5

        var inOrder = inOrder(mockList);
        inOrder.verify(mockList).add(1);
        inOrder.verify(mockList).add(2);
        inOrder.verify(mockList).add(3);
        inOrder.verify(mockList).add(5);
        inOrder.verify(mockList).add(8);

        inOrder = inOrder(mockList);
        inOrder.verify(mockList).get(1);
        inOrder.verify(mockList).get(0);
        inOrder.verify(mockList).get(1);
        inOrder.verify(mockList).get(0);
        inOrder.verify(mockList).get(0);

        inOrder = inOrder(mockList);
        inOrder.verify(mockList).remove(1);
        inOrder.verify(mockList).remove(0);
        inOrder.verify(mockList).remove(1);
        inOrder.verify(mockList).remove(0);
        inOrder.verify(mockList).remove(0);
    }


    @Test
    void testComplex() {
        @SuppressWarnings("unchecked")
        var mockList = (List<Integer>) mock(ArrayList.class);
        var calculator = new StackCalculator(mockList);
        when(mockList.get(anyInt())).thenReturn(1, 3, 2, 7, 4, 14, 3, 18, 6);
        when(mockList.size()).thenReturn(3, 3, 2, 2, 2, 2, 1, 1, 2, 2, 1, 1, 1);

        assertEquals(6, calculator.calculate("7 3 1 - * 4 + 3 /")); // (7 * (3 - 1) + 4) / 3

        var inOrder = inOrder(mockList);
        inOrder.verify(mockList).add(7);
        inOrder.verify(mockList).add(3);
        inOrder.verify(mockList).add(1);
        inOrder.verify(mockList).add(2);
        inOrder.verify(mockList).add(14);
        inOrder.verify(mockList).add(18);
        inOrder.verify(mockList).add(6);

        inOrder = inOrder(mockList);
        inOrder.verify(mockList).get(2);
        inOrder.verify(mockList).get(1);
        inOrder.verify(mockList).get(1);
        inOrder.verify(mockList).get(0);
        inOrder.verify(mockList).get(1);
        inOrder.verify(mockList).get(0);
        inOrder.verify(mockList).get(0);

        inOrder = inOrder(mockList);
        inOrder.verify(mockList).remove(2);
        inOrder.verify(mockList).remove(1);
        inOrder.verify(mockList).remove(1);
        inOrder.verify(mockList).remove(0);
        inOrder.verify(mockList).remove(1);
        inOrder.verify(mockList).remove(0);
        inOrder.verify(mockList).remove(0);
    }

    @Test
    void testTwoSimple() {
        @SuppressWarnings("unchecked")
        var mockList = (List<Integer>) mock(ArrayList.class);
        var calculator = new StackCalculator(mockList);
        when(mockList.get(anyInt())).thenReturn(20, 500, 480, 150, 30, 4500);
        when(mockList.size()).thenReturn(2, 2, 1, 1, 1, 2, 2, 1, 1, 1);

        assertEquals(480, calculator.calculate("500 20 -"));
        assertEquals(4500, calculator.calculate("30 150 *"));

        var inOrder = inOrder(mockList);
        inOrder.verify(mockList).add(500);
        inOrder.verify(mockList).add(20);
        inOrder.verify(mockList).add(480);
        inOrder.verify(mockList).add(30);
        inOrder.verify(mockList).add(150);
        inOrder.verify(mockList).add(4500);

        inOrder = inOrder(mockList);
        inOrder.verify(mockList).get(1);
        inOrder.verify(mockList).get(0);
        inOrder.verify(mockList).get(0);
        inOrder.verify(mockList).get(1);
        inOrder.verify(mockList).get(0);
        inOrder.verify(mockList).get(0);

        inOrder = inOrder(mockList);
        inOrder.verify(mockList).remove(1);
        inOrder.verify(mockList).remove(0);
        inOrder.verify(mockList).remove(0);
        inOrder.verify(mockList).remove(1);
        inOrder.verify(mockList).remove(0);
        inOrder.verify(mockList).remove(0);
    }

    @Test
    void testNegative() {
        @SuppressWarnings("unchecked")
        var mockList = (List<Integer>) mock(ArrayList.class);
        var calculator = new StackCalculator(mockList);
        when(mockList.get(anyInt())).thenReturn(2, -100, -200);
        when(mockList.size()).thenReturn(2, 2, 1, 1, 1);

        assertEquals(-200, calculator.calculate("-100 2 *"));

        var inOrder = inOrder(mockList);
        inOrder.verify(mockList).add(-100);
        inOrder.verify(mockList).add(2);
        inOrder.verify(mockList).add(-200);

        inOrder = inOrder(mockList);
        inOrder.verify(mockList).get(1);
        inOrder.verify(mockList).get(0);
        inOrder.verify(mockList).get(0);

        inOrder = inOrder(mockList);
        inOrder.verify(mockList).remove(1);
        inOrder.verify(mockList).remove(0);
        inOrder.verify(mockList).remove(0);
    }
}