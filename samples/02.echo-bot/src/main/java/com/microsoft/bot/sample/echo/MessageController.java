package com.microsoft.bot.sample.echo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class MessageController {
    @GetMapping("/api/send")
    public CompletableFuture<ResponseEntity<Object>> incoming() {
        return CompletableFuture.supplyAsync(() -> {
            return new ResponseEntity<>("sucessss", HttpStatus.OK);
        });
    }
}
