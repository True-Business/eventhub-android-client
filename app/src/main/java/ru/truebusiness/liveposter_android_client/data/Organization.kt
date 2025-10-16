package ru.truebusiness.liveposter_android_client.data

import java.util.UUID


fun emptyOrganization(): Organization {
    return Organization(
        id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
        name = "",
        coverUrl = "",
        address = "",
        description = "",
        admins = emptyList(),
        events = emptyList(),
        images = emptyList(),
        isSubscribed = false,
        isMine = false
    )
}

data class Organization(
    val id: UUID,
    val name: String,
    val coverUrl: String,
    val address: String,
    val description: String,
    val admins: List<User>,
    val events: List<Event>,
    val images: List<String>,
    val isSubscribed: Boolean,  //TODO временно, пока нет точного контракта??
    val isMine: Boolean
)