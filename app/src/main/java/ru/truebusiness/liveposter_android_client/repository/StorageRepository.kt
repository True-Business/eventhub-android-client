package ru.truebusiness.liveposter_android_client.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import ru.truebusiness.liveposter_android_client.data.ImageOwner
import ru.truebusiness.liveposter_android_client.data.dto.ConfirmUploadRequest
import ru.truebusiness.liveposter_android_client.data.dto.DownloadUrlsRequest
import ru.truebusiness.liveposter_android_client.data.dto.ImageUrls
import ru.truebusiness.liveposter_android_client.data.dto.ListConfirmedRequest
import ru.truebusiness.liveposter_android_client.data.dto.PageRequest
import ru.truebusiness.liveposter_android_client.data.dto.UploadUrlsRequest
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance
import java.io.File
import java.time.Instant

/**
 * Репозиторий для работы с хранилищем изображений.
 * Инкапсулирует логику загрузки и получения изображений через presigned URLs.
 *
 * API использует:
 * - ownerType: тип сущности ("user", "event", "organization")
 * - ownerId: UUID сущности
 * - origin: "photo_{uuid}" (обычные изображения) или "cover_{uuid}" (обложка)
 *
 * Origin содержит случайный UUID для уникальности имени файла.
 * API обрабатывает по одному файлу за запрос. Для загрузки нескольких файлов
 * используется конкурентный запуск отдельных запросов.
 *
 * Все методы асинхронные и безопасны для вызова из UI-потока.
 */
class StorageRepository {

    private val storageApi = RetrofitInstance.storageApi
    private val uploadClient = RetrofitInstance.insecureUploadClient

    /**
     * Загружает один файл для указанного владельца.
     * Это базовый приватный метод, который выполняет полный цикл загрузки:
     * 1. Получает presigned URL
     * 2. Загружает файл через PUT
     * 3. Подтверждает загрузку
     *
     * @param owner владелец изображения (User/Event/Organization)
     * @param origin origin строка (например "photo_{uuid}" или "cover_{uuid}")
     * @param file файл для загрузки
     * @return Result с ID загруженного изображения или ошибкой
     */
    private suspend fun uploadSingleImage(
        owner: ImageOwner,
        origin: String,
        file: File
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            // 1. Получаем presigned URL для одного файла
            val urlsRequest = UploadUrlsRequest(
                ownerId = owner.getOwnerId(),
                ownerType = owner.getOwnerType(),
                originNames = listOf(origin)
            )

            val urlsResponse = storageApi.getUploadUrls(urlsRequest)
            if (!urlsResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Failed to get upload URL: ${urlsResponse.code()} ${urlsResponse.message()}")
                )
            }

            val uploadUrl = urlsResponse.body()?.urls?.firstOrNull()
                ?: return@withContext Result.failure(Exception("Empty upload URLs response"))

            // 2. Загружаем файл на presigned URL
            val uploadResult = uploadFileToPresignedUrl(
                url = uploadUrl.url,
                file = file,
                contentType = getContentType(file)
            )

            if (uploadResult.isFailure) {
                return@withContext Result.failure(uploadResult.exceptionOrNull()!!)
            }

            // 3. Подтверждаем загрузку
            val confirmRequest = ConfirmUploadRequest(
                ownerId = owner.getOwnerId(),
                ownerType = owner.getOwnerType(),
                ids = listOf(uploadUrl.id)
            )

