package io.github.overlordsiii;

import java.util.function.Function;

import javax.security.auth.login.LoginException;

import io.github.overlordsiii.command.CreateCommand;
import io.github.overlordsiii.command.MessageLinkCommand;
import io.github.overlordsiii.command.ReadyCommand;
import io.github.overlordsiii.command.RocketReactionCommand;
import io.github.overlordsiii.command.StartCommand;
import io.github.overlordsiii.command.TestCommand;
import io.github.overlordsiii.config.PropertiesHandler;
import io.github.overlordsiii.game.AmongUsGame;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

public class Main {


	public static final PropertiesHandler TOKEN = PropertiesHandler.builder()
		.setFileName("token.properties")
		.addConfigOption("token", "")
		.build();

	public static final PropertiesHandler CONFIG = PropertiesHandler.builder()
		.setFileName("amongus.properties")
		.addConfigOption("imposters", "1")
		.addConfigOption("status", OnlineStatus.ONLINE.toString())
		.addConfigOption("activityType", Activity.ActivityType.COMPETING.toString())
		.addConfigOption("activityText", "Among Us Automatic Chooser")
		.build();


	public static AmongUsGame currentGame;

	public static void main(String[] args) throws LoginException {
		try {
			JDA builder = JDABuilder
				.createDefault(TOKEN.getConfigOption("token", Function.identity()))
				.setEventManager(new AnnotatedEventManager())
				.addEventListeners(new MessageLinkCommand())
				.addEventListeners(new ReadyCommand())
				.addEventListeners(new CreateCommand())
				.addEventListeners(new StartCommand())
				.addEventListeners(new RocketReactionCommand())
				.setActivity(Activity.of(CONFIG.getConfigOption("activityType", Activity.ActivityType::valueOf), CONFIG.getConfigOption("activityText", Function.identity())))
				.setStatus(CONFIG.getConfigOption("status", OnlineStatus::valueOf))
				.build();
		} catch (LoginException e) {
			JDA builder = JDABuilder
				.createDefault(System.getenv("TOKEN"))
				.setEventManager(new AnnotatedEventManager())
				.addEventListeners(new MessageLinkCommand())
				.addEventListeners(new ReadyCommand())
				.addEventListeners(new CreateCommand())
				.addEventListeners(new StartCommand())
				.addEventListeners(new RocketReactionCommand())
				.setActivity(Activity.of(CONFIG.getConfigOption("activityType", Activity.ActivityType::valueOf), CONFIG.getConfigOption("activityText", Function.identity())))
				.setStatus(CONFIG.getConfigOption("status", OnlineStatus::valueOf))
				.build();
		}
	}
}
