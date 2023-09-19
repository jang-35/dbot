package com.dbot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

public class OverwatchCommand extends ListenerAdapter {
    static final String SUPPORT_ICON = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Support_icon.svg/1200px-Support_icon.svg.png";
    static final String TANK_ICON = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Tank_icon.svg/2048px-Tank_icon.svg.png";
    static final String DPS_ICON = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Damage_icon.svg/1024px-Damage_icon.svg.png";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("ow")) {
            return;
        }
        if (event.getOption("type") == null || event.getOption("arg") == null) {
            event.reply("invalid argument").queue();
            return;
        }
        if (event.getOption("type").getAsString().equals("hero")) {
            String hero = event.getOption("arg").getAsString().replace(' ', '-');
            try {
                HttpRequest req = HttpRequest.newBuilder().uri
                                (new URI("https://overfast-api.tekrop.fr/heroes/" + hero.toLowerCase()))
                        .timeout(Duration.of(20, SECONDS))
                        .GET()
                        .build();

                HttpResponse<String> res = HttpClient.newBuilder()
                        .build()
                        .send(req, HttpResponse.BodyHandlers.ofString());

                if (res.statusCode() == 404) {
                    event.reply("hero not found").queue();
                } else if (res.statusCode() != 200) {
                    event.reply("server error").queue();
                }
                ObjectMapper om = new ObjectMapper();
                JsonNode jn = om.readTree(res.body());

                List<JsonNode> abilities = new ArrayList<>();
                for (JsonNode j : jn.get("abilities")) {
                    abilities.add(j);
                }
                String role = String.valueOf(jn.get("role")).replaceAll("\"", "");
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(String.valueOf(jn.get("name")).replaceAll("\"", ""))
                        .setImage(String.valueOf(jn.get("portrait")).replaceAll("\"", ""))
                        .setDescription(String.valueOf(jn.get("description")).replaceAll("\"", ""))
                        .setThumbnail(role.equals("damage") ? DPS_ICON : (role.equals("support")) ? SUPPORT_ICON : TANK_ICON)
                        .setTimestamp(Instant.now())
                        .setFooter("jangs bot", "https://i.imgur.com/wjZqpBX.gif")
                        .addField("Health Stats:", "Health: " + jn.get("hitpoints").get("health")
                                + "\n" + "Armor: " + jn.get("hitpoints").get("armor") + "\n"
                                + "Shields: " + jn.get("hitpoints").get("shields") + "\n"
                                + "Total: " + jn.get("hitpoints").get("total"), false)
                        .addField("Abilities: ", "", false)
                        .addField(String.valueOf(abilities.get(0).get("name")).replaceAll("\"", ""),
                                String.valueOf(abilities.get(0).get("description")).replaceAll("\"", "")
                                , true)
                        .addField(String.valueOf(abilities.get(1).get("name")).replaceAll("\"", ""),
                                String.valueOf(abilities.get(1).get("description")).replaceAll("\"", "")
                                , true)
                        .addField(String.valueOf(abilities.get(2).get("name")).replaceAll("\"", ""),
                                String.valueOf(abilities.get(2).get("description")).replaceAll("\"", "")
                                , true)
                        .addField(String.valueOf(abilities.get(3).get("name")).replaceAll("\"", ""),
                                String.valueOf(abilities.get(3).get("description")).replaceAll("\"", "")
                                , true);
                if (abilities.size() == 5) {
                    builder.addField(String.valueOf(abilities.get(4).get("name")).replaceAll("\"", ""),
                            String.valueOf(abilities.get(4).get("description")).replaceAll("\"", "")
                            , true);
                }
                MessageEmbed embed = builder.build();
                event.replyEmbeds(embed).queue();
            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (event.getOption("type").getAsString().equals("rank")) {
            String name = event.getOption("arg").getAsString().replace('#', '-');
            try {
                HttpRequest req = HttpRequest.newBuilder().uri
                                (new URI("https://overfast-api.tekrop.fr/players/" + name + "/summary"))
                        .timeout(Duration.of(20, SECONDS))
                        .GET()
                        .build();

                HttpResponse<String> res = HttpClient.newBuilder()
                        .build()
                        .send(req, HttpResponse.BodyHandlers.ofString());

                if (res.statusCode() == 404) {
                    event.reply("player not found").queue();
                } else if (res.statusCode() != 200) {
                    event.reply("server error").queue();
                }

                ObjectMapper om = new ObjectMapper();
                JsonNode jn = om.readTree(res.body());
                if (String.valueOf(jn.get("competitive")).equalsIgnoreCase("null")) {
                    event.reply("No PC competitive data or private profile").queue();
                    return;
                }
                String tank = StringUtils.capitalize(String.valueOf
                        (jn.get("competitive").get("pc").get("tank").get("division")).replaceAll("\"", ""))
                        + " " +
                        jn.get("competitive").get("pc").get("tank").get("tier");
                if (StringUtils.containsIgnoreCase(tank, "null")) {
                    tank = "Unranked";
                }
                String dps = StringUtils.capitalize(String.valueOf
                        (jn.get("competitive").get("pc").get("damage").get("division")).replaceAll("\"", ""))
                        + " " +
                        jn.get("competitive").get("pc").get("damage").get("tier");
                if (StringUtils.containsIgnoreCase(dps, "null")) {
                    dps = "Unranked";
                }
                String supp = StringUtils.capitalize(String.valueOf
                        (jn.get("competitive").get("pc").get("support").get("division")).replaceAll("\"", ""))
                        + " " +
                        jn.get("competitive").get("pc").get("support").get("tier");
                if (StringUtils.containsIgnoreCase(supp, "null")) {
                    supp = "Unranked";
                }
                EmbedBuilder builder = new EmbedBuilder();
                builder
                        .setTitle("Rank Statistics")
                        .setAuthor(event.getOption("arg").getAsString(), "https://www.youtube.com/watch?v=dQw4w9WgXcQ", String.valueOf(jn.get("avatar")).replace("\"", ""))
                        .setDescription("Season " + jn.get("competitive").get("pc").get("season") + " ranked info.")
                        .addField("Tank", tank, true)
                        .addField("Dps", dps, true)
                        .addField("Support", supp, true)
                        .setTimestamp(Instant.now())
                        .setFooter("jangs bot", "https://i.imgur.com/wjZqpBX.gif");
                event.replyEmbeds(builder.build()).queue();
            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        } else {
            event.reply("invalid type").queue();
        }
    }
}