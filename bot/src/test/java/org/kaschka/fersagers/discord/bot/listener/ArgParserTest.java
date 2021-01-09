package org.kaschka.fersagers.discord.bot.listener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArgParserTest {

    private final ArgParser argParser;

    public ArgParserTest() {
        this.argParser = new ArgParser();
    }

    @Test
    void singleArgTest() {
        Assertions.assertEquals(true, true);
    }
}
