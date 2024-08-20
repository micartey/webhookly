package me.micartey.webhookly.embeds;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Author {

    private final String name;
    private final String url;
    private final String iconUrl;

}
