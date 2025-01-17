// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.bot.sample.echo;

import com.microsoft.bot.integration.AdapterWithErrorHandler;
import com.microsoft.bot.integration.BotFrameworkHttpAdapter;
import com.microsoft.bot.integration.Configuration;
import com.microsoft.bot.integration.spring.BotController;
import com.microsoft.bot.integration.spring.BotDependencyConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * This is the starting point of the Sprint Boot Bot application.
 * <p>
 * This class also provides overrides for dependency injections.  A class that extends the
 * {@link com.microsoft.bot.builder.Bot} interface should be annotated with @Component.
 *
 * @see EchoBot
 */
@SpringBootApplication

// Use the default BotController to receive incoming Channel messages. A custom controller
// could be used by eliminating this import and creating a new RestController.  The default
// controller is created by the Spring Boot container using dependency injection.  The
// default route is /api/messages.
@Import({BotController.class, MessageController.class})
public class Application extends BotDependencyConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Returns a custom Adapter that provides error handling.
     *
     * @param configuration The Configuration object to use.
     * @return An error handling BotFrameworkHttpAdapter.
     */
    @Override
    public BotFrameworkHttpAdapter getBotFrameworkHttpAdaptor(Configuration configuration) {
        return new AdapterWithErrorHandler(configuration);
    }

    @Bean
    public ConversationReferences getConversationReferences() {
        return new ConversationReferences();
    }
}
