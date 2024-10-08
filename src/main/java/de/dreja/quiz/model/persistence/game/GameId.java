package de.dreja.quiz.model.persistence.game;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Objects;

public final class GameId extends Number implements Comparable<GameId> {

    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder decoder = Base64.getUrlDecoder();

    private final long numerical;
    private final String base64;

    private GameId(long numerical, @Nonnull String base64) {
        this.numerical = numerical;
        this.base64 = Objects.requireNonNull(base64);
    }

    @Nonnull
    public static GameId of(@Nullable String base64) {
        return new GameId(asNumber(base64), base64 == null ? "" : base64);
    }

    private static long asNumber(@Nullable String input) {
        final String raw = input == null ? "" : input;
        final byte[] bytes = decoder.decode(raw);
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return switch (bytes.length) {
            case Short.BYTES -> buffer.asShortBuffer().get();
            case Integer.BYTES -> buffer.asIntBuffer().get();
            case Long.BYTES -> buffer.asLongBuffer().get();
            default -> 0L;
        };
    }

    @Nonnull
    public static GameId of(long numerical) {
        final ByteBuffer buffer;
        if(Math.abs(numerical) <= Short.MAX_VALUE) {
            buffer = ByteBuffer.allocate(Short.BYTES);
            buffer.putShort((short) numerical);
        } else if(Math.abs(numerical) <= Integer.MAX_VALUE) {
            buffer = ByteBuffer.allocate(Integer.BYTES);
            buffer.putInt((int) numerical);
        } else {
            buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.putLong(numerical);
        }
        return new GameId(numerical, encoder.encodeToString(buffer.array()));
    }

    public long getNumerical() {
        return numerical;
    }

    @Nonnull
    public String getBase64() {
        return base64;
    }

    @Override
    public int compareTo(@Nonnull GameId o) {
        return Long.compare(numerical, o.numerical);
    }

    @Override
    public int intValue() {
        return (int) numerical;
    }

    @Override
    public long longValue() {
        return numerical;
    }

    @Override
    public float floatValue() {
        return numerical;
    }

    @Override
    public double doubleValue() {
        return numerical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameId gameId = (GameId) o;
        return numerical == gameId.numerical;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numerical);
    }

    @Override
    public String toString() {
        return base64;
    }
}
