package de.dreja.quiz.model.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@Schema(enumAsRef = true)
public enum Emoji {

    DUCK("🦆", "&#x1F986"),
    SHARK("🦈", "&#x1F988"),
    CAMEL("🐫", "&#x1F42B"),
    FLAMINGO("🦩", "&#x1F9A9"),
    HEDGEHOG("🦔", "&#x1F994"),
    CAT("🐈", "&#x1F408"),
    CRAB("🦀", "&#x1F980"),
    KANGAROO("🦘", "&#x1F998"),
    HIPPOPOTAMUS("🦛", "&#x1F99B"),
    GORILLA("🦍", "&#x1F98D"),
    TIGER("🐅", "&#x1F405"),
    LEOPARD("🐆", "&#x1F406"),
    LAMA("🦙", "&#x1F999"),
    RHINOCEROS("🦏", "&#x1F98F"),
    HORSE("🐎", "&#x1F40E"),
    SLOTH("🦥", "&#x1F9A5"),
    ELEFANT("🐘", "&#x1F418"),
    MOUSE("🐁", "&#x1F401"),
    RABBIT("🐇", "&#x1F407"),
    BEAVER("🦫", "&#x1F9AB"),
    CROCODILE("🐊", "&#x1F40A"),
    TORTOISE("🐢", "&#x1F422"),
    SNAKE("🐍", "&#x1F40D"),
    DOLPHIN("🐬", "&#x1F42C"),
    OTTER("🦦", "&#x1F9A6"),
    WHALE("🐳", "&#x1F433"),
    ROOSTER("🐓", "&#x1F413"),
    EAGLE("🦅", "&#x1F985"),
    SWAN("🦢", "&#x1F9A2"),
    PARROT("🦜", "&#x1F99C"),
    PENGUIN("🐧", "&#x1F427"),
    DOG("🐕", "&#x1F415");

    private final String emoji;

    private final String html;

    Emoji(String emoji, String html) {
        this.emoji = emoji;
        this.html = html;
    }

    @Nullable
    public static Emoji fromKey(@Nullable String key) {
        if (key == null) {
            return null;
        }
        for (Emoji emoji : Emoji.values()) {
            if (emoji.name().equalsIgnoreCase(key)) {
                return emoji;
            }
        }
        return null;
    }

    @Nonnull
    public String getEmoji() {
        return emoji;
    }

    @Nonnull
    public String getHtml() {
        return html;
    }
}
