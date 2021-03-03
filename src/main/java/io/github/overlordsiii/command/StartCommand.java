package io.github.overlordsiii.command;

import java.awt.Color;
import java.util.List;
import java.util.Random;

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

public class StartCommand  {

	private static final Random RANDOM = new Random();


	@SubscribeEvent
	public void execute(MessageReceivedEvent event) {

		MessageChannel channel = event.getChannel();

		Message message = event.getMessage();

		if (!message.getContentRaw().equalsIgnoreCase("!start")) {
			return;
		}
		//Cannot start a game if there was no game created!
		if (Main.currentGame == null) {
			message.reply(EmbedUtil.getCannotCreateEmbed(event.getAuthor(), null, null, "Cannot start a game if there was no game created!")).queue();
			return;
		}

		if (Main.currentGame.getAuthor().getIdLong() != message.getAuthor().getIdLong()) {
			message.reply(EmbedUtil.getCannotCreateEmbed(event.getAuthor(), Main.currentGame.getAuthor(), Main.currentGame.getMessage(), "You cannot start the game since you are not the author!")).queue();
			return;
		}

		channel.sendMessage("Dming Users...").queue();

		List<User> playing = Main.currentGame.getPlayingUsers();

		filterAndDmImposters(playing);
		playing.forEach(user -> sendPrivateMessage(user, EmbedUtil.createCrewmateEmbedSpec(user)));


		Main.currentGame = null;


	}

	private static void filterAndDmImposters(List<User> users) {

		int imposters = Main.CONFIG.getConfigOption("imposters", Integer::parseInt);


		for (int i = 0; i < imposters; i++) {
			int random = RANDOM.nextInt(users.size());
			User imposter = users.get(random);
			sendPrivateMessage(imposter, EmbedUtil.createImposterEmbedSpec(imposter));
			users.remove(random);
		}

	}


	public static void sendPrivateMessage(User user, MessageEmbed content) {
		user
			.openPrivateChannel()
			.submit()
			.thenCompose(channel -> channel.sendMessage(content).submit())
			.whenComplete((message, throwable) -> {
				if (throwable != null) throwable.printStackTrace();
			});

	}



}
