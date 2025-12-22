package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.truebusiness.liveposter_android_client.data.dto.ConfirmUploadRequest
import ru.truebusiness.liveposter_android_client.data.dto.ConfirmUploadResponse
import ru.truebusiness.liveposter_android_client.data.dto.DownloadUrlsRequest
import ru.truebusiness.liveposter_android_client.data.dto.DownloadUrlsResponse
import ru.truebusiness.liveposter_android_client.data.dto.ListConfirmedRequest
import ru.truebusiness.liveposter_android_client.data.dto.ListConfirmedResponse
import ru.truebusiness.liveposter_android_client.data.dto.UploadUrlsRequest
import ru.truebusiness.liveposter_android_client.data.dto.UploadUrlsResponse

/**
 * API интерфейс для работы с хранилищем изображений.
 * Все методы асинхронные (suspend).
 */
interface StorageApi {

    /**
     * Получить presigned URLs для загрузки файлов
     *
     * @param request запрос с ownerId, ownerType и списком origin names
     * @return presigned URLs для PUT-загрузки
     */
    @POST("api/v1/storage/urls")
    suspend fun getUploadUrls(@Body request: UploadUrlsRequest): Response<UploadUrlsResponse>

    /**
     * Подтвердить загрузку файлов
     *
     * @param request запрос с ownerId, ownerType и списком ID загруженных файлов
     * @return статусы подтверждения для каждого файла
     */
    @POST("api/v1/storage/confirmed")
    suspend fun confirmUploaded(@Body request: ConfirmUploadRequest): Response<ConfirmUploadResponse>

    /**
     * Получить список подтвержденных объектов (изображений)
     *
     * @param request запрос с ownerId, ownerType и параметрами пагинации
     * @return список объектов с id и origin
     */
    @POST("api/v1/storage/confirmed/list")
    suspend fun listConfirmed(@Body request: ListConfirmedRequest): Response<ListConfirmedResponse>

    /**
     * Получить presigned URLs для скачивания файлов
     *
     * @param request запрос со списком ID файлов
     * @return URLs для скачивания с метаданными
     */
    @POST("api/v1/storage/confirmed/urls")
    suspend fun getDownloadUrls(@Body request: DownloadUrlsRequest): Response<DownloadUrlsResponse>
}

