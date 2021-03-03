package io.github.overlordsiii.command;

import io.github.overlordsiii.Main;
import io.github.overlordsiii.game.AmongUsGame;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageLinkCommand {



	public void execute(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (content.equals("!messagelink") && Main.currentGame != null) {
			event.getMessage().reply("Link to current game message: " + AmongUsGame.getMessageLink(Main.currentGame.getMessage())).queue(mes -> mes.suppressEmbeds(true).queue());
		}
	}
}
