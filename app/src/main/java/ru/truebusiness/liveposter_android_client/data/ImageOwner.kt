package ru.truebusiness.liveposter_android_client.data

import java.util.UUID

/**
 * Sealed class представляющий владельца изображения.
 *
 * Используется для формирования запросов к Storage API:
 * - ownerType: "user", "event", "organization"
 * - ownerId: UUID сущности
 * - origin: "photo_{uuid}" (обычные изображения) или "cover_{uuid}" (обложка)
 *
 * Origin содержит случайный UUID для уникальности имени файла.
 * Cover определяется по дате загрузки — самый новый cover является текущим.
 */
sealed class ImageOwner {

    /**
     * Возвращает тип владельца для API ("user", "event", "organization")
     */
    abstract fun getOwnerType(): String

    /**
     * Возвращает ID владельца (UUID сущности)
     */
    abstract fun getOwnerId(): String

    /**
     * Генерирует уникальный origin для обычных изображений.
     * Формат: "photo_{random_uuid}"
     */
    fun generatePhotoOrigin(): String = "${ORIGIN_PHOTO_PREFIX}${UUID.randomUUID()}"

    /**
     * Генерирует уникальный origin для cover-изображения.
     * Формат: "cover_{random_uuid}"
     */
    fun generateCoverOrigin(): String = "${ORIGIN_COVER_PREFIX}${UUID.randomUUID()}"

    /**
     * Изображение принадлежит пользователю
     */
    data class User(val id: String) : ImageOwner() {
        constructor(uuid: UUID) : this(uuid.toString())

        override fun getOwnerType(): String = "user"
        override fun getOwnerId(): String = id
    }

    /**
     * Изображение принадлежит событию/мероприятию
     */
    data class Event(val id: String) : ImageOwner() {
        constructor(uuid: UUID) : this(uuid.toString())

        override fun getOwnerType(): String = "event"
        override fun getOwnerId(): String = id
    }

    /**
     * Изображение принадлежит организации
     */
    data class Organization(val id: String) : ImageOwner() {
        constructor(uuid: UUID) : this(uuid.toString())

        override fun getOwnerType(): String = "organization"
        override fun getOwnerId(): String = id
    }

    companion object {
        /** Префикс origin для обычных изображений */
        const val ORIGIN_PHOTO_PREFIX = "photo_"
        /** Префикс origin для cover-изображений */
        const val ORIGIN_COVER_PREFIX = "cover_"
    }
}
