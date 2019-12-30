package org.kaschka.fersagers.discord.bot.configuration;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiredPermission;
import org.kaschka.fersagers.discord.bot.utils.DiscordUtils;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

@Aspect
public class LogAndRemoveMessageImpl {

    private static Logger logger = Logger.getInstance();


    @Pointcut("@annotation(message)")
    public void callAt(LogAndRemoveMessage message) {
    }

    @Around("callAt(message)")
    public void around(ProceedingJoinPoint joinPoint, LogAndRemoveMessage message) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof MessageReceivedEvent) {
                MessageReceivedEvent event = (MessageReceivedEvent) arg;
                if(!event.isFromType(ChannelType.PRIVATE)) {
                    event.getMessage().delete().queue();
                }
                logger.logChatMessage(event);
            }
        }
        joinPoint.proceed();
    }
}
