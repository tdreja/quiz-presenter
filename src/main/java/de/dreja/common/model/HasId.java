package de.dreja.common.model;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

import io.micrometer.common.lang.Nullable;
import jakarta.annotation.Nonnull;

public interface HasId<ID extends Comparable<?>> {
    
    @Nonnull
    public ID getId();

    @Nonnull
    static <ID extends Comparable<?>, ITEM extends HasId<ID>> Optional<ITEM> get(@Nullable Map<ID, ITEM> map, @Nullable ID id) {
        if(id == null || map == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(map.get(id));
    }

    static <ID extends Comparable<?>, ITEM extends HasId<ID>> void put(@Nullable Map<ID, ITEM> map, @Nullable ITEM item) {
        if(map == null || item == null) {
            return;
        }
        map.put(item.getId(), item);
    }

    @Nonnull
    static <ID extends Comparable<ID>, ITEM extends HasId<ID>> Comparator<ITEM> compareById() {
        return (i1, i2) -> {
            final ID id1 = i1 == null ? null : i1.getId();
            final ID id2 = i2 == null ? null : i2.getId();
            if(id1 == null) {
                if(id2 == null) {
                    return 0;
                }
                return -1;
            }
            if(id2 == null) {
                return 1;
            }
            return id1.compareTo(id2);
        };
    }
}
