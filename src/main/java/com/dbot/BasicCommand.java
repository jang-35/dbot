package com.dbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class BasicCommand extends ListenerAdapter {
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("say")) {
            event.reply(event.getOption("message").getAsString()).queue();
        } else if (event.getName().equalsIgnoreCase("help")) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setDescription("Supported commands:\n" +
                    "/say to make the bot say something\n" +
                    "/ow for Overwatch data lookups. Currently, rank and hero are supported options for type");
            MessageEmbed embed = builder.build();
            event.replyEmbeds(embed).queue();
        } else if (event.getName().equalsIgnoreCase("fish")) {
            try {
                FishingGame.fish(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (event.getName().equalsIgnoreCase("profile")) {

        } else if (event.getName().equalsIgnoreCase("inventory")) {
            DBDriver dbd = DBDriver.getInstance();
            try {
                dbd.connect("jdbc:sqlite:us.db");
                
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}