            val confirmResponse = storageApi.confirmUploaded(confirmRequest)
            if (!confirmResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Failed to confirm upload: ${confirmResponse.code()} ${confirmResponse.message()}")
                )
            }

            val status = confirmResponse.body()?.statuses?.firstOrNull()
            if (status?.status != "UPLOADED") {
                return@withContext Result.failure(
                    Exception("File failed to confirm: ${status?.id}: ${status?.status}")
                )
            }

            Result.success(uploadUrl.id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Загружает несколько изображений для указанного владельца.
     * Каждый файл загружается отдельным запросом, все запросы выполняются конкурентно.
     * Все изображения загружаются как обычные (origin = "photo_{uuid}").
     *
     * @param owner владелец изображений (User/Event/Organization)
     * @param files список файлов для загрузки
     * @return Result с списком ID загруженных изображений или ошибкой
     */
    suspend fun uploadImages(
        owner: ImageOwner,
        files: List<File>
    ): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            if (files.isEmpty()) {
                return@withContext Result.success(emptyList())
            }

            // Конкурентно загружаем все файлы, генерируя уникальный origin для каждого
            val results = coroutineScope {
                files.map { file ->
                    async {
                        uploadSingleImage(owner, owner.generatePhotoOrigin(), file)
                    }
                }.awaitAll()
            }

            // Проверяем результаты
            val successIds = mutableListOf<String>()
            val errors = mutableListOf<String>()

            results.forEach { result ->
                result.onSuccess { id ->
                    successIds.add(id)
                }.onFailure { e ->
                    errors.add(e.message ?: "Unknown error")
                }
            }

            if (errors.isNotEmpty()) {
                return@withContext Result.failure(
                    Exception("Failed to upload ${errors.size} file(s): ${errors.joinToString("; ")}")
                )
            }

            Result.success(successIds)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Получает URLs для скачивания обычных изображений (origin начинается с "photo_") указанного владельца.
     *
     * @param owner владелец изображений
     * @return Result со списком URLs для скачивания (можно использовать в Coil/ImageView)
     */
    suspend fun getImageUrls(
        owner: ImageOwner
    ): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            // 1. Получаем список всех подтвержденных объектов владельца
            val listRequest = ListConfirmedRequest(
                ownerId = owner.getOwnerId(),
                ownerType = owner.getOwnerType(),
                page = PageRequest(size = 100, current = 0)
            )

            val listResponse = storageApi.listConfirmed(listRequest)
            if (!listResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Failed to list confirmed objects: ${listResponse.code()} ${listResponse.message()}")
                )
            }

            val objects = listResponse.body()?.objects ?: emptyList()

            // 2. Фильтруем только обычные изображения (origin начинается с "photo_")
            val photoObjects = objects.filter { it.origin.startsWith(ImageOwner.ORIGIN_PHOTO_PREFIX) }

            if (photoObjects.isEmpty()) {
                return@withContext Result.success(emptyList())
            }

            // 3. Запрашиваем download URLs
            val ids = photoObjects.map { it.id }
            val downloadRequest = DownloadUrlsRequest(ids = ids)

            val downloadResponse = storageApi.getDownloadUrls(downloadRequest)
            if (!downloadResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Failed to get download URLs: ${downloadResponse.code()} ${downloadResponse.message()}")
                )
            }

            val downloadUrls = downloadResponse.body()?.urls?.map { it.meta.downloadUrl } ?: emptyList()

            Result.success(downloadUrls)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Загружает одно изображение на presigned URL.
     */
    private suspend fun uploadFileToPresignedUrl(
        url: String,
        file: File,
        contentType: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val mediaType = contentType.toMediaType()
            val requestBody = file.asRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .put(requestBody)
                .header("Content-Type", contentType)
                .build()

            val response = uploadClient.newCall(request).execute()

            if (response.code !in listOf(200, 204)) {
                return@withContext Result.failure(
                    Exception("Upload failed: ${response.code} ${response.body?.string()}")
                )
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Определяет Content-Type по расширению файла.
     */
    private fun getContentType(file: File): String {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            "gif" -> "image/gif"
            "bmp" -> "image/bmp"
            else -> "image/jpeg" // fallback
        }
    }

    /**
     * Загружает изображения с cover.
     * Первый файл в списке становится cover-изображением (origin = "cover_{uuid}"),
     * остальные — обычными изображениями (origin = "photo_{uuid}").
     * Каждый файл загружается отдельным запросом, все запросы выполняются конкурентно.
     *
     * @param owner владелец изображений (User/Event/Organization)
     * @param files список файлов для загрузки (первый = cover)
     * @return Result с списком ID загруженных изображений или ошибкой
     */
    suspend fun uploadImagesWithCover(
        owner: ImageOwner,
        files: List<File>
    ): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            if (files.isEmpty()) {
                return@withContext Result.success(emptyList())
            }

            // Конкурентно загружаем все файлы (первый как cover, остальные как обычные)
            val results = coroutineScope {
                files.mapIndexed { index, file ->
                    val origin = if (index == 0) owner.generateCoverOrigin() else owner.generatePhotoOrigin()
                    async {
                        uploadSingleImage(owner, origin, file)
                    }
                }.awaitAll()
            }

            // Проверяем результаты
            val successIds = mutableListOf<String>()
            val errors = mutableListOf<String>()

            results.forEach { result ->
                result.onSuccess { id ->
                    successIds.add(id)
                }.onFailure { e ->
                    errors.add(e.message ?: "Unknown error")
                }
            }

            if (errors.isNotEmpty()) {
                return@withContext Result.failure(
                    Exception("Failed to upload ${errors.size} file(s): ${errors.joinToString("; ")}")
                )
            }

            Result.success(successIds)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Загружает только cover-изображение.
     * Если cover уже существует, новый становится текущим (определяется по дате).
     *
     * @param owner владелец изображения (User/Event/Organization)
     * @param file файл cover-изображения
     * @return Result с ID загруженного cover или ошибкой
     */
    suspend fun uploadCoverImage(
        owner: ImageOwner,
        file: File
    ): Result<String> {
        return uploadSingleImage(owner, owner.generateCoverOrigin(), file)
    }

    /**
     * Получает URL текущего cover-изображения.
     * Cover определяется по дате загрузки — самый новый cover является текущим.
     *
     * @param owner владелец cover (User/Event/Organization)
     * @return Result с URL cover или null если cover нет
     */
    suspend fun getCoverImageUrl(
        owner: ImageOwner
    ): Result<String?> = withContext(Dispatchers.IO) {
        try {
            // Получаем список всех объектов владельца
            val listRequest = ListConfirmedRequest(
                ownerId = owner.getOwnerId(),
                ownerType = owner.getOwnerType(),
                page = PageRequest(size = 100, current = 0)
            )

            val listResponse = storageApi.listConfirmed(listRequest)
            if (!listResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Failed to list confirmed objects: ${listResponse.code()} ${listResponse.message()}")
                )
            }

            val objects = listResponse.body()?.objects ?: emptyList()

            // Фильтруем cover-объекты (origin начинается с "cover_")
            val coverObjects = objects.filter { it.origin.startsWith(ImageOwner.ORIGIN_COVER_PREFIX) }

            if (coverObjects.isEmpty()) {
                return@withContext Result.success(null)
            }

            // Получаем download URLs для всех cover
            val ids = coverObjects.map { it.id }
            val downloadRequest = DownloadUrlsRequest(ids = ids)

            val downloadResponse = storageApi.getDownloadUrls(downloadRequest)
            if (!downloadResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Failed to get download URLs: ${downloadResponse.code()} ${downloadResponse.message()}")
                )
            }

            val downloadItems = downloadResponse.body()?.urls ?: emptyList()

            // Выбираем самый новый cover по дате uploaded
            val latestCover = downloadItems.maxByOrNull { parseUploadedDate(it.meta.uploaded) }

            Result.success(latestCover?.meta?.downloadUrl)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Получает все изображения с cover первым.
     * Cover определяется по дате загрузки — самый новый cover является текущим.
     *
     * @param owner владелец изображений (User/Event/Organization)
     * @return Result с ImageUrls (coverUrl + imageUrls)
     */
    suspend fun getImageUrlsWithCover(
        owner: ImageOwner
    ): Result<ImageUrls> = withContext(Dispatchers.IO) {
        try {
            // Получаем список всех объектов владельца
            val listRequest = ListConfirmedRequest(
                ownerId = owner.getOwnerId(),
                ownerType = owner.getOwnerType(),
                page = PageRequest(size = 100, current = 0)
            )

            val listResponse = storageApi.listConfirmed(listRequest)
            if (!listResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Failed to list confirmed objects: ${listResponse.code()} ${listResponse.message()}")
                )
            }

            val objects = listResponse.body()?.objects ?: emptyList()

            // Разделяем на cover и обычные изображения по префиксам
            val coverObjects = objects.filter { it.origin.startsWith(ImageOwner.ORIGIN_COVER_PREFIX) }
            val regularObjects = objects.filter { it.origin.startsWith(ImageOwner.ORIGIN_PHOTO_PREFIX) }

            if (coverObjects.isEmpty() && regularObjects.isEmpty()) {
                return@withContext Result.success(ImageUrls(null, emptyList()))
            }

            // Получаем download URLs для всех объектов
            val allIds = (coverObjects + regularObjects).map { it.id }
            val downloadRequest = DownloadUrlsRequest(ids = allIds)

            val downloadResponse = storageApi.getDownloadUrls(downloadRequest)
            if (!downloadResponse.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Failed to get download URLs: ${downloadResponse.code()} ${downloadResponse.message()}")
                )
            }

            val downloadItems = downloadResponse.body()?.urls ?: emptyList()

            // Создаем map id -> downloadItem для быстрого доступа
            val downloadMap = downloadItems.associateBy { it.id }

            // Находим текущий cover (самый новый по дате)
            val coverDownloadItems = coverObjects.mapNotNull { downloadMap[it.id] }
            val latestCover = coverDownloadItems.maxByOrNull { parseUploadedDate(it.meta.uploaded) }

            // Собираем URLs обычных изображений
            val regularUrls = regularObjects.mapNotNull { downloadMap[it.id]?.meta?.downloadUrl }

            Result.success(ImageUrls(
                coverUrl = latestCover?.meta?.downloadUrl,
                imageUrls = regularUrls
            ))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Парсит дату uploaded из ISO формата для сравнения.
     */
    private fun parseUploadedDate(uploaded: String): Instant {
        return try {
            Instant.parse(uploaded)
        } catch (e: Exception) {
            Instant.EPOCH // fallback для некорректных дат
        }
    }
}
