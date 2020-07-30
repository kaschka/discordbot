package org.kaschka.fersagers.discord.bot.commands.poll

import com.vdurmont.emoji.EmojiManager
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse
import org.kaschka.fersagers.discord.bot.commands.Command
import org.kaschka.fersagers.discord.bot.configuration.ApplicationConfiguration
import org.kaschka.fersagers.discord.bot.db.DbService
import org.kaschka.fersagers.discord.bot.utils.Logger
import org.kaschka.fersagers.discord.bot.utils.MessageUtils
import java.awt.Color
import java.lang.Thread.sleep
import java.time.Duration
import java.time.Instant

class PollCommand() : Command {

    private val MAX_TIME = 24*60
    private val db = DbService()

    private val logger = Logger.getInstance()


    override fun handle(args: MutableList<String>, event: MessageReceivedEvent) {
        assertPollCommand(args, event)
        val poll = Poll()
        poll.channelId  = event.channel.idLong
        poll.start = Instant.now().toEpochMilli()
        poll.endTime = Instant.now().plusMillis(Duration.ofMinutes(args[1].toLong()).toMillis()).toEpochMilli()

        val embedBuilder = EmbedBuilder()
                .setColor(Color.GREEN)
                .setTimestamp(Instant.ofEpochMilli(poll.start))
        val message = buildEmbedAndSendMessage(event, args, embedBuilder)
        poll.messageId = message.idLong
        db.addPoll(poll)
        logger.log("Created Poll! Author: " + event.author.name + ", Title: " + args[0] + ", Time: " + args[1] + "m")

        addReactions(args, message)
        refreshPoll(poll)
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
        if(args.size < 2 || args.size % 2 != 0 || args[1].toInt() < 1 || args[1].toInt() > MAX_TIME) {
            MessageUtils.sendMessageToUser(event.author, "Invalid args.\n /poll [title] [minutes] [choices emoji]*")
            MessageUtils.sendMessageToUser(event.author, "-- Max $MAX_TIME minutes")
            MessageUtils.sendMessageToUser(event.author, "You wrote: \n " + event.message.contentRaw)
            throw RuntimeException()
        }
    }

    override fun getInvoke(): String {
        return "poll"
    }

    override fun getHelp(): String {
        return "/poll [title] [minutes] [choices emoji]*: start a poll. Use {example with space} to group spaces!"
    }

    companion object Refresher {
        val db = DbService()

        fun refreshPoll(poll: Poll) {
            Thread {
                var message: Message? = null
                try {
                    message = ApplicationConfiguration.SHARD_MANAGER.getTextChannelById(poll.channelId)?.retrieveMessageById(poll.messageId)?.complete()
                } catch (ex : ErrorResponseException) {
                    if(ex.errorCode == ErrorResponse.UNKNOWN_MESSAGE.code) {
                        db.deletePoll(poll)
                    }
                }

                if(message != null) {
                    val embedBuilder = createEmbedBuilderFromEmbed(message.embeds[0])
                    while (Instant.now().isBefore(Instant.ofEpochMilli(poll.endTime))) {
                        embedBuilder.setFooter("Time left: " + poll.humanReadableDuration)
                        message.editMessage(embedBuilder.build()).complete()
                        sleep(60 * 1000)
                    }
                    db.deletePoll(poll)
                    embedBuilder.setFooter("Poll is over!").setColor(Color.RED)
                    result(embedBuilder, message)
                    message.editMessage(embedBuilder.build()).queue()
                }
            }.start()
        }

        private fun createEmbedBuilderFromEmbed(embed : MessageEmbed) : EmbedBuilder {
            val embedBuilder = EmbedBuilder()
            for (field in embed.fields) {
                embedBuilder.addField(field.name, field.value, false)
            }
            embedBuilder.setTimestamp(embed.timestamp)
            embedBuilder.setColor(embed.color)
            embedBuilder.setTitle(embed.title)
            embedBuilder.setDescription(embed.description)

            if(embed.author != null) {
                embedBuilder.setAuthor(embed.author.toString())
            }

            return embedBuilder;
        }

        private fun result(embedBuilder : EmbedBuilder, message: Message) {
            //poll the message again to retrieve the current reactions
            val newMessage = message.channel.retrieveMessageById(message.id).complete()
            val sorted = newMessage.reactions.sortedByDescending { it.count }
            val builder = StringBuilder()
            var none = true
            sorted.forEach {
                if(it.count > 1) {
                    if (it.reactionEmote.isEmote) {
                        none = false
                        builder.append("<:" + it.reactionEmote.emote.name + ":" + it.reactionEmote.emote.id + "> --> " + (it.count - 1) + "\n")
                    } else {
                        none = false
                        builder.append(it.reactionEmote.emoji + " --> " + (it.count - 1) + "\n")
                    }
                }
            }
            if(!none) {
                embedBuilder.addBlankField(false).addField("Result!", builder.toString(), false)
            } else {
                embedBuilder.addBlankField(false).addField("Noone voted!", "", false)
                embedBuilder.setColor(Color.YELLOW)
            }
            message.editMessage(embedBuilder.build()).queue()
        }
    }
}
