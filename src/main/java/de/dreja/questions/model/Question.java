package de.dreja.questions.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.dreja.common.model.HasId;
import jakarta.annotation.Nonnull;

import java.util.Locale;
import java.util.regex.Pattern;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @Type(value = MultipleChoiceQuestion.class, name = MultipleChoiceQuestion.TYPE_NAME),
        @Type(value = EstimateQuestion.class, name = EstimateQuestion.TYPE_NAME)
})
public interface Question extends HasId<String> {

    Pattern SPACES = Pattern.compile("[\s]+");
    Pattern NOT_LETTERS_OR_NUMBERS = Pattern.compile("[^a-z0-9-]+");

    int getDifficulty();


    @Nonnull
    static String buildId(final @Nonnull String typeName, final @Nonnull String text) {
        String clean = SPACES.matcher(text).replaceAll("-");
        clean = clean.toLowerCase(Locale.GERMAN);
        clean = clean.replace("ö", "oe")
                .replace("ü", "ue")
                .replace("ä", "ae")
                .replace("ß", "ss");
        clean = NOT_LETTERS_OR_NUMBERS.matcher(clean).replaceAll("_");
        return typeName + "-" + clean;
    }
}
