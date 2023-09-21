package com.dbot;

import com.vdurmont.emoji.EmojiManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("inventory")) {
            DBDriver db = DBDriver.getInstance();
            try {
                db.connect("jdbc:sqlite:us.db");
                ResultSet user = db.query("SELECT fish, quantity, rarity, sellprice, alias FROM userfish " +
                        "INNER JOIN fish ON userfish.fish = fish.fishname" +
                        " WHERE uid = " + "'" + event.getUser().getId() + "';");
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(event.getUser().getName() + "'s inventory:");
                while (user.next()) {
                    String alias = user.getString("alias") == null ? user.getString("fish") : user.getString("alias");
                    String rarity = "";
                    if (user.getInt("rarity") == -5) {
                        rarity = EmojiManager.getForAlias("heart_eyes_cat").getUnicode();
                    } else if (user.getInt("rarity") == -4) {
                        rarity = EmojiManager.getForAlias("wastebasket").getUnicode();
                    } else {
                        for (int i = 0; i < user.getInt("rarity"); i++) {
                            rarity += EmojiManager.getForAlias("star").getUnicode();
                        }
                    }
                    builder.addField("", EmojiManager.getForAlias(alias).getUnicode() + " " + StringUtils.capitalize(user.getString("fish")) +
                            " x" + user.getInt("quantity") + "\n"
                            + "Rarity: " + rarity + "\n" +
                            "Sell for: " + user.getInt("sellprice") + " coins", true);
                }
                event.replyEmbeds(builder.build()).queue();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }
}