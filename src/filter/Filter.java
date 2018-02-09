package filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {

    private static final String EMPTY_LINE = "";
    private static final String SPACE = " ";
    private static final char SEMICOLON = ';';

    private Pattern[] mPatterns;
    private List<String> mOutput;

    private Filter() {
        mOutput = new ArrayList<>();
    }

    public static void main(String[] args) {
        new Filter().start(args);
    }

    private void start(String[] args) {
        initArgs(args);

        Scanner input = new Scanner(System.in);
        String line;

        do {
            line = input.nextLine();

            String[] words = getWords(line);

            boolean isCorresponds = checkMatches(words);

            if (isCorresponds) mOutput.add(line);

        } while (!line.equals(EMPTY_LINE));

        showResult();
    }

    private String[] getWords(String line) {
        String[] words = line.split(SPACE);

        if (words.length > 0) {
            String lastWord = words[words.length - 1];

            if (lastWord.length() > 0) {
                char lastChar = lastWord.charAt(lastWord.length() - 1);

                if (lastChar == SEMICOLON) {
                    words[words.length - 1] = lastWord.substring(0, lastWord.length() - 1);
                }
            }
        }
        return words;
    }

    private void showResult() {
        for (int i = 0; i < mOutput.size(); i++) {
            System.out.println(mOutput.get(i));
        }
    }

    private boolean checkMatches(String words[]) {
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < mPatterns.length; j++) {
                Pattern pattern = mPatterns[j];
                String word = words[i];
                Matcher matcher = pattern.matcher(word);

                if (matcher.matches()) return true;
            }
        }
        return false;
    }

    private void initArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            mPatterns = new Pattern[args.length];
            mPatterns[i] = Pattern.compile(args[i]);
        }
    }
}
