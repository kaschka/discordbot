package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.AudioPlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.utils.DiscordUtils;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.isInVoiceChannel;

public class ShowCommand implements Command {

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        User user = event.getAuthor();

        VoiceChannel connectedChannel = DiscordUtils.getBotAndUserVoiceChannel(member);

        if (!isInVoiceChannel(member, connectedChannel)) {
            MessageUtils.sendMessageToUser(user, "You are not in the same voicechannel as the Bot!");
        } else {
            BlockingQueue<AudioTrack> queue = AudioPlayerManager.getInstance().getGuildMusicManager(guild).scheduler.getQueue();

            String current = AudioPlayerManager.getInstance().getGuildMusicManager(guild).player.getPlayingTrack().getInfo().title;
            String queueString = "";

            if (queue.size() > 0) {
                queueString = queue.stream()
                        .map(track -> "\n-> " + track.getInfo().title)
                        .collect(Collectors.joining()
                        );
            }

            current += queueString;
            MessageUtils.sendMessageToUser(user, "Current playing: " + current);

        }
    }

    @Override
    public String getInvoke() {
        return "show";
    }

    @Override
    public String getHelp() {
        return "/show: Shows the current playlist";
    }
}
