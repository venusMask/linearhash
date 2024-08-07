package org.venus.linearhash;

@FunctionalInterface
public interface HashFunction<KeyT> {

    int hash(KeyT key);

}
