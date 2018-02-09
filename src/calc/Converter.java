package calc;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Converter {

    private static final String EMPTY = "";

    private char[] mOperators = new char[]{'+', '-', '*', '/', '^', '(', ')'};
    private String mInput;
    private ArrayList<String> mPartsList;
    private ArrayDeque<String> mTempOp;
    private ArrayList<String> mOutput;


    Converter() {
        resetData();
    }

    private void resetData() {
        mPartsList = new ArrayList<>();
        mTempOp = new ArrayDeque<>();
        mOutput = new ArrayList<>();
    }

    private void parseLine() {
        resetData();

        int opIndex = -1;

        for (int i = 0; i < mInput.length(); i++) {
            char c = mInput.charAt(i);

            for (int j = 0; j < mOperators.length; j++) {
                if (mOperators[j] == c) {
                    String operator = String.valueOf(c);

                    if (c == '-') {
                        String num = mInput.substring(opIndex + 1, i).trim();

                        if (num.equals(EMPTY)) continue;
                    }

                    if (i - opIndex <= 1) {
                        mPartsList.add(operator);
                    } else {
                        String num = mInput.substring(opIndex + 1, i).trim();

                        if (!num.equals(EMPTY)) mPartsList.add(num);

                        mPartsList.add(operator);
                    }
                    opIndex = i;
                }
            }

            if (i == mInput.length() - 1) {
                if (c != ')') {
                    String num = mInput.substring(opIndex + 1).trim();
                    mPartsList.add(num);
                }
            }
        }
    }

    public ArrayList<String> convertToRpn(String input) {
        mInput = input.trim();

        parseLine();

        for (int i = 0; i < mPartsList.size(); i++) {
            String string = mPartsList.get(i);

            if (isOperator(string)) {
                checkOperators(string);
            } else {
                mOutput.add(string);
            }
        }

        while (!mTempOp.isEmpty()) {
            mOutput.add(mTempOp.pop());
        }
        return mOutput;
    }

    private boolean isOperator(String string) {
        for (int i = 0; i < mOperators.length; i++) {
            if (String.valueOf(mOperators[i]).equals(string)) return true;
        }
        return false;
    }

    private void checkOperators(String string) {
        switch (string) {
            case "(":
                mTempOp.addFirst(string);
                break;
            case ")":
                getOperators();
                break;
            case "^":
                getOperators(string, 3);
                break;
            case "*":
            case "/":
                getOperators(string, 2);
                break;
            case "+":
            case "-":
                getOperators(string, 1);
                break;
        }
    }

    private void getOperators() {
        while (!mTempOp.isEmpty()) {
            String operator = mTempOp.pop();

            if (operator.equals("(")) break;

            mOutput.add(operator);
        }
    }

    private void getOperators(String currentOp, int currentPriority) {
        while (!mTempOp.isEmpty()) {
            String lastOp = mTempOp.pop();

            if (lastOp.equals("(")) {
                mTempOp.push(lastOp);
                break;
            } else {
                int priority = getPriority(lastOp);

                if (priority < currentPriority) {
                    mTempOp.push(lastOp);
                    break;
                } else {
                    mOutput.add(lastOp);
                }
            }
        }
        mTempOp.push(currentOp);
    }

    private int getPriority(String lastOp) {
        switch (lastOp) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 3;
        }
    }
}
