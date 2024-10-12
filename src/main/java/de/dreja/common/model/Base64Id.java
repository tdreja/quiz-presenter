package de.dreja.common.model;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class Base64Id implements Comparable<Base64Id>{

    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();
    
    private final LocalDate date;
    private final long index;
    private final String base64;

    
    Base64Id(@Nonnull LocalDate date, long index, @Nonnull String base64) {
        this.date = date;
        this.index = index;
        this.base64 = base64;
    }

    @Nonnull
    public static Base64Id of(@Nonnull LocalDate date, long index) {
        final String raw = "%04d-%02d-%02d-%d".formatted(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth(), index);
        return new Base64Id(date, index, ENCODER.encodeToString(raw.getBytes(StandardCharsets.UTF_8)));
    }

    @Nonnull
    public static Base64Id of(@Nullable String base64) {
        final String decoded = base64 == null ? "" : new String(DECODER.decode(base64), StandardCharsets.UTF_8);
        final String[] split = decoded.split("-");
        if(split.length != 4) {
            return new Base64Id(LocalDate.MIN, 0, base64);
        }
        try {
            final int year = Integer.parseInt(split[0]);
            final int month = Integer.parseInt(split[1]);
            final int day = Integer.parseInt(split[2]);
            final long index = Long.parseLong(split[3]);
            return new Base64Id(LocalDate.of(year, month, day), index, base64);
        } catch (NumberFormatException e) {
            return new Base64Id(LocalDate.MIN, 0, base64);
        }
    }

    @Nonnull
    public LocalDate getDate() {
        return date;
    }


    public long getIndex() {
        return index;
    }

    @Nonnull
    public String getBase64() {
        return base64;
    }

    @Override
    public int compareTo(@Nonnull Base64Id o) {
        final int cmp = date.compareTo(o.date);
        if(cmp != 0) {
            return cmp;
        }
        return Long.compare(index, o.index);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + (int) (index ^ (index >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Base64Id other = (Base64Id) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        return index == other.index;
    }

    @Override
    public String toString() {
        return base64;
    }
    
}
