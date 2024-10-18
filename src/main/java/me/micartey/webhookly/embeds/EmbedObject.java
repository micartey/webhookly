package me.micartey.webhookly.embeds;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.Color;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
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
    private String timestamp;

    private Footer footer;
    private Thumbnail thumbnail;
    private Image image;
    private Author author;

    private final List<Field> fields = new ArrayList<>();

    public void setTimestampFromOffsetDateTime(OffsetDateTime offsetDateTime) {
        timestamp = offsetDateTime.toString();
    }

    public void setTimestampFromTemporalAccessor(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Instant) {
            timestamp = OffsetDateTime.ofInstant((Instant) temporalAccessor, Clock.systemDefaultZone().getZone()).toString();
        } else {
            timestamp = OffsetDateTime.from(temporalAccessor).toString();
        }
    }

    public void setTimestampFromTimeInMillis(long timeInMillis) {
        setTimestampFromTemporalAccessor(Instant.ofEpochMilli(timeInMillis));
    }

}