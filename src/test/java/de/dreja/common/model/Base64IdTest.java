package de.dreja.common.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import static de.dreja.common.model.Base64Id.IdPart.DAY;
import static de.dreja.common.model.Base64Id.IdPart.INDEX;
import static de.dreja.common.model.Base64Id.IdPart.MONTH;
import static de.dreja.common.model.Base64Id.IdPart.YEAR;

public class Base64IdTest {
    
    @Test
    void testToBigInt() {
        final BigInteger bigInt = BigInteger.valueOf(123080924L);
        assertThat(Base64Id.fromBigInt(bigInt)).isNotNull().isNotEmpty().hasSize(4)
            .containsAllEntriesOf(Map.of(YEAR, 2024, MONTH, 9, DAY, 8, INDEX, 123));
    }

    @Test
    void testFromBigInt() {
        assertThat(Base64Id.asBigInt(Map.of(YEAR, 2024, MONTH, 9, DAY, 8, INDEX, 123)))
            .isNotNull().isEqualTo(123080924L);
    }

    @Test
    void testBase64() {
        final var id = Base64Id.of(LocalDate.of(2024, 9, 8), 123);
        assertThat(id).isNotNull().hasNoNullFieldsOrProperties();

        final var fromBase64 = Base64Id.of(id.getBase64());
        assertThat(fromBase64).isNotNull().hasNoNullFieldsOrProperties().isEqualTo(id);
    }
    
}
