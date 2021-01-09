package org.kaschka.fersagers.discord.bot.listener;

import java.util.ArrayList;
import java.util.List;

public class ArgParser {

    public List<String> parse(String[] unfilteredArgs) {
        final List<String> extractedArgs = new ArrayList<>();
        int firstBracketPosition = 0, lastBracketPosition = 0;

        for (int i = 1; i < unfilteredArgs.length; i++) {
            if (isOpeningBracket(unfilteredArgs[i])) {
                firstBracketPosition = i;
            }

            if (isClosingBracket(unfilteredArgs[i])) {
                lastBracketPosition = i;
            }

            if (bothBracketsFound(firstBracketPosition, lastBracketPosition)) {
                firstBracketPosition = 0;
                lastBracketPosition = 0;
                extractedArgs.add(getArgBetweenBrackets(unfilteredArgs, firstBracketPosition, lastBracketPosition));

                //if no brackets are found yet, we must have a single element
            } else if (noBracketsFound(firstBracketPosition, lastBracketPosition)) {
                extractedArgs.add(unfilteredArgs[i]);
            }
        }

        removeBrackets(extractedArgs);
        return extractedArgs;
    }

    private boolean bothBracketsFound(int firstBracketPosition, int lastBracketPosition) {
        return firstBracketPosition != 0 && lastBracketPosition != 0;
    }

    private boolean noBracketsFound(int firstBracketPosition, int lastBracketPosition) {
        return firstBracketPosition == 0 && lastBracketPosition == 0;
    }

    private boolean isClosingBracket(String string) {
        return string.startsWith("}") || string.endsWith("}");
    }

    private boolean isOpeningBracket(String string) {
        return string.startsWith("{");
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
            args.set(i, args.get(i).replaceAll("[{}]", ""));
        }
    }
}
