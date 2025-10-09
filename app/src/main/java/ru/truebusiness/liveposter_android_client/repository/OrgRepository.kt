package ru.truebusiness.liveposter_android_client.repository

import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganization
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganizationsPool
import java.util.UUID

class OrgRepository {

    fun fetchOrganizationMock(eventId: UUID): Organization? {
        return mockOrganization
    }

    suspend fun fetchOrganizationsPage(
        page: Int,
        pageSize: Int,
        query: String?
    ): List<Organization> {
        val full = mockOrganizationsPool
            .filter { query.isNullOrBlank() || it.name.contains(query, ignoreCase = true) }

        val from = page * pageSize
        val toExclusive = minOf(from + pageSize, full.size)

        return if (from >= full.size) emptyList() else full.subList(from, toExclusive)
    }

}