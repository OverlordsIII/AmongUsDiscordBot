package io.github.overlordsiii.command;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import io.github.overlordsiii.Main;
import io.github.overlordsiii.game.AmongUsGame;
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

		if (Main.currentGame == null) {
			message.reply("Cannot start a game if there was no game created! (Hint: do !create first and have your friends react to the rocket)").queue(mes -> mes.suppressEmbeds(true).queue());
			return;
		}

		if (Main.currentGame.getAuthor().getIdLong() != message.getAuthor().getIdLong()) {
			message.reply("You cannot start the game since you are not the author! (The Author is " + Main.currentGame.getAuthor().getAsMention() + ". Do !messagelink for the link to the game message.)").queue();
			return;
		}

		channel.sendMessage("Dming Users...").queue();

		List<User> playing = Main.currentGame.getPlayingUsers();

		filterAndDmImposters(playing);
		playing.forEach(user -> sendPrivateMessage(user, createCrewmateEmbedSpec(user)));


		Main.currentGame = null;


	}

	private static void filterAndDmImposters(List<User> users) {

		int imposters = Main.CONFIG.getConfigOption("imposters", Integer::parseInt);


		for (int i = 0; i < imposters; i++) {
			int random = RANDOM.nextInt(users.size());
			User imposter = users.get(random);
			sendPrivateMessage(imposter, createImposterEmbedSpec(imposter));
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


	private static MessageEmbed createCrewmateEmbedSpec(User participant) {

		EmbedBuilder builder = new EmbedBuilder();

		//builder.setImage("https://micky.com.au/wp-content/uploads/2020/10/fb-innersloth-among-us-crewmates.jpg");
		builder.setColor(Color.CYAN);
		builder.setTitle("You are a Crewmate!");
		builder.addField("Your Job", "You are to find the imposter and win the bedwars game. Try not to die and win! If you think you see the imposter, vote them out at the gen upgrade", false);
		builder.setAuthor(participant.getName(), participant.getAvatarUrl(), participant.getAvatarUrl());

		return builder.build();
	}

	private static MessageEmbed createImposterEmbedSpec(User participant) {

		EmbedBuilder builder = new EmbedBuilder();

		//builder.setImage("https://th.bing.com/th/id/OIP.dk5-h6vwUeznfH_-dDsivAHaEK?pid=ImgDet&rs=1");
		builder.setColor(Color.RED);
		builder.setTitle("You are the Imposter!");
		builder.addField("Your Job", "You are to team grief your bedwars team without being noticed. If you are noticed, the crewmates can vote you out and possibly win the game", false);
		builder.setAuthor(participant.getName(), participant.getAvatarUrl(), participant.getAvatarUrl());

		return builder.build();
	}
}
