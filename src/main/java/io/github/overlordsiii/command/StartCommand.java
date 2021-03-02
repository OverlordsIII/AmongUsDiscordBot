package io.github.overlordsiii.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import io.github.overlordsiii.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.requests.RestAction;

public class StartCommand  {

	private static final Random RANDOM = new Random();


	@SubscribeEvent
	public void execute(MessageReceivedEvent event) {

		if (Main.currentGame == null) return;

		MessageChannel channel = event.getChannel();

		Message message = event.getMessage();

		if (!message.getContentRaw().equalsIgnoreCase("!start")) return;

		channel.sendMessage("Dming Users...").queue();

		Main.currentGame.getNumberOfPlayerPlaying(channel, action -> {

			List<User> list = new ArrayList<>();

			action.forEachAsync(value -> {
				if (!value.isBot()) list.add(value);
				return true;
			}).thenRun(() -> {
				filterAndDmImposters(list, channel);
				list.forEach(user -> sendPrivateMessage(user, createCrewmateEmbedSpec(user)));
			});


		});


		Main.currentGame = null;


	}

	private static void filterAndDmImposters(List<User> users, MessageChannel channel) {

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
