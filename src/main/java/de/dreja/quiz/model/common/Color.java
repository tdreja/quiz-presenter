package de.dreja.quiz.model.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

@Schema(enumAsRef = true)
public enum Color {

    RED,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    PURPLE,
    TURQUOISE,
    WHITE;

    @Nullable
    public static Color fromKey(@Nullable String key) {
        if(key == null) {
            return null;
        }
        for(Color color : Color.values()) {
            if(color.name().equalsIgnoreCase(key)) {
                return color;
            }
        }
        return null;
    }
}
