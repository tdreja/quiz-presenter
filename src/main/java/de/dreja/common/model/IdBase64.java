package de.dreja.common.model;

import java.math.BigInteger;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class IdBase64 extends Number implements Comparable<IdBase64> {

    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

    private final long numberValue;
    private final String base64Value;

    private IdBase64(@Nonnull String base64Value, long numberValue) {
        this.base64Value = base64Value;
        this.numberValue = numberValue;
    }

    @Nonnull
    public static IdBase64 of(long value) {
        return new IdBase64(ENCODER.encodeToString(BigInteger.valueOf(value).toByteArray()), value);
    }

    @Nonnull
    @JsonCreator
    public static IdBase64 of(@Nullable String base64) {
        if(base64 == null) {
            return of(1L);
        }
        try {
            return new IdBase64(base64, new BigInteger(DECODER.decode(base64)).longValue());
        } catch (IllegalArgumentException ex) {
            return of(1L);
        }
    }

    @Override
    public int compareTo(@Nonnull IdBase64 o) {
        return Long.compare(numberValue, o.numberValue);
    }

    @Override
    @JsonIgnore
    public int intValue() {
        return (int) numberValue;
    }

    @Override
    @JsonIgnore
    public long longValue() {
        return numberValue;
    }

    @Override
    @JsonIgnore
    public float floatValue() {
        return numberValue;
    }

    @Override
    @JsonIgnore
    public double doubleValue() {
        return numberValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return base64Value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(numberValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IdBase64 other = (IdBase64) obj;
        return numberValue == other.numberValue;
    }
    
    @Nonnull
    public IdBase64 next() {
        return IdBase64.of(numberValue+1);
    }
}
