package org.kaschka.fersagers.discord.bot.listener.handler

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.kaschka.fersagers.discord.bot.utils.Logger
import java.util.regex.Pattern

private val HELLO_REGEX = Pattern.compile("^(h+(e+|a+)l+o+\\?*)$", Pattern.CASE_INSENSITIVE)
private val HI_REGEX = Pattern.compile("^(h+i+\\?*)$", Pattern.CASE_INSENSITIVE)
private val HEY_REGEX = Pattern.compile("^(h+e+y\\?*)$", Pattern.CASE_INSENSITIVE)

private val logger = Logger.getInstance()

class HelloHandler : ChatHandler {

    enum class TRIGGER(val url: String) {
        HELLO("https://tenor.com/view/hi-hello-wave-gif-5957952"),
        HI("https://tenor.com/view/hi-hello-stitch-lilo-and-stitch-wave-gif-3427491"),
        HEY("https://tenor.com/view/hey-jimmy-fallon-gif-5247867"),
        NONE("")
    }

    override fun handle(event: MessageReceivedEvent?): Boolean {
        val message = event?.message
        if (message != null) {
            val trigger = getTriggerString(message.contentRaw)
            if(trigger != TRIGGER.NONE) {
                answerOnHello(message, trigger)
                return true
            }
        }
        return false
    }

    private fun containsHello(string: String): Boolean {
        return HELLO_REGEX.matcher(string).find();
    }

    private fun containsHi(string: String): Boolean {
        return HI_REGEX.matcher(string).find();
    }

    private fun containsHey(string: String): Boolean {
        return HEY_REGEX.matcher(string).find();
    }

    private fun getTriggerString(string: String) : TRIGGER {
        return when {
            containsHello(string) -> {
                TRIGGER.HELLO
            }
            containsHi(string) -> {
                TRIGGER.HI
            }
            containsHey(string) -> {
                TRIGGER.HEY
            }
            else -> TRIGGER.NONE
        }
    }

    private fun answerOnHello(message: Message, trigger: TRIGGER) {
        message.channel.sendMessage(trigger.url).queue()
        logger.log(String.format("%s@%s@%s: Answered on %s",
                message.author.name,
                message.guild.name,
                message.channel.name,
                trigger.name)
        )
    }
}
