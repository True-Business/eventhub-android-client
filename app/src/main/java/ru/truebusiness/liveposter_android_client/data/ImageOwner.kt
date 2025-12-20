package ru.truebusiness.liveposter_android_client.data

import java.util.UUID

/**
 * Sealed class представляющий владельца изображения.
 * Используется для формирования origin в формате "type_uuid" или "cover_type_uuid" при загрузке изображений.
 *
 * Формат origin:
 * - Обычные изображения: "user_{uuid}", "event_{uuid}", "organization_{uuid}"
 * - Cover-изображения: "cover_user_{uuid}", "cover_event_{uuid}", "cover_organization_{uuid}"
 *
 * Cover определяется по дате загрузки — самый новый cover является текущим.
 */
sealed class ImageOwner {

    /**
     * Возвращает origin строку в формате "type_uuid"
     */
    abstract fun toOrigin(): String

    /**
     * Возвращает origin строку для cover-изображения в формате "cover_type_uuid"
     */
    abstract fun toCoverOrigin(): String

    /**
     * Возвращает префикс типа для фильтрации (например "user_", "event_", "organization_")
     */
    abstract fun getTypePrefix(): String

    /**
     * Возвращает префикс для cover-изображений (например "cover_user_", "cover_event_")
     */
    abstract fun getCoverTypePrefix(): String

    /**
     * Изображение принадлежит пользователю
     */
    data class User(val id: String) : ImageOwner() {
        constructor(uuid: UUID) : this(uuid.toString())

        override fun toOrigin(): String = "user_$id"
        override fun toCoverOrigin(): String = "cover_user_$id"
        override fun getTypePrefix(): String = "user_"
        override fun getCoverTypePrefix(): String = "cover_user_"
    }

    /**
     * Изображение принадлежит событию/мероприятию
     */
    data class Event(val id: String) : ImageOwner() {
        constructor(uuid: UUID) : this(uuid.toString())

        override fun toOrigin(): String = "event_$id"
        override fun toCoverOrigin(): String = "cover_event_$id"
        override fun getTypePrefix(): String = "event_"
        override fun getCoverTypePrefix(): String = "cover_event_"
    }

    /**
     * Изображение принадлежит организации
     */
    data class Organization(val id: String) : ImageOwner() {
        constructor(uuid: UUID) : this(uuid.toString())

        override fun toOrigin(): String = "organization_$id"
        override fun toCoverOrigin(): String = "cover_organization_$id"
        override fun getTypePrefix(): String = "organization_"
        override fun getCoverTypePrefix(): String = "cover_organization_"
    }

    companion object {
        /** Префикс для cover-изображений */
        const val COVER_PREFIX = "cover_"
    }
}
