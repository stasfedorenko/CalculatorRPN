package calсulator;


import java.util.Stack;

public class Calculator {
    private static final String OPERATORS = "+-*/^%";

    public double DecideExpression(String expression) {
        CheckExpression(expression);
        String prepared = CheckingForUnaryMinus(expression);
        String rpn = ExpressionToRpn(prepared);
        return RpnToResult(rpn);
    }

    private void CheckExpression(String expression) {
        boolean flag = expression.matches("^[-+*/^%0-9.() ]*$");

        if (!flag) {
            ExpressionError("Неизвестные символы в выражении!!!");
        }

        expression = expression.replaceAll(" ", "");
        for (int i = 0; i < OPERATORS.length(); i++) {
            if (expression.charAt(0) == OPERATORS.charAt(i) && expression.charAt(0) != '-' && expression.charAt(0) != '%') {
                ExpressionError("Оператор в начале выражения!!!");
            }
            if (expression.charAt(expression.length() - 1) == OPERATORS.charAt(i)) {
                ExpressionError("Оператор в конце выражения!!!");
            }
        }

        for (int i = 0; i < expression.length(); i++) {
            for(int y = 0; y< OPERATORS.length(); y++){
                if(expression.charAt(i)== OPERATORS.charAt(y)){
                    for(int z = 0; z< OPERATORS.length(); z++){
                        if(expression.charAt(i+1)== OPERATORS.charAt(z)){
                            ExpressionError("Ошибка постановки операторов!!!");
                        }
                    }
                }
            }
        }
    }

    private void ExpressionError(String error) {
        System.out.println(error);
        System.exit(0);
    }

    private String CheckingForUnaryMinus(String expression) {
        StringBuilder preparedExpression = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char symbol = expression.charAt(i);
            if (symbol == '-') {
                if (expression.startsWith("-")) {
                    preparedExpression.append(0);
                } else if (expression.charAt(i - 1) == '(') {
                    preparedExpression.append(0);
                }
            }
            preparedExpression.append(symbol);
        }
        return preparedExpression.toString();
    }

    private String ExpressionToRpn(String expression) {
        StringBuilder str = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        int priority;

        for (int i = 0; i < expression.length(); i++) {
            priority = PrioritySymbol(expression.charAt(i));
            if (priority == 0) str.append(expression.charAt(i));
            if (priority == 2) stack.push(expression.charAt(i));
            if (priority > 2) {
                str.append(" ");
                while (!stack.empty()) {
                    if (PrioritySymbol(stack.peek()) >= priority)
                        str.append(stack.pop());
                    else break;
                }
                stack.push(expression.charAt(i));
            }
            if (priority == 1) {
                str.append(" ");
                while (PrioritySymbol(stack.peek()) != 2) {
                    if (stack.size() == 1 && stack.lastElement() != '(') {
                        ExpressionError("Ошибка в постановке скобок!!!");
                        break;
                    }
                    str.append(stack.pop());
                }
                stack.pop();
            }
        }

        while (!stack.empty()) {
            str.append(stack.pop());
        }

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                ExpressionError("Ошибка в постановке скобок!!!");
            }
        }

        System.out.println("Reverse Polish notation is: " + str);

        return str.toString();
    }

    private Double RpnToResult(String rpn) {
        StringBuilder operand = new StringBuilder();
        Stack<Double> stack = new Stack<>();
        int priority;

        for (int i = 0; i < rpn.length(); i++) {
            priority = PrioritySymbol(rpn.charAt(i));
            if (rpn.charAt(i) == ' ') continue;
            if (priority == 0) {
                while (rpn.charAt(i) != ' ' && priority == 0) {
                    operand.append(rpn.charAt(i++));
                    if (i == rpn.length()) break;
                    priority = PrioritySymbol(rpn.charAt(i));
                }
                stack.push(Double.parseDouble(String.valueOf(operand)));
                operand = new StringBuilder();
            }
            if (priority == 3 && rpn.charAt(i) == '%') {
                double a = stack.pop();
                stack.push(a / 100);
            }
            if (priority > 2 && rpn.charAt(i) != '%') {
                double a = stack.pop();
                double b = stack.pop();
                for(int y = 0; y< OPERATORS.length(); y++){
                    if(rpn.charAt(i)== OPERATORS.charAt(y)){
                        switch (y) {
                            case 0 -> stack.push(b + a);
                            case 1 -> stack.push(b - a);
                            case 2 -> stack.push(b * a);
                            case 3 -> stack.push(b / a);
                            case 4 -> stack.push(Math.pow(b, a));
                        }
                    }

                }
            }
        }
        return stack.pop();
    }

    private int PrioritySymbol(char symbol) {
        if (symbol == '*' || symbol == '/' || symbol == '^')
            return 4;
        else if (symbol == '+' || symbol == '-' || symbol == '%')
            return 3;
        else if (symbol == '(')
            return 2;
        else if (symbol == ')')
            return 1;
        else
            return 0;
    }
}
