package com.dbot;

import com.vdurmont.emoji.EmojiManager;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FishingGame {

    static void fish(GenericCommandInteractionEvent event) throws SQLException {
        String uid = event.getMember().getId();
        String fish = "";
        String alias = "";
        double d = Math.random();
        if (d < 0.000001) {
            fish = "legendary Moby Dick";
            alias = "whale2";
        } else if (d < 0.001) {//0.1%
            fish = "dragon";
        } else if (d < 0.005) { //0.4%
            fish = "whale";
        } else if (d < 0.01) {//0.5%
            fish = "shark";
        } else if (d < 0.015) {//0.5%
            if (Math.random() < 0.5) {
                fish = "gem";
            } else {
                fish = "MONA LISA";
                alias = "painting";
            }
        } else if (d < 0.020) {//0.5%
            fish = "octopus";
        } else if (d < 0.029) {//0.9%
            fish = "squid";
        } else if (d < 0.039) {//1%
            fish = "blowfish";
        } else if (d < 0.06) {//2.1%
            fish = "lobster";
        } else if (d < 0.12) {//6%
            fish = "crab";
        } else if (d < 0.25) {//13%
            fish = "tropical fish";
            alias = "tropical_fish";
        } else if (d < 0.4) { //15%
            fish = "boot";
            alias = "hiking_boot";
        } else { //60%
            fish = "common fish";
            alias = "fish";
        }
        if (alias.isEmpty()) {
            alias = fish;
        }
        Logger logger = LoggerFactory.getLogger(FishingGame.class);
        logger.info(alias);
        String a = "a ";
        if (fish.charAt(0) == 'a' || fish.charAt(0) == 'e' || fish.charAt(0) == 'i' || fish.charAt(0) == 'o' || fish.charAt(0) == 'u') {
            a = "an ";
        }

        //write to db..
        //later on we can just select from db such that ... using inner join or smth

        DBDriver db = DBDriver.getInstance();
        db.connect("jdbc:sqlite:us.db");
        ResultSet user = db.query("SELECT * FROM userfish WHERE uid = " + "'" + uid + "' AND fish = " + "'" + fish + "';");
        if (user.next()) {
            db.update("UPDATE userfish SET quantity = " + (user.getInt("quantity") + 1) + " WHERE uid = " + "'" + uid + "'" +
                    " AND fish = " + "'" + fish + "';");
        } else {
            db.update("INSERT INTO userfish (uid, fish, quantity) VALUES (" + "'" + uid + "', " + "'" + fish + "', 1);");
        }
        event.reply(EmojiManager.getForAlias(alias).getUnicode() + " <@" + uid + ">, You caught " + a + fish + "!").queue();
    }
}