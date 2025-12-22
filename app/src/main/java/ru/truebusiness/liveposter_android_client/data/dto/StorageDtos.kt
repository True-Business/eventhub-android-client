package ru.truebusiness.liveposter_android_client.data.dto

/**
 * DTO модели для работы со Storage API (загрузка и получение изображений)
 *
 * API использует:
 * - ownerType: тип сущности ("user", "event", "organization")
 * - ownerId: UUID сущности
 * - origin: "photo" (обычные изображения) или "cover" (обложка)
 */

// ============ Upload URLs ============

/**
 * Запрос на получение presigned URLs для загрузки файлов
 *
 * @param ownerId UUID сущности (пользователя/события/организации)
 * @param ownerType тип сущности ("user", "event", "organization")
 * @param originNames список origin-имён ("photo" или "cover")
 */
data class UploadUrlsRequest(
    val ownerId: String,
    val ownerType: String,
    val originNames: List<String>
)

/**
 * Элемент ответа с presigned URL для загрузки
 */
data class UploadUrlItem(
    val origin: String,
    val id: String,
    val url: String
)

/**
 * Ответ с presigned URLs для загрузки
 */
data class UploadUrlsResponse(
    val urls: List<UploadUrlItem>
)

// ============ Confirm Upload ============

/**
 * Запрос на подтверждение загрузки файлов
 *
 * @param ownerId UUID сущности (пользователя/события/организации)
 * @param ownerType тип сущности ("user", "event", "organization")
 * @param ids список ID файлов, полученных при получении presigned URLs
 */
data class ConfirmUploadRequest(
    val ownerId: String,
    val ownerType: String,
    val ids: List<String>
)

/**
 * Статус подтверждения загрузки
 */
data class ConfirmStatus(
    val id: String,
    val status: String
)

/**
 * Ответ на подтверждение загрузки
 */
data class ConfirmUploadResponse(
    val statuses: List<ConfirmStatus>
)

// ============ List Confirmed ============

/**
 * Параметры пагинации
 */
data class PageRequest(
    val size: Int = 100,
    val current: Int = 0
)

/**
 * Запрос на получение списка подтвержденных объектов
 *
 * @param ownerId UUID сущности (пользователя/события/организации)
 * @param ownerType тип сущности ("user", "event", "organization")
 * @param page параметры пагинации
 */
data class ListConfirmedRequest(
    val ownerId: String,
    val ownerType: String,
    val page: PageRequest = PageRequest()
)

/**
 * Объект хранилища (изображение)
 */
data class StorageObject(
    val id: String,
    val origin: String
)

/**
 * Ответ со списком подтвержденных объектов
 */
data class ListConfirmedResponse(
    val objects: List<StorageObject>
)

// ============ Download URLs ============

/**
 * Запрос на получение ссылок для скачивания
 */
data class DownloadUrlsRequest(
    val ids: List<String>
)

/**
 * Метаданные для скачивания
 */
data class DownloadMeta(
    val downloadUrl: String,
    val uploaded: String
)

/**
 * Элемент ответа с URL для скачивания
 */
data class DownloadUrlItem(
    val id: String,
    val meta: DownloadMeta
)

/**
 * Ответ с URLs для скачивания
 */
data class DownloadUrlsResponse(
    val urls: List<DownloadUrlItem>
)

// ============ Image URLs Result ============

/**
 * Результат получения изображений с cover.
 * Cover определяется по дате загрузки — самый новый cover является текущим.
 *
 * @param coverUrl URL текущего cover-изображения (самый новый по дате) или null если cover нет
 * @param imageUrls список URLs обычных изображений (без cover)
 */
data class ImageUrls(
    val coverUrl: String?,
    val imageUrls: List<String>
) {
    /**
     * Возвращает все URLs: cover первым (если есть), затем остальные изображения
     */
    fun getAllUrls(): List<String> = listOfNotNull(coverUrl) + imageUrls

    /**
     * Проверяет, есть ли хотя бы одно изображение (cover или обычное)
     */
    fun hasImages(): Boolean = coverUrl != null || imageUrls.isNotEmpty()

    /**
     * Общее количество изображений
     */
    fun totalCount(): Int = (if (coverUrl != null) 1 else 0) + imageUrls.size
}
