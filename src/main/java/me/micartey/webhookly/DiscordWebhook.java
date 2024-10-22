package me.micartey.webhookly;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.micartey.webhookly.embeds.EmbedObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Setter
@RequiredArgsConstructor
public class DiscordWebhook {

    @Getter
    private final List<EmbedObject> embeds = new ArrayList<>();

    private final String url;

    private String content, username, avatarUrl;
    private boolean tts;

    @SuppressWarnings("unused")
    public void execute() throws IOException {
        JSONObject json = new JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        embeds: {
            if (this.embeds.isEmpty())
                break embeds;

            List<JSONObject> embedObjects = new ArrayList<>();

            for (EmbedObject embed : this.embeds) {
                JSONObject jsonEmbed = new JSONObject();

                jsonEmbed.put("title", embed.getTitle());
                jsonEmbed.put("description", embed.getDescription());
                jsonEmbed.put("url", embed.getUrl());

                Optional.ofNullable(embed.getColor()).ifPresent(color -> {
                    int rgb = color.getRed();
                    rgb = (rgb << 8) + color.getGreen();
                    rgb = (rgb << 8) + color.getBlue();

                    jsonEmbed.put("color", rgb);
                });

                Optional.ofNullable(embed.getTimestamp()).ifPresent(timestamp -> {
                    String isoTimestamp = timestamp.toString();

                    jsonEmbed.put("timestamp", isoTimestamp);
                });

                Optional.ofNullable(embed.getFooter()).ifPresent(footer -> {
                    JSONObject jsonFooter = new JSONObject();

                    jsonFooter.put("text", footer.getText());
                    jsonFooter.put("icon_url", footer.getIconUrl());
                    jsonEmbed.put("footer", jsonFooter);
                });

                Optional.ofNullable(embed.getImage()).ifPresent(image -> {
                    JSONObject jsonImage = new JSONObject();

                    jsonImage.put("url", image.getUrl());
                    jsonEmbed.put("image", jsonImage);
                });

                Optional.ofNullable(embed.getThumbnail()).ifPresent(thumbnail -> {
                    JSONObject jsonThumbnail = new JSONObject();

                    jsonThumbnail.put("url", thumbnail.getUrl());
                    jsonEmbed.put("thumbnail", jsonThumbnail);
                });

                Optional.ofNullable(embed.getAuthor()).ifPresent(author -> {
                    JSONObject jsonAuthor = new JSONObject();

                    jsonAuthor.put("name", author.getName());
                    jsonAuthor.put("url", author.getUrl());
                    jsonAuthor.put("icon_url", author.getIconUrl());
                    jsonEmbed.put("author", jsonAuthor);
                });


                List<JSONObject> jsonFields = new ArrayList<>();

                Optional.ofNullable(embed.getFields()).ifPresent(fields -> {
                    fields.forEach(field -> {
                        JSONObject jsonField = new JSONObject();

                        jsonField.put("name", field.getName());
                        jsonField.put("value", field.getValue());
                        jsonField.put("inline", field.isInline());

                        jsonFields.add(jsonField);
                    });
                });

                jsonEmbed.put("fields", jsonFields.toArray());
                embedObjects.add(jsonEmbed);
            }

            json.put("embeds", embedObjects.toArray());
        }

        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "webhookly");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }
}