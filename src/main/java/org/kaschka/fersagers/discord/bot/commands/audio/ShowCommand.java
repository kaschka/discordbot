package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertPermissions;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.isInVoiceChannel;

public class ShowCommand implements Command {

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        MessageUtils.logAndDeleteMessage(event);
        assertPermissions("Bot Permissions", event.getMember());

        VoiceChannel connectedChannel = event.getGuild().getAudioManager().getConnectedChannel();
        assertConnectedChannel(connectedChannel, event.getAuthor());

        if(!isInVoiceChannel(event.getMember(), connectedChannel)) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "You are not in the same voicechannel as the Bot!");
        } else {
            BlockingQueue<AudioTrack> queue = PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).scheduler.getQueue();

            String current = PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.getPlayingTrack().getInfo().title;
            String queueString =  "No Songs in Queue!";

            if(queue.size() != 0) {
                queueString = queue.stream()
                        .map(track -> "\n-> " + track.getInfo().title)
                        .collect(Collectors.joining()
                        );
            }

            MessageUtils.sendMessageToUser(event.getAuthor(), "Current playing: " + current);
            MessageUtils.sendMessageToUser(event.getAuthor(), "Queue: " + queueString);
        }
    }

    private void assertConnectedChannel(VoiceChannel connectedChannel, User user) {
        if(connectedChannel == null) {
            MessageUtils.sendMessageToUser(user, "Bot is not in a Channel!");
            throw new RuntimeException();
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
