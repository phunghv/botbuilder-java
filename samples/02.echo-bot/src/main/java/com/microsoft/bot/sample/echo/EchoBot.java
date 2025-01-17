// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.bot.sample.echo;

import com.codepoetics.protonpack.collectors.CompletableFutures;
import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ChannelAccount;
import com.microsoft.bot.schema.ConversationReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class implements the functionality of the Bot.
 *
 * <p>This is where application specific logic for interacting with the users would be
 * added.  For this sample, the {@link #onMessageActivity(TurnContext)} echos the text
 * back to the user.  The {@link #onMembersAdded(List, TurnContext)} will send a greeting
 * to new conversation participants.</p>
 */
@Component
@Slf4j
public class EchoBot extends ActivityHandler {
    private ConversationReferences conversationReferences;

    public EchoBot(ConversationReferences withReferences) {
        conversationReferences = withReferences;
    }

    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
        addConversationReference(turnContext.getActivity());
        return turnContext
                .sendActivity(MessageFactory.text("Echo: " + turnContext.getActivity().getText()))
                .thenApply(sendResult -> null);
    }

    @Override
    protected CompletableFuture<Void> onConversationUpdateActivity(TurnContext turnContext) {
        addConversationReference(turnContext.getActivity());
        return super.onConversationUpdateActivity(turnContext);
    }

    private void addConversationReference(Activity activity) {
        ConversationReference conversationReference = activity.getConversationReference();
        conversationReferences.put(conversationReference.getUser().getId(), conversationReference);
    }

    @Override
    protected CompletableFuture<Void> onEventActivity(TurnContext turnContext) {
        log.info("onEventActivity");
        return super.onEventActivity(turnContext);
    }

    @Override
    protected CompletableFuture<Void> onUnrecognizedActivityType(TurnContext turnContext) {
        log.info("onUnrecognizedActivityType");
        addConversationReference(turnContext.getActivity());
        return super.onUnrecognizedActivityType(turnContext);
    }

    @Override
    protected CompletableFuture<Void> onMembersAdded(List<ChannelAccount> membersAdded, TurnContext turnContext) {
        addConversationReference(turnContext.getActivity());
        return membersAdded.stream()
                .filter(member -> !StringUtils.equals(member.getId(), turnContext.getActivity().getRecipient().getId()))
                .map(channel -> turnContext.sendActivity(MessageFactory.text("Hello and welcome!")))
                .collect(CompletableFutures.toFutureList())
                .thenApply(resourceResponses -> null);
    }
}
