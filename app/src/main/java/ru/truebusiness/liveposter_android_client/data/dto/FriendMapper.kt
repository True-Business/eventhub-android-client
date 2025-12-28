package ru.truebusiness.liveposter_android_client.data.dto

import ru.truebusiness.liveposter_android_client.data.Friend
import java.util.UUID

fun FriendDto.toDomainFriend(): Friend {
    return Friend(
        id = UUID.fromString(this.id),
        username = this.username,
        shortId = this.shortId,
        bio = this.bio,
        registrationDate = parseIsoDateTime(this.registrationDate),
        confirmed = this.confirmed
    )
}