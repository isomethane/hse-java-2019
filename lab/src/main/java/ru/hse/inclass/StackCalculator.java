package ru.hse.inclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StackCalculator {
    public static void main(String[] args) {
        while (true) {
            Scanner in = new Scanner(System.in);
            var calc = new StackCalculator(new ArrayList<>());
            System.out.println(calc.calculate(in.nextLine()));
        }
    }

    private List<Integer> stack;

    StackCalculator(List<Integer> stack) {
        this.stack = stack;
    }

    public int calculate(String str) {
        Scanner in = new Scanner(str);

        while(in.hasNext()) {
            if (in.hasNextInt()) {
                stack.add(in.nextInt());
            } else {
                String token = in.next();
                if (token.length() != 1) {
                    throw new IllegalArgumentException();
                }

                char c = token.charAt(0);
                int firstOperand, secondOperand;
                secondOperand = stack.get(stack.size() - 1);
                stack.remove(stack.size() - 1);
                firstOperand = stack.get(stack.size() - 1);
                stack.remove(stack.size() - 1);

                switch (c) {
                    case '-':
                        stack.add(firstOperand - secondOperand);
                        break;
                    case '+':
                        stack.add(firstOperand + secondOperand);
                        break;
                    case '*':
                        stack.add(firstOperand * secondOperand);
                        break;
                    case '/':
                        stack.add(firstOperand / secondOperand);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }

        if (stack.size() == 1) {
            return stack.get(0);
        } else {
            throw new IllegalStateException();
        }
    }
}
