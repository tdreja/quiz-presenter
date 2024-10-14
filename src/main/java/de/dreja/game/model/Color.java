package de.dreja.game.model;

import jakarta.annotation.Nullable;

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
