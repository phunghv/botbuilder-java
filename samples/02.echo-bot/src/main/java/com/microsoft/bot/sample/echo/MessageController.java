package com.microsoft.bot.sample.echo;

import com.microsoft.bot.integration.BotFrameworkHttpAdapter;
import com.microsoft.bot.integration.Configuration;
import com.microsoft.bot.schema.ConversationReference;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class MessageController {
    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final BotFrameworkHttpAdapter adapter;
    private ConversationReferences conversationReferences;
    private String appId;


    public MessageController(BotFrameworkHttpAdapter withAdapter, Configuration withConfiguration, ConversationReferences withReferences) {
        adapter = withAdapter;
        this.conversationReferences = withReferences;
        appId = withConfiguration.getProperty("MicrosoftAppId");
        if (StringUtils.isEmpty(appId)) {
            appId = UUID.randomUUID().toString();
        }
    }

    @PostMapping("/api/send")
    public CompletableFuture<ResponseEntity<Object>> incoming(@RequestBody Message msg) {
        String message = Optional.ofNullable(msg).map(Message::getMessage).orElse("---");
        logger.info("send : {}", message);
        return CompletableFuture.supplyAsync(() -> {
            try {
                for (ConversationReference reference : conversationReferences.values()) {
                    adapter.continueConversation(appId, reference, turnContext -> turnContext.sendActivity(message)
                            .thenApply(resourceResponse -> null));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>("success", HttpStatus.OK);
        });
    }
}
