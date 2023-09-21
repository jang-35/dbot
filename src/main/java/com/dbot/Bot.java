package com.dbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {
    public static void main(String[] args) throws Exception {
        JDA api = JDABuilder.createDefault(System.getenv("BOT_TOKEN"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new PingListener(),
                        new JoinListener(),
                        new BasicCommand(),
                        new OverwatchCommand(),
                        new InventoryCommand()
                )
                .build()
                .awaitReady();
        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing("/help"));


        api.updateCommands().addCommands(
                Commands.slash("say", "Make the bot say something!")
                        .addOption(OptionType.STRING, "message", "What the bot should say."),
                Commands.slash("help", "Learn about jangs bot"),
                Commands.slash("ow", "Overwatch stats lookup.")
                        .addOption(OptionType.STRING, "type", "type of lookup: rank, hero")
                        .addOption(OptionType.STRING, "arg", "argument for lookup: user name#tag, hero name"),
                Commands.slash("fish", "\uD83C\uDFA3 Cast your line!"),
                Commands.slash("inventory", "View your inventory!")
        ).queue();
    }
}