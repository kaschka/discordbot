package org.kaschka.fersagers.discord.bot.commands.audio;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.AudioPlayerManager;
import org.kaschka.fersagers.discord.bot.audio.GuildMusicManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import java.util.List;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getBotAndUserVoiceChannel;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.isInVoiceChannel;

public class SkipCommand implements Command {

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        VoiceChannel connectedChannel = getBotAndUserVoiceChannel(event.getMember());

        if (!isInVoiceChannel(event.getMember(), connectedChannel)) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "You are not in the same voicechannel as the Bot!");
        } else {
            final GuildMusicManager guildMusicManager = AudioPlayerManager.getInstance().getGuildMusicManager(event.getGuild());
            skipAll(args, guildMusicManager);
            guildMusicManager.player.stopTrack();
        }
    }

    private void skipAll(List<String> args, GuildMusicManager guildMusicManager) {
        if (args.size() == 1 && args.get(0).equals("all")) {
            guildMusicManager.scheduler.clearQueue();
        }
    }

    @Override

    public String getInvoke() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "/skip [all]: Skips the current track";
    }
}
