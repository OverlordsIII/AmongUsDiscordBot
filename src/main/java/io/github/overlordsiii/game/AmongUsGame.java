package io.github.overlordsiii.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;

public class AmongUsGame {

	private final long messageId;
	private final User author;

	public AmongUsGame(long messageId, User authorId) {
		this.messageId = messageId;
		this.author = authorId;
	}

	public void getNumberOfPlayerPlaying(MessageChannel channel, Consumer<ReactionPaginationAction> queueAction) {
		channel.retrieveMessageById(messageId)
				.map(message -> Objects.requireNonNull(message.retrieveReactionUsers("\uD83D\uDE80")))
				.queue(queueAction);
	}

	public long getMessageId() {
		return messageId;
	}

	public User getAuthor() {
		return author;
	}
}
