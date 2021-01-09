package org.kaschka.fersagers.discord.bot.listener;

import java.util.ArrayList;
import java.util.List;

public class ArgParser {

    private static final String STRING_OPENING_VALUE = "{";
    private static final String STRING_CLOSING_VALUE = "}";
    private static final int RESET = -1;

    public List<String> parse(String[] unfilteredArgs) {
        final List<String> extractedArgs = new ArrayList<>();
        int firstBracketPosition = RESET, lastBracketPosition = RESET;

        for (int i = 0; i < unfilteredArgs.length; i++) {
            if (isStringOpening(unfilteredArgs[i])) {
                firstBracketPosition = i;
            }

            if (isStringClosing(unfilteredArgs[i])) {
                lastBracketPosition = i;
            }

            if (bothBracketsFound(firstBracketPosition, lastBracketPosition)) {
                extractedArgs.add(getArgBetweenBrackets(unfilteredArgs, firstBracketPosition, lastBracketPosition));
                firstBracketPosition = RESET;
                lastBracketPosition = RESET;
                //if no brackets are found yet, we must have a single element
            } else if (noBracketsFound(firstBracketPosition, lastBracketPosition)) {
                extractedArgs.add(unfilteredArgs[i]);
            }
        }

        removeBrackets(extractedArgs);
        return extractedArgs;
    }

    private boolean bothBracketsFound(int firstBracketPosition, int lastBracketPosition) {
        return firstBracketPosition != RESET && lastBracketPosition != RESET;
    }

    private boolean noBracketsFound(int firstBracketPosition, int lastBracketPosition) {
        return firstBracketPosition == RESET && lastBracketPosition == RESET;
    }

    private boolean isStringClosing(String string) {
        return string.startsWith(STRING_CLOSING_VALUE) || string.endsWith(STRING_CLOSING_VALUE);
    }

    private boolean isStringOpening(String string) {
        return string.startsWith(STRING_OPENING_VALUE);
    }

    private String getArgBetweenBrackets(String[] unfilteredArgs, int firstBracketPosition, int lastBracketPosition) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = firstBracketPosition; i <= lastBracketPosition; i++) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(unfilteredArgs[i]);
        }
        return stringBuilder.toString();
    }

    private void removeBrackets(List<String> args) {
        for (int i = 0; i < args.size(); i++) {
            args.set(i, args.get(i).replaceAll(String.format("[%s%s]", STRING_OPENING_VALUE, STRING_CLOSING_VALUE), ""));
        }
    }
}
