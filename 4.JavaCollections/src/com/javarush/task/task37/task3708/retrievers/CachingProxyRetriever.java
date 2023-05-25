package com.javarush.task.task37.task3708.retrievers;

import com.javarush.task.task37.task3708.cache.LRUCache;
import com.javarush.task.task37.task3708.storage.Storage;

public class CachingProxyRetriever implements Retriever{

    LRUCache<Long, Object> cache = new LRUCache<>(10);
    OriginalRetriever originalRetriever;

    public CachingProxyRetriever(Storage storage) {
        this.originalRetriever = new OriginalRetriever(storage);
    }

    @Override
    public Object retrieve(long id) {
        Object obj = cache.find(id);
        if (obj == null) {
            Object result = originalRetriever.retrieve(id);
            cache.set(id, result);
            return result;
        }
        return obj;
    }
}
