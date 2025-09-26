package ru.truebusiness.liveposter_android_client.repository

import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.repository.mocks.mockEventList
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganization
import java.util.UUID

class OrgRepository {

    fun fetchOrganization(eventId: UUID): Organization? {
        return mockOrganization
    }

}