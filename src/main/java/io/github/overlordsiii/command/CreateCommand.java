package io.github.overlordsiii.command;

import java.awt.Color;
import java.util.List;

import io.github.overlordsiii.Main;
import io.github.overlordsiii.game.AmongUsGame;
import io.github.overlordsiii.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class CreateCommand {




	@SubscribeEvent
	public void execute(MessageReceivedEvent event) {

		if (event.getAuthor().isBot()) return;

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (!content.equals("!create")) return;

		if (Main.currentGame != null) {
			message
				.reply(EmbedUtil.getCannotCreateEmbed(event.getAuthor(), Main.currentGame.getAuthor(), Main.currentGame.getMessage(),
					"You can't start a game if there is already one in progress!"))
				.mentionRepliedUser(false)
				.queue();
			return;
		}

		MessageChannel channel = event.getChannel();


		channel.sendMessage(EmbedUtil.createDefaultEmbed(message.getAuthor()).build()).queue(embedMessage -> {

			Main.currentGame = new AmongUsGame(embedMessage, message.getAuthor());


			embedMessage.addReaction("\uD83D\uDE80").queue();

			Main.currentGame.addUser(event.getAuthor());
			Main.currentGame.getMessage().editMessage(EmbedUtil.createUpdatedEmbed(EmbedUtil.createDefaultEmbed(Main.currentGame.getAuthor()), Main.currentGame.getPlayingUsers())).queue();
		});
	}



}
