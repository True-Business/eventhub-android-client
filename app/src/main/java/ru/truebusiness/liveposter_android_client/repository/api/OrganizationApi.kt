package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import ru.truebusiness.liveposter_android_client.data.dto.OrganizationDto
import ru.truebusiness.liveposter_android_client.data.dto.CreateOrganizationRequestDto
import ru.truebusiness.liveposter_android_client.data.dto.UpdateOrganizationRequestDto
import ru.truebusiness.liveposter_android_client.data.dto.SearchOrganizationRequestDto

/**
 * API интерфейс для работы с организациями
 * Базовый URL: http://eventhub-backend.ru/dev/
 */
interface OrganizationApi {

    /**
     * Создание новой организации
     * POST /api/v1/organization
     */
    @POST("api/v1/organization")
    suspend fun createOrganization(@Body request: CreateOrganizationRequestDto): OrganizationDto

    /**
     * Поиск организаций по параметрам
     * POST /api/v1/organization/search
     */
    @POST("api/v1/organization/search")
    suspend fun searchOrganizations(@Body request: SearchOrganizationRequestDto): List<OrganizationDto>

    /**
     * Получение организации по ID
     * GET /api/v1/organization/{organizationID}
     */
    @GET("api/v1/organization/{organizationID}")
    suspend fun getOrganizationById(@Path("organizationID") organizationId: String): OrganizationDto

    /**
     * Обновление организации
     * PATCH /api/v1/organization/{organizationID}
     */
    @PATCH("api/v1/organization/{organizationID}")
    suspend fun updateOrganization(
        @Path("organizationID") organizationId: String,
        @Body request: UpdateOrganizationRequestDto
    ): OrganizationDto

    /**
     * Удаление организации
     * DELETE /api/v1/organization/{organizationID}
     */
    @DELETE("api/v1/organization/{organizationID}")
    suspend fun deleteOrganization(@Path("organizationID") organizationId: String)
}