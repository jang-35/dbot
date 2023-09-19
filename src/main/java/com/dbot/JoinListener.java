package com.dbot;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel tc = event.getGuild().getTextChannelsByName("general", true).get(0);
        tc.sendMessage("\uD83E\uDD13 <@" + event.getMember().getId() + ">").queue();
    }
}