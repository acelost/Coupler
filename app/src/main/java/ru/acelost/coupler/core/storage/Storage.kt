package ru.acelost.coupler.core.storage

/**
 * Интерфейс хранилища объектов.
 * @param K - тип первичного ключа
 * @param T - тип объектов в хранилище
 */
interface Storage<K, T> {

    /**
     * Прочитать все элементы из базы.
     */
    fun list(): List<T>

    /**
     * Вставить сущность в базу.
     */
    fun insert(entity: T): K

    /**
     * Прочитать сущность из базы.
     */
    fun read(key: K): T?

    /**
     * Обновить сущность в базе.
     */
    fun update(key: K, entity: T): Boolean

    /**
     * Удалить сущность из базы.
     */
    fun delete(key: K): Boolean

    /**
     * Удалить данные из хранилища.
     */
    fun drop()

}