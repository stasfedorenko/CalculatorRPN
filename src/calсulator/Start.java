package calсulator;

import java.util.Scanner;

public class Start {
    public static void main(String[] args) {
        System.out.println("enter expression (and press enter):\n");
        String expression;
        Scanner sc;

        sc = new Scanner(System.in);
        expression = sc.nextLine();

        System.out.println("Result: " + new Calculator().DecideExpression(expression));
        sc.close();
    }
}
