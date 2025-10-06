package ru.truebusiness.liveposter_android_client.repository

import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganization
import java.util.UUID

class OrgRepository {

    fun fetchOrganizationMock(eventId: UUID): Organization? {
        return mockOrganization
    }

}