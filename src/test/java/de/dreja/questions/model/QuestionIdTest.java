package de.dreja.questions.model;

import org.junit.jupiter.api.Test;

import static de.dreja.questions.model.Question.buildId;
import static org.assertj.core.api.Assertions.assertThat;

class QuestionIdTest {

    @Test
    void testRegularText() {
        assertThat(buildId("prefix", "Hallo Welt"))
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("prefix-hallo-welt");
    }

    @Test
    void testNumbers() {
        assertThat(buildId("prefix", "Hallo 7"))
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("prefix-hallo-7");
    }

    @Test
    void testGermanUmlauts() {
        assertThat(buildId("prefix", "Mein Äüöß Text"))
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("prefix-mein-aeueoess-text");
    }

    @Test
    void testSpecialCharacters() {
        assertThat(buildId("prefix", "Sonderzeichen ,.,."))
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("prefix-sonderzeichen-_");
    }

    @Test
    void testCombination() {
        assertThat(buildId("prefix", "Führsorge, so daß..."))
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("prefix-fuehrsorge_-so-dass_");
    }
}
