package io.github.overlordsiii.command;

import java.awt.Color;

import io.github.overlordsiii.Main;
import io.github.overlordsiii.game.AmongUsGame;
import io.github.overlordsiii.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class MessageLinkCommand {


	@SubscribeEvent
	public void execute(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (!content.equals("!messagelink")) return;

		if (Main.currentGame == null) {
			event.getMessage().reply(EmbedUtil.getCannotCreateEmbed(event.getAuthor(), null, null, "Cannot get the game link if there was no game created!")).queue();
			return;
		}

		event.getMessage().reply(EmbedUtil.getLinkEmbed(event.getAuthor(), Main.currentGame.getMessage(), Main.currentGame.getAuthor())).queue();
	}

}
