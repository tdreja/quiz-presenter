package de.dreja.common.service;

import java.nio.file.Paths;

import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.dreja.common.model.DbRoot;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PreDestroy;


@Service
public class Storage {
    
    private final EmbeddedStorageManager storageManager;

    @Autowired
    Storage(@Value("") String storageFile) {
        this.storageManager = EmbeddedStorage.start(null, Paths.get(storageFile));
    }
    
    @Nonnull
    public DbRoot getRoot() {
        if(storageManager.root() == null) {
            storageManager.setRoot(new DbRoot());
        }
        return (DbRoot) storageManager.root();
    }

    public void storeRoot() {
        storageManager.storeRoot();
    }

    @PreDestroy
    public void onDestroy() {
        storageManager.shutdown();
    }
}
