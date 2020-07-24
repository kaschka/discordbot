package org.kaschka.fersagers.discord.bot.commands

import com.vdurmont.emoji.EmojiManager
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.kaschka.fersagers.discord.bot.utils.MessageUtils

import java.awt.Color
import java.lang.Thread.sleep
import java.time.Instant

class PollCommand : Command {

    override fun handle(args: MutableList<String>, event: MessageReceivedEvent) {
        assertPollCommand(args, event)
        val embedBuilder = EmbedBuilder()
                .setColor(Color.GREEN)
                .setTimestamp(Instant.now())

        val message = buildEmbedAndSendMessage(event, args, embedBuilder)
        addReactions(args, message)
        refreshMinutesLeft(args[1].toInt(), embedBuilder, message)
    }

    private fun refreshMinutesLeft(minutesLeft : Int, embedBuilder : EmbedBuilder, message : Message) {
        var minutesLeftVar = minutesLeft
        Thread {
            while(minutesLeftVar >= 1) {
                embedBuilder.setFooter("Minutes left: $minutesLeftVar")
                message.editMessage(embedBuilder.build()).complete()
                sleep(60 * 1000)
                minutesLeftVar -= 1
                }
            embedBuilder.setFooter("Poll is over!").setColor(Color.RED)
            result(embedBuilder, message)
            message.editMessage(embedBuilder.build()).queue()
        }.start()
    }

    private fun result(embedBuilder : EmbedBuilder, message: Message) {
        //poll the message again to retrieve the current reactions
        val newMessage = message.channel.retrieveMessageById(message.id).complete()
        val sorted = newMessage.reactions.sortedByDescending { it.count }
        val builder = StringBuilder()
        sorted.forEach {
            if (it.reactionEmote.isEmote) {
                builder.append("<:" + it.reactionEmote.emote.name + ":" + it.reactionEmote.emote.id + "> --> " + (it.count-1) + "\n")
            } else {
                builder.append(it.reactionEmote.emoji + " --> " + (it.count-1) + "\n")
            }
        }

        embedBuilder.addBlankField(false).addField("Result!", builder.toString(), false)
        message.editMessage(embedBuilder.build()).queue()
    }

    private fun addReactions(args: MutableList<String>, message : Message) {
        filterUniCodeEmojis(args).forEach{ message.addReaction(it).queue()}
        filterGuildEmojis(args, message.guild).forEach{ message.addReaction(it).queue()}
    }

    private fun buildEmbedAndSendMessage(event: MessageReceivedEvent, args: MutableList<String>, embedBuilder : EmbedBuilder): Message {
        val builder = StringBuilder()
        for (i in 2 until args.size - 1 step 2) {
            builder.append(args[i + 1] + " --> " + args[i] + "\n")
        }

        embedBuilder.addField(event.author.name + " asks: " + args[0], builder.toString(), false)
        return event.channel.sendMessage(embedBuilder.build()).complete()
    }

    private fun filterUniCodeEmojis(strings: MutableList<String>) : List<String> {
        val emojis = mutableListOf<String>()
        for (string in strings) {
            if (EmojiManager.isEmoji(string)) {
                emojis.add(string)
            }
        }
        return emojis
    }

    private fun filterGuildEmojis(strings: MutableList<String>, guild : Guild) : List<Emote> {
        val emojisLong = mutableListOf<Long>()
        for (string in strings) {
            if (string.startsWith("<")) {
                emojisLong.add(string.split(":")[2].removeSuffix(">").toLong())
            }
        }

        val emojis = mutableListOf<Emote>()
        emojisLong.forEach{ guild.getEmoteById(it)?.let { notNull -> emojis.add(notNull) } }
        return emojis
    }

    private fun assertPollCommand(args: List<String>, event: MessageReceivedEvent) {
        if(args.size < 2 || args.size % 2 != 0 || args[1].toInt() < 1 || args[1].toInt() > 24*60) {
            MessageUtils.sendMessageToUser(event.author, "Invalid args.\n /poll [title] [minutes] [choices emoji]*")
            throw RuntimeException()
        }
    }

    override fun getInvoke(): String {
        return "poll"
    }

    override fun getHelp(): String {
        return "/poll [title] [minutes] [choices emoji]*: start a poll"
    }
}
