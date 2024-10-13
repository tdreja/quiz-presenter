package de.dreja.common.model;

import java.math.BigInteger;

import org.apache.commons.codec.binary.Base32;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class IdBase32 extends Number implements Comparable<IdBase32> {

    private static final Base32 base32 = Base32.builder().setLineLength(0).get();

    private final long numberValue;
    private final String base64Value;

    private IdBase32(@Nonnull String base64Value, long numberValue) {
        this.base64Value = base64Value;
        this.numberValue = numberValue;
    }

    @Nonnull
    public static IdBase32 of(long value) {
        return new IdBase32(base32.encodeAsString(BigInteger.valueOf(value).toByteArray()).replace("=", ""), value);
    }

    @Nonnull
    @JsonCreator
    public static IdBase32 of(@Nullable String base64) {
        if(base64 == null) {
            return of(1L);
        }
        try {
            return new IdBase32(base64, new BigInteger(base32.decode(base64)).longValue());
        } catch (IllegalArgumentException ex) {
            return of(1L);
        }
    }

    @Override
    public int compareTo(@Nonnull IdBase32 o) {
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
        IdBase32 other = (IdBase32) obj;
        return numberValue == other.numberValue;
    }
    
    @Nonnull
    public IdBase32 next() {
        return IdBase32.of(numberValue+1);
    }
}
