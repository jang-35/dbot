package com.dbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingListener extends ListenerAdapter { //listeneradapter provides methods for specific classes instead of us having to check
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String content = event.getMessage().getContentRaw();
        if (content.equals("!ping")) {
            event.getChannel().sendMessage("pong!").queue();
        }
    }
}