package com.dbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BasicCommand extends ListenerAdapter {
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("say")) {
            event.reply(event.getOption("message").getAsString()).queue();
        } else if (event.getName().equals("help")) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setDescription("Supported commands:\n" +
                    "/say to make the bot say something\n" +
                    "/ow for Overwatch data lookups. Currently, rank and hero are supported options for type");
            MessageEmbed embed = builder.build();
            event.replyEmbeds(embed).queue();
        }
    }
}