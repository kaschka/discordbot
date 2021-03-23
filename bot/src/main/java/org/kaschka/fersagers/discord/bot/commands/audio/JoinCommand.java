package org.kaschka.fersagers.discord.bot.commands.audio;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.InMemoryConfiguration;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;

import java.util.List;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertVoiceChannelNotNull;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getCurrentVoiceChannel;

public class JoinCommand implements Command {

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = getCurrentVoiceChannel(event.getMember());
        assertVoiceChannelNotNull(event.getAuthor(), event.getAuthor().getName(), voiceChannel);
        audioManager.openAudioConnection(voiceChannel);
        InMemoryConfiguration.isManuallyJoined.put(event.getGuild().getIdLong(), true);
    }

    @Override
    public String getInvoke() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "/join: Joins the current server channel";
    }
}
