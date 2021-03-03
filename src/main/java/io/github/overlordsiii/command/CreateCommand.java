package io.github.overlordsiii.command;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;

import io.github.overlordsiii.Main;
import io.github.overlordsiii.game.AmongUsGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class CreateCommand {




	@SubscribeEvent
	public void execute(MessageReceivedEvent event) {

		if (event.getAuthor().isBot()) return;

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (!content.equals("!create")) return;

		if (Main.currentGame != null) {
			message.reply("Cannot create a game if there is already one in progress! (Created by " + Main.currentGame.getAuthor().getAsMention() + ". Do !messagelink for the link to the message)").queue();
			return;
		}

		MessageChannel channel = event.getChannel();


		channel.sendMessage(createEmbed(message.getAuthor())).queue(embedMessage -> {

			Main.currentGame = new AmongUsGame(embedMessage, message.getAuthor());


			embedMessage.addReaction("\uD83D\uDE80").queue();

			Main.currentGame.addUser(event.getAuthor());
			Main.currentGame.getMessage().editMessage(CreateCommand.createUpdatedEmbed(Main.currentGame.getAuthor(), Main.currentGame.getPlayingUsers())).queue();
		});
		/*
		action
			.map(CreateCommand::addRocket)
			.queue();
 */
	}

	private static MessageEmbed createEmbed(User executor) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(executor.getName(), executor.getAvatarUrl(), executor.getAvatarUrl());
		builder.setColor(Color.GREEN);
		builder.setTitle("Start a game of among us!");
		builder.addField("How to Participate", "React to this message with the reaction to indicate you are playing this game.", false);
		builder.addField("How to Start", "Then run the command !start to begin!", false);
		return builder.build();
	}

	public static MessageEmbed createUpdatedEmbed(User executor, List<User> participants) {

		if (participants.isEmpty()) {
			return createEmbed(executor);
		}

		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(executor.getName(), executor.getAvatarUrl(), executor.getAvatarUrl());
		builder.setColor(Color.GREEN);
		builder.setTitle("Start a game of among us!");
		builder.addField("How to Participate", "React to this message with the reaction to indicate you are playing this game.", false);
		builder.addField("How to Start", "Then run the command !start to begin!", false);
		StringBuilder stringBuilder = new StringBuilder();
		participants.forEach(user -> {
			stringBuilder.append(user.getAsMention());
			stringBuilder.append("\n");
		});
		builder.addField("Participants", stringBuilder.toString(), false);
		return builder.build();
	}

}
