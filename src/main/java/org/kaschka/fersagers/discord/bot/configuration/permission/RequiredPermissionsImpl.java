package org.kaschka.fersagers.discord.bot.configuration.permission;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.kaschka.fersagers.discord.bot.utils.DiscordUtils;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

@Aspect
public class RequiredPermissionsImpl {

    @Pointcut("@annotation(permission)")
    public void callAt(RequiredPermission permission) {
    }

    @Around("callAt(permission)")
    public void around(ProceedingJoinPoint joinPoint, RequiredPermission permission) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof MessageReceivedEvent) {
                MessageReceivedEvent event = (MessageReceivedEvent) arg;

                if(caseTextChannel(joinPoint, permission, event)) return;
                if(caseDirectMessage(joinPoint, permission, event)) return;
                MessageUtils.sendMessageToUser(event.getAuthor(), "Insufficient Permissions!");
                break;
            }
        }
    }

    private boolean caseTextChannel(ProceedingJoinPoint joinPoint, RequiredPermission permission, MessageReceivedEvent event) throws Throwable {
        if(event.isFromType(ChannelType.TEXT)) {
            Member member = event.getMember();
            if (DiscordUtils.hasPermission(permission.value(), member) || isUserIdAllowed(permission.allowedIds(), member.getIdLong())) {
                joinPoint.proceed();
                return true;
            }
        }
        return false;
    }

    private boolean caseDirectMessage(ProceedingJoinPoint joinPoint, RequiredPermission permission, MessageReceivedEvent event) throws Throwable {
        if(event.isFromType(ChannelType.PRIVATE)) {
            if(isUserIdAllowed(permission.allowedIds(), event.getAuthor().getIdLong())) {
                joinPoint.proceed();
                return true;
            }
        }
        return false;
    }

    private boolean isUserIdAllowed(long[] allowedIds, long userId) {
        if(allowedIds.length > 0) {
            for (long id : allowedIds) {
                if (id == userId) {
                    return true;
                }
            }
        }
        return false;
    }
}
