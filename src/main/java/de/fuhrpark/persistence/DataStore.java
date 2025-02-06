package de.fuhrpark.persistence;

import java.util.Optional;

public interface DataStore {
    void save(String key, Object data);
    Optional<Object> load(String key);
    void delete(String key);
} 