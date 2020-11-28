package org.kaschka.fersagers.discord.bot.configuration.permission;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;
import org.aspectj.lang.annotation.Pointcut;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.configuration.permission.Permissions.hasPermission;

@Aspect
@DeclarePrecedence("org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermissionsImpl")
public class RequiresPermissionsImpl {

    @Pointcut("@annotation(rp)")
    public void callAt(RequiresPermission rp) {
    }

    @Around("callAt(rp)")
    public void around(ProceedingJoinPoint joinPoint, RequiresPermission rp) throws Throwable {
        Object target = joinPoint.getTarget();
        if (target instanceof Command) {
            Permissions permission = ((Command) target).requiredPermissions();

            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg instanceof MessageReceivedEvent) {
                    MessageReceivedEvent event = (MessageReceivedEvent) arg;

                    if (caseTextChannel(joinPoint, permission, event)) {
                        return;
                    }
                    if (caseDirectMessage(joinPoint, permission, event)) {
                        return;
                    }
                    MessageUtils.sendMessageToUser(event.getAuthor(), "Insufficient Permissions!");
                    break;
                }
            }
        } else {
            throw new RuntimeException("Only use this Interface on Command Classes!");
        }
    }

    private boolean caseTextChannel(ProceedingJoinPoint joinPoint, Permissions permission, MessageReceivedEvent event) throws Throwable {
        if (event.isFromType(ChannelType.TEXT)) {
            Member member = event.getMember();
            if (hasPermission(permission, member)) {
                joinPoint.proceed();
                return true;
            }
        }
        return false;
    }

    private boolean caseDirectMessage(ProceedingJoinPoint joinPoint, Permissions permission, MessageReceivedEvent event) throws Throwable {
        if (event.isFromType(ChannelType.PRIVATE)) {
            if (hasPermission(permission, event.getMember())) {
                joinPoint.proceed();
                return true;
            }
        }
        return false;
    }
}
