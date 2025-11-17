package ru.truebusiness.liveposter_android_client.repository

import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganization
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganizationsPool
import ru.truebusiness.liveposter_android_client.view.organizationslist.OrganizationTab
import java.util.UUID

class OrgRepository {

    fun fetchOrganizationMock(eventId: UUID): Organization? {
        return mockOrganization
    }

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
}