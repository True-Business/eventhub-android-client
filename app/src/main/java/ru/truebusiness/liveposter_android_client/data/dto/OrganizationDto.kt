package ru.truebusiness.liveposter_android_client.data.dto

import ru.truebusiness.liveposter_android_client.data.Organization
import java.util.UUID

/**
 * DTO для ответа от API с информацией об организации
 */
data class OrganizationDto(
    val id: UUID,
    val name: String,
    val description: String,
    val address: String,
    val pictureUrl: String,
    val creatorId: UUID
)

/**
 * DTO для запроса на создание новой организации
 */
data class CreateOrganizationRequestDto(
    val name: String,
    val description: String,
    val address: String = "",
    val pictureUrl: String = "",
    val creatorId: UUID
)

/**
 * DTO для запроса на обновление организации
 */
data class UpdateOrganizationRequestDto(
    val name: String = "",
    val description: String = "",
    val address: String = "",
    val pictureUrl: String = ""
)

/**
 * DTO для запроса поиска организаций
 */
data class SearchOrganizationRequestDto(
    val search: String = "",
    val creatorShortId: String = "",
    val address: String = "",
    val onlyVerified: Boolean = false,
    val onlySubscribed: Boolean = false,
    val onlyAdministrated: Boolean = false
)

/**
 * Расширение для преобразования OrganizationDto в доменную модель Organization
 */
fun OrganizationDto.toDomainOrganization(): Organization {
    return Organization(
        id = this.id,
        name = this.name,
        description = this.description,
        address = this.address,
        coverUrl = this.pictureUrl,

        // Поля, которые не предоставляются API - используем значения по умолчанию
        // TODO: В будущем эти поля могут быть получены из отдельных API вызовов
        admins = emptyList(),
        events = emptyList(),
        images = emptyList(),
        isSubscribed = false,
        isMine = false,
    )
}
