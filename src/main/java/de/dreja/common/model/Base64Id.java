package de.dreja.common.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

import static de.dreja.common.model.Base64Id.IdPart.YEAR;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class Base64Id implements Comparable<Base64Id>{

    static enum IdPart {

        YEAR(2),
        MONTH(2),
        DAY(2),
        INDEX(10);

        private final BigInteger factor;

        IdPart(int maxLength) {
            this.factor = BigInteger.valueOf(10L).pow(maxLength);
        }
    }

    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();
    
    private final LocalDate date;
    private final int index;
    private final String base64;

    
    Base64Id(@Nonnull LocalDate date, int index, @Nonnull String base64) {
        this.date = date;
        this.index = index;
        this.base64 = base64;
    }

    @Nonnull
    public static Base64Id of(@Nonnull LocalDate date, int index) {
        final BigInteger bigInt = asBigInt(Map.of(
            IdPart.YEAR, date.getYear(), 
            IdPart.MONTH, date.getMonthValue(), 
            IdPart.DAY, date.getDayOfMonth(),
            IdPart.INDEX, index));
        return new Base64Id(date, index, ENCODER.encodeToString(bigInt.toByteArray()));
    }

    @Nonnull
    static BigInteger asBigInt(@Nonnull Map<IdPart, Integer> parts) {
        BigInteger factor = BigInteger.ONE;
        BigInteger result = BigInteger.ZERO;
        for(IdPart part : IdPart.values()) {
            BigInteger partValue = BigInteger.valueOf(parts.getOrDefault(part, 1));
            if(part == YEAR) {
                partValue = partValue.mod(BigInteger.valueOf(100));
            }
            result = result.add(partValue.multiply(factor));
            factor = factor.multiply(part.factor);
        }
        return result;
    }

    @Nonnull
    static Map<IdPart,Integer> fromBigInt(final @Nonnull BigInteger bigInteger) {
        final Map<IdPart, Integer> parts = new EnumMap<>(IdPart.class);
        BigInteger rest = bigInteger;
        for(IdPart part : IdPart.values()) {
            int partValue = rest.mod(part.factor).intValue();
            if(part == YEAR) {
                partValue += 2000;
            }
            parts.put(part, partValue);
            rest = rest.divide(part.factor);
        }
        return parts;
    }

    @Nonnull
    public static Base64Id of(@Nullable String base64) {
        final Map<IdPart, Integer> parts = fromBigInt(base64 == null ? BigInteger.ZERO : new BigInteger(DECODER.decode(base64)));
        return new Base64Id(LocalDate.of(
                parts.getOrDefault(IdPart.YEAR, 2000), 
                parts.getOrDefault(IdPart.MONTH, 1), 
                parts.getOrDefault(IdPart.DAY, 1)), 
            parts.getOrDefault(IdPart.INDEX, 1), 
            base64);
    }

    @Nonnull
    public LocalDate getDate() {
        return date;
    }


    public int getIndex() {
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
        return Integer.compare(index, o.index);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + index;
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
