package io.github.overlordsiii.command;

import io.github.overlordsiii.Main;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class RocketReactionCommand {

	@SubscribeEvent
	public void executeRocketCommand(GuildMessageReactionAddEvent event) {

		if (Main.currentGame == null || event.getUser().isBot()) return;

		if (Main.currentGame.getMessage().getIdLong() != event.getMessageIdLong()) return;

		if (event.getReactionEmote().isEmote() || !event.getReactionEmote().isEmoji()) return;

		if (!event.getReactionEmote().getEmoji().equals("\uD83D\uDE80")) return;

		if (Main.currentGame.getPlayingUsers().contains(event.getUser())) {
			event.retrieveMessage().queue(message -> {
				message.removeReaction(event.getReactionEmote().getEmoji(), event.getUser()).queue();
				Main.currentGame.removeUser(event.getUser());
				Main.currentGame.getMessage().editMessage(CreateCommand.createUpdatedEmbed(Main.currentGame.getAuthor(), Main.currentGame.getPlayingUsers())).queue();
			});
			return;
		}


		event.retrieveMessage().queue(message -> {
			message.removeReaction(event.getReactionEmote().getEmoji(), event.getUser()).queue();
			Main.currentGame.addUser(event.getUser());
			Main.currentGame.getMessage().editMessage(CreateCommand.createUpdatedEmbed(Main.currentGame.getAuthor(), Main.currentGame.getPlayingUsers())).queue();
		});
	}
}
