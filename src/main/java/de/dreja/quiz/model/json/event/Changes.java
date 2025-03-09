package de.dreja.quiz.model.json.event;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

public final class Changes {

    private static final Changes NONE = new Changes(List.of(), List.of(), List.of());

    private final List<Object> created;
    private final List<Object> updated;
    private final List<Object> deleted;


    private Changes(@Nonnull List<Object> created,
                    @Nonnull List<Object> updated,
                    @Nonnull List<Object> deleted) {
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    public boolean noChanges() {
        return created.isEmpty() && updated.isEmpty() && deleted.isEmpty();
    }

    public boolean anyChanges() {
        return !created.isEmpty() || !updated.isEmpty() || !deleted.isEmpty();
    }

    @Nonnull
    public static Changes none() {
        return NONE;
    }

    @Nonnull
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Changes changes = (Changes) o;
        return Objects.equals(created, changes.created)
                && Objects.equals(updated, changes.updated)
                && Objects.equals(deleted, changes.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(created, updated, deleted);
    }

    public static class Builder {

        private List<Object> created;
        private List<Object> updated;
        private List<Object> deleted;

        private Builder() {
            // Ignore
        }

        @Nonnull
        public Builder created(@Nullable Object object) {
            if (object != null) {
                if (created == null) {
                    created = new LinkedList<>();
                }
                created.add(object);
            }
            return this;
        }

        @Nonnull
        public Builder updated(@Nullable Object object) {
            if (object != null) {
                if (updated == null) {
                    updated = new LinkedList<>();
                }
                updated.add(object);
            }
            return this;
        }

        @Nonnull
        public Builder deleted(@Nullable Object object) {
            if (object != null) {
                if (deleted == null) {
                    deleted = new LinkedList<>();
                }
                deleted.add(object);
            }
            return this;
        }

        @Nonnull
        public Changes build() {
            return new Changes(created == null ? List.of() : unmodifiableList(created),
                    updated == null ? List.of() : unmodifiableList(updated),
                    deleted == null ? List.of() : unmodifiableList(deleted));
        }
    }
}
