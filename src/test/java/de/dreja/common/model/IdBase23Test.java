package de.dreja.common.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class IdBase23Test {

    @Test
    void testBase64() {
        final var id = IdBase32.of(1_000_000L);
        assertThat(id).isNotNull().hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("numberValue", 1_000_000L);

        final var fromBase64 = IdBase32.of(id.toString());
        assertThat(fromBase64).isNotNull().hasNoNullFieldsOrProperties().isEqualTo(id)
        .hasFieldOrPropertyWithValue("numberValue", 1_000_000L);
    }
    
}
