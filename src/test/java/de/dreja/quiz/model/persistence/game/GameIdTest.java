package de.dreja.quiz.model.persistence.game;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameIdTest {

    @Test
    void test() {
        final var maxShort = GameId.of(Short.MAX_VALUE);
        assertThat(maxShort).isNotNull().hasNoNullFieldsOrProperties();

        final var maxInt = GameId.of(Integer.MAX_VALUE);
        assertThat(maxInt).isNotNull().hasNoNullFieldsOrProperties().isGreaterThan(maxShort);

        final var maxLong = GameId.of(Long.MAX_VALUE);
        assertThat(maxLong).isNotNull().hasNoNullFieldsOrProperties().isGreaterThan(maxInt);

        final var decShort = GameId.of(maxShort.getBase64());
        assertThat(decShort).isNotNull().hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("numerical", (long) Short.MAX_VALUE)
                .isEqualTo(maxShort);

        final var decInt = GameId.of(maxInt.getBase64());
        assertThat(decInt).isNotNull().hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("numerical", (long) Integer.MAX_VALUE)
                .isEqualTo(maxInt);

        final var decLong = GameId.of(maxLong.getBase64());
        assertThat(decLong).isNotNull().hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("numerical", Long.MAX_VALUE)
                .isEqualTo(maxLong);
    }
}
