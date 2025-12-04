package ru.truebusiness.liveposter_android_client.repository

import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.data.dto.SearchOrganizationRequestDto
import ru.truebusiness.liveposter_android_client.data.dto.OrganizationMapping
import ru.truebusiness.liveposter_android_client.data.dto.toCreateRequestDto
import ru.truebusiness.liveposter_android_client.data.dto.toUpdateRequestDto
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganization
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganizationsPool
import ru.truebusiness.liveposter_android_client.view.organizationslist.OrganizationTab
import java.util.UUID

/**
 * Репозиторий для работы с организациями
 * Поддерживает как реальные API вызовы, так и mock данные для разработки
 */
class OrgRepository {

    // ========== API Методы ==========

    /**
     * Получение организации по ID через API
     */
    suspend fun fetchOrganization(orgId: UUID): Organization? {
        return try {
            val dto = RetrofitInstance.organizationApi.getOrganizationById(orgId.toString())
            OrganizationMapping.toDomain(dto)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Поиск организаций через API
     */
    suspend fun searchOrganizations(
        search: String? = null,
        creatorShortId: String? = null,
        address: String? = null,
        onlyVerified: Boolean? = null,
        onlySubscribed: Boolean,
        onlyAdministrated: Boolean
    ): List<Organization>? {
        val request = SearchOrganizationRequestDto(
            search = search?: "",
            creatorShortId = creatorShortId?: "",
            address = address?: "",
            onlyVerified = onlyVerified?: false,
            onlySubscribed = onlySubscribed,
            onlyAdministrated = onlyAdministrated
        )
        return try {
            val dtos = RetrofitInstance.organizationApi.searchOrganizations(request)
            OrganizationMapping.toDomainList(dtos)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Создание новой организации через API
     */
    suspend fun createOrganization(
        organization: Organization,
        creatorId: UUID
    ): Organization? {
        val request = organization.toCreateRequestDto(creatorId)
        return try {
            val dto = RetrofitInstance.organizationApi.createOrganization(request)
            OrganizationMapping.toDomain(dto)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Обновление организации через API
     */
    suspend fun updateOrganization(
        orgId: UUID,
        organization: Organization
    ): Organization? {
        val request = organization.toUpdateRequestDto()
        return try {
            val dto = RetrofitInstance.organizationApi.updateOrganization(orgId.toString(), request)
            OrganizationMapping.toDomain(dto)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Удаление организации через API
     */
    suspend fun deleteOrganization(orgId: UUID): Boolean {
        return try {
            RetrofitInstance.organizationApi.deleteOrganization(orgId.toString())
            true
        } catch (e: Exception) {
            false
        }
    }

    // ========== Mock Методы (сохраняются для разработки и тестирования) ==========

    /**
     * Mock метод получения организации
     */
    fun fetchOrganizationMock(eventId: UUID): Organization? {
        return mockOrganization
    }

    /**
     * Mock метод получения страницы организаций
     */
    fun fetchOrganizationsPage(
        page: Int,
        pageSize: Int,
        tab: OrganizationTab
    ): List<Organization> {
        val full = mockOrganizationsPool.filter { org ->
            when (tab) {
                OrganizationTab.ALL -> true
                OrganizationTab.SUBSCRIPTIONS -> org.isSubscribed
                OrganizationTab.MINE -> org.isMine
            }
        }
        val from = page * pageSize
        val to = minOf(from + pageSize, full.size)
        return if (from >= full.size) emptyList() else full.subList(from, to)
    }

    /**
     * Mock метод поиска организаций
     */
    fun searchOrganizationsMock(
        query: String,
        onResult: (List<Organization>?) -> Unit
    ) {
        val filtered = mockOrganizationsPool.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true) ||
            it.address.contains(query, ignoreCase = true)
        }
        onResult(filtered)
    }
}