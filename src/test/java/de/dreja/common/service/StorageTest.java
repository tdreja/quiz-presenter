package de.dreja.common.service;

import java.nio.file.Paths;
import java.util.NavigableSet;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dreja.common.model.Base64Id;
import de.dreja.common.model.DbRoot;

class StorageTest {

    private final NavigableSet<Base64Id> ids = new TreeSet<>();
    private EmbeddedStorageManager storage;

    @BeforeEach
    void setup() {
        storage = EmbeddedStorage.start(null, Paths.get("./build/test.bin"));
    }

    @Test
    void round1() {
        runTest();
    }

    @Test
    void round2() {
        runTest();
    }

    @Test
    void round3() {
        runTest();
    }

    private void runTest() {
        assertThat(storage).isNotNull();
        if(storage.root() == null) {
            storage.setRoot(new DbRoot());
        } else {
            assertThat(storage.root()).isNotNull().isInstanceOf(DbRoot.class);
        }
        final DbRoot root = (DbRoot) storage.root();
        final Base64Id nextId = root.nextId();
        assertThat(nextId).isNotNull();
        assertThat(ids).doesNotContain(nextId);
        ids.add(nextId);
    }

    @AfterEach
    void cleanup() {
        if(storage != null) {
            storage.shutdown();
            storage = null;
        }
    }
    
}
