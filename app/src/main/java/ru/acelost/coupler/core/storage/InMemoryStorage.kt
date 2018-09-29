package ru.acelost.coupler.core.storage

import java.lang.IllegalStateException

abstract class InMemoryStorage<K, T> : Storage<K, T> {

    private val storage = mutableMapOf<K, T>()

    protected abstract fun generateKey(): K

    protected fun contains(key: K): Boolean {
        return storage.containsKey(key)
    }

    override fun list(): List<T> {
        return storage.values.toList()
    }

    override fun insert(entity: T): K {
        val key = generateKey()
        if (contains(key)) {
            throw IllegalStateException("Key $key already exists in storage $this." )
        }
        storage[key] = entity
        return key
    }

    override fun read(key: K): T? {
        return storage[key]
    }

    override fun update(key: K, entity: T): Boolean {
        if (!contains(key)) {
            return false
        }
        storage[key] = entity
        return true
    }

    override fun delete(key: K): Boolean {
        if (!contains(key)) {
            return false
        }
        storage.remove(key)
        return true
    }

    override fun drop() {
        storage.clear()
    }

}