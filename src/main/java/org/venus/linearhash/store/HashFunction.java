package org.venus.linearhash.store;

@FunctionalInterface
public interface HashFunction<KeyT> {

    int hash(KeyT key);

}
