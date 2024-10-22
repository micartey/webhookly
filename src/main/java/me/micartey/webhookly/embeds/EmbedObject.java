package me.micartey.webhookly.embeds;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class EmbedObject {

    private String title;
    private String description;
    private String url;
    private Color color;
    private OffsetDateTime timestamp;

    private Footer footer;
    private Thumbnail thumbnail;
    private Image image;
    private Author author;

    private final List<Field> fields = new ArrayList<>();

}