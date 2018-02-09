package calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Calc {

    private static final String EMPTY_LINE = "";
    private static final String INVALID_INPUT = "Невалидные данные";
    private static final String ZERO_DIVIDE = "Деление на 0";

    private ArrayDeque<String> mDeque = new ArrayDeque<>();

    public static void main(String[] args) {
        new Calc().start();
    }

    private void start() {
        Converter converter = new Converter();

        Scanner input = new Scanner(System.in);
        String line;

        do {
            line = input.nextLine();

            if (line.equals(EMPTY_LINE)) return;

            ArrayList<String> parts = converter.convertToRpn(line);

            try {
                calculate(parts);

                System.out.println(new BigDecimal(mDeque.pop()).stripTrailingZeros().toPlainString());
                mDeque.clear();
            } catch (ArithmeticException e) {
                System.out.println(ZERO_DIVIDE);
                mDeque.clear();
            } catch (NumberFormatException | NoSuchElementException e) {
                System.out.println(INVALID_INPUT);
                mDeque.clear();
            }

        } while (!line.equals(EMPTY_LINE));

    }

    private void calculate(ArrayList<String> parts) {
        for (int i = 0; i < parts.size(); i++) {
            String part = parts.get(i);

            switch (part) {
                case "+":
                    sum();
                    break;
                case "-":
                    sub();
                    break;
                case "*":
                    mult();
                    break;
                case "/":
                    div();
                    break;
                case "^":
                    pow();
                    break;
                default:
                    mDeque.addFirst(part.replaceAll(",", "\\."));
            }
        }
    }

    private void sum() {
        BigDecimal num2 = new BigDecimal(mDeque.pop());
        BigDecimal num1 = new BigDecimal(mDeque.pop());
        BigDecimal result = num1.add(num2);
        mDeque.addFirst(result.toString());
    }

    private void sub() {
        BigDecimal num2 = new BigDecimal(mDeque.pop());
        BigDecimal num1 = new BigDecimal(mDeque.pop());
        BigDecimal result = num1.subtract(num2);
        mDeque.addFirst(result.toString());
    }

    private void mult() {
        BigDecimal num2 = new BigDecimal(mDeque.pop());
        BigDecimal num1 = new BigDecimal(mDeque.pop());
        BigDecimal result = num1.multiply(num2);
        mDeque.addFirst(result.toString());
    }

    private void div() {
        BigDecimal num2 = new BigDecimal(mDeque.pop());
        BigDecimal num1 = new BigDecimal(mDeque.pop());
        BigDecimal result = num1.divide(num2, new MathContext(200, RoundingMode.HALF_UP));
        mDeque.addFirst(result.toString());
    }

    private void pow() {
        BigDecimal num2 = new BigDecimal(mDeque.pop());
        BigDecimal num1 = new BigDecimal(mDeque.pop());
        BigDecimal result = num1.pow(num2.intValue(), new MathContext(200, RoundingMode.HALF_UP));
        mDeque.addFirst(result.toString());
    }
}
