package fr.uha.chaguer.android.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import java.util.Date

@Suppress("unused")
abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun internalCreate(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun internalCreate(entities: Array<T>): Array<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun internalCreate(entities: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun internalUpdate(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun internalUpdate(entities: Array<T>): Array<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun internalUpdate(entities: List<T>): List<Long>

    @Delete
    abstract fun delete(entity: T)

    @Delete
    abstract fun delete(entities: Array<T>)

    @Delete
    abstract fun delete(entities: List<T>)

    fun create(entity: T): Long {
        if (entity is Timable) {
            val date = Date()
            if (entity.getCreatedDate() == null) entity.setCreatedDate(date)
            entity.setUpdatedDate(date)
        }
        val id = internalCreate(entity)
        if (entity is Referencable) {
            entity.setId(id)
        }
        return id
    }

    fun create(entities: Array<T>): Array<Long> {
        val date = Date()
        entities.filterIsInstance<Timable>().forEach { entity ->
            if (entity.getCreatedDate() == null) entity.setCreatedDate(date)
            entity.setUpdatedDate(date)
        }
        val ids = internalCreate(entities)
        entities.zip(ids).filter { it.first is Referencable }.forEach {
            (it.first as Referencable).setId(it.second)
        }
        return ids
    }

    fun create(entities: List<T>): List<Long> {
        val date = Date()
        entities.filterIsInstance<Timable>().forEach { entity ->
            if (entity.getCreatedDate() == null) entity.setCreatedDate(date)
            entity.setUpdatedDate(date)
        }
        val ids = internalCreate(entities)
        entities.zip(ids).filter { it.first is Referencable }.forEach {
            (it.first as Referencable).setId(it.second)
        }
        return ids
    }

    fun update(entity: T): Long {
        if (entity is Timable) {
            val date = Date()
            entity.setUpdatedDate(date)
        }
        return internalUpdate(entity)
    }

    fun update(entities: Array<T>): Array<Long> {
        val date = Date()
        entities.filterIsInstance<Timable>().forEach { entity ->
            entity.setUpdatedDate(date)
        }
        return internalUpdate(entities)
    }

    fun update(entities: List<T>): List<Long> {
        val date = Date()
        entities.filterIsInstance<Timable>().forEach { entity ->
            entity.setUpdatedDate(date)
        }
        return internalUpdate(entities)
    }

    fun upsert(entity: T): Long {
        if (entity is Timable) {
            val date = Date()
            if (entity.getCreatedDate() == null) entity.setCreatedDate(date)
            entity.setUpdatedDate(date)
        }
        val id = internalUpdate(entity)
        if (entity is Referencable) {
            if (entity.getId() == 0L) {
                entity.setId(id)
            }
        }
        return id
    }

    companion object {

        protected fun <T : Referencable> contains(list: List<T>, item: T?): Boolean {
            return list.map { it.getId() }.contains(item?.getId())
        }

        protected fun <T : Referencable> toAdd(previous: List<T>, now: List<T>): List<T> {
            return now.filterNot { contains(previous, it) }
        }

        protected fun <T : Referencable> toRemove(previous: List<T>, now: List<T>): List<T> {
            return previous.filterNot { contains(now, it) }
        }
    }
}