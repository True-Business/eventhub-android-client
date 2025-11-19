package ru.truebusiness.liveposter_android_client.data.dto

import ru.truebusiness.liveposter_android_client.data.Organization
import java.util.UUID

/**
 * Объект-хелпер для маппинга организаций
 */
object OrganizationMapping {

    /**
     * Преобразует OrganizationDto в доменную модель Organization
     */
    fun toDomain(dto: OrganizationDto): Organization {
        return Organization(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            address = dto.address,
            coverUrl = dto.pictureUrl,
            admins = emptyList(),
            events = emptyList(),
            images = if (dto.pictureUrl.isNotBlank()) listOf(dto.pictureUrl) else emptyList(),
            isSubscribed = false,
            isMine = false
        )
    }

    /**
     * Преобразует список OrganizationDto в список доменных моделей Organization
     */
    fun toDomainList(dtos: List<OrganizationDto>): List<Organization> {
        return dtos.map { dto -> toDomain(dto) }
    }
}

/**
 * Расширение для преобразования доменной модели Organization в CreateOrganizationRequestDto
 */
fun Organization.toCreateRequestDto(creatorId: UUID): CreateOrganizationRequestDto {
    return CreateOrganizationRequestDto(
        name = this.name,
        description = this.description,
        address = this.address,
        pictureUrl = this.coverUrl,
        creatorId = creatorId
    )
}

/**
 * Расширение для преобразования доменной модели Organization в UpdateOrganizationRequestDto
 */
fun Organization.toUpdateRequestDto(): UpdateOrganizationRequestDto {
    return UpdateOrganizationRequestDto(
        name = this.name,
        description = this.description,
        address = this.address,
        pictureUrl = this.coverUrl
    )
}