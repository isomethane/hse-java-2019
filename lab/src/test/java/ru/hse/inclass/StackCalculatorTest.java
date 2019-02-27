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

        assertEquals(8, calculator.calculate("1 2 + 5 +"));

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
    }
}