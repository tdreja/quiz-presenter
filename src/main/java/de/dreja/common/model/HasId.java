package de.dreja.common.model;

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
}
