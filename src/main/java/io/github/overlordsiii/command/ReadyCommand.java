package io.github.overlordsiii.command;

import java.util.Date;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class ReadyCommand {


	@SubscribeEvent
	public void onReady(@NotNull ReadyEvent event) {



		System.out.printf("Logged bot in as %s on %s", event.getJDA().getSelfUser().getAsTag(), new Date().toString());
	}

}
