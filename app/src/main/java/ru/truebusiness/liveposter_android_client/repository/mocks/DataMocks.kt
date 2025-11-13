package ru.truebusiness.liveposter_android_client.repository.mocks

import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.data.EventStatus
import ru.truebusiness.liveposter_android_client.data.Friend
import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.data.User
import ru.truebusiness.liveposter_android_client.data.dto.parseIsoDateTime
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

// Mock current user ID
val mockCurrentUserId = UUID.fromString("12345678-1234-1234-1234-123456789abc")

val mockEventList = listOf(
    // Events organized by current user
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FESTIVALS),
        title = "Мой Фестиваль",
        content = "Ежегодное мероприятие с множеством развлечений, концертом и т.п.",
        startDate = parseIsoDateTime("2025-05-07T13:00:00"),
        endDate = parseIsoDateTime("2025-05-07T18:00:00"),
        location = "Академгородок, ул. Николаева, 11",
        posterUrl = "https://nadvizh.ru/media/events_img/67/smart-piknik_logo.jpg",
        
        // API fields
        organizerId = mockCurrentUserId,
        organizationId = UUID.fromString("12345678-1234-1234-1234-123456789def"),
        updatedAt = parseIsoDateTime("2025-01-15T10:00:00Z"),
        address = "ул. Николаева, 11",
        route = "рядом с метро Академгородок",
        city = "Новосибирск",
        peopleLimit = 500,
        registerEndDateTime = parseIsoDateTime("2025-05-06T23:59:59Z"),
        withRegister = true,
        open = true,
        
        // Existing fields
        price = 500.0,
        duration = 300, // 5 hours
        organizer = "NSU Events",
        isUserParticipating = true,
        eventStatus = EventStatus.PUBLISHED,
        isPublic = true
    ),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.MEETINGS),
        title = "Моя Конференция",
        content = "Выставка факультетов, общение со студентами и многое другое.",
        startDate = parseIsoDateTime("2025-06-10T10:00:00Z"),
        endDate = parseIsoDateTime("2025-06-10T15:00:00Z"),
        location = "Академгородок, ул. Пирогова, 1",
        posterUrl = "https://static.tildacdn.com/tild3337-6465-4835-a130-623838656562/1680900--------2.jpg",
        
        // API fields
        organizerId = mockCurrentUserId,
        organizationId = UUID.fromString("12345678-1234-1234-1234-123456789def"),
        updatedAt = parseIsoDateTime("2025-01-20T14:30:00Z"),
        address = "ул. Пирогова, 1",
        route = "главный корпус НГУ",
        city = "Новосибирск",
        peopleLimit = 1000,
        registerEndDateTime = parseIsoDateTime("2025-06-09T23:59:59Z"),
        withRegister = true,
        open = true,
        
        // Existing fields
        price = null, // free
        duration = 300, // 5 hours
        organizer = "НГУ",
        isUserParticipating = true,
        eventStatus = EventStatus.PUBLISHED,
        isPublic = true
    ),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.SHOWS),
        title = "Мое Шоу",
        content = "Самое смешное юмористическое соревнование в Академгородке.",
        startDate = parseIsoDateTime("2024-11-15T19:00:00Z"),
        endDate = parseIsoDateTime("2024-11-15T21:00:00Z"),
        location = "Академгородок, Проспект Строителей, 21",
        posterUrl = "https://sun9-12.userapi.com/Tjc3E_Yysjm5NfzuxndPMHgTXAO1S7T6-Ks87Q/iSYEIaVpiX4.jpg",
        
        // API fields
        organizerId = mockCurrentUserId,
        organizationId = UUID.fromString("12345678-1234-1234-1234-123456789def"),
        updatedAt = parseIsoDateTime("2024-11-01T12:00:00Z"),
        address = "Проспект Строителей, 21",
        route = "рядом с ДК Строителей",
        city = "Новосибирск",
        peopleLimit = 200,
        registerEndDateTime = parseIsoDateTime("2024-11-14T23:59:59Z"),
        withRegister = true,
        open = true,
        
        // Existing fields
        price = 300.0,
        duration = 120, // 2 hours
        organizer = "Студенческий совет НГУ",
        isUserParticipating = true,
        eventStatus = EventStatus.COMPLETED,
        isPublic = true
    ),
    // DRAFT events organized by current user
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.MUSIC),
        title = "Черновик Концерта",
        content = "Концерт группы 3 дня дождя (в разработке)",
        startDate = parseIsoDateTime("2025-12-01T20:00:00Z"),
        endDate = parseIsoDateTime("2025-12-01T23:00:00Z"),
        location = "Новосибирск, Локомотив-Арена",
        posterUrl = "https://geopro-photos.storage.yandexcloud.net/resize_cache/48245238/e14e74968349be09ee1354fc509cee5d/iblock/aea/aeabbb2f275ef6812990534d33cb64d2/photo_2024_08_29-00.27.34.jpeg",
        
        // API fields
        organizerId = mockCurrentUserId,
        organizationId = UUID.fromString("12345678-1234-1234-1234-123456789def"),
        updatedAt = parseIsoDateTime("2025-01-25T16:45:00Z"),
        address = "ул. Дусэ Ковальчук, 4",
        route = "ст. метро Спортивная",
        city = "Новосибирск",
        peopleLimit = 5000,
        registerEndDateTime = parseIsoDateTime("2025-11-30T23:59:59Z"),
        withRegister = true,
        open = true,
        
        // Existing fields
        price = 1500.0,
        duration = 180, // 3 hours
        organizer = "Концертное агентство",
        isUserParticipating = false,
        eventStatus = EventStatus.DRAFT,
        isPublic = true
    ),
//    Event(
//        id = UUID.randomUUID(),
//        category = listOf(EventCategory.ALL, EventCategory.FESTIVALS),
//        title = "Черновик Фестиваля 2",
//        content = "Планируемый фестиваль (не готов)",
//        startDate = "2025-11-15T12:00:00",
//        endDate = "2025-11-15T20:00:00",
//        location = "Новосибирск, Центральный парк",
//        posterUrl = "https://example.com/festival-draft.jpg",
//        price = 800.0,
//        duration = 480, // 8 hours
//        organizer = "Event Organizer",
//        organizerId = mockCurrentUserId,
//        isUserParticipating = false,
//        eventStatus = EventStatus.DRAFT,
//        isPublic = true
//    ),
    // Events organized by other users
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FESTIVALS),
        title = "OpenSpacePicnic",
        content = "Пикник в дворике нового корпуса НГУ посвящённый дню знаний.",
        startDate = parseIsoDateTime("2025-04-15T12:00:00Z"),
        endDate = parseIsoDateTime("2025-04-15T16:00:00Z"),
        location = "Академгородок, ул. Пирогова, 1",
        posterUrl = "https://static.tildacdn.com/tild3230-3163-4439-b733-366633643931/DSC_4453.jpg",
        
        // API fields
        organizerId = UUID.fromString("87654321-4321-4321-4321-210987654321"),
        organizationId = UUID.fromString("12345678-1234-1234-1234-123456789def"),
        updatedAt = parseIsoDateTime("2025-04-01T09:00:00Z"),
        address = "ул. Пирогова, 1",
        route = "дворик нового корпуса НГУ",
        city = "Новосибирск",
        peopleLimit = 300,
        registerEndDateTime = parseIsoDateTime("2025-04-14T23:59:59Z"),
        withRegister = false,
        open = true,
        
        // Existing fields
        price = null, // free
        duration = 240, // 4 hours
        organizer = "НГУ",
        isUserParticipating = true,
        eventStatus = EventStatus.COMPLETED,
        isPublic = true
    ),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.SHOWS),
        title = "Андрей Бебуришвили",
        content = "Стендап 18+",
        startDate = parseIsoDateTime("2025-02-15T19:00:00Z"),
        endDate = parseIsoDateTime("2025-02-15T21:00:00Z"),
        location = "Новосибирск, ККК им.Маяковского",
        posterUrl = "https://live.mts.ru/image/full/505d782c-73eb-2170-a0e5-b28853d839b0.jpg",
        
        // API fields
        organizerId = UUID.fromString("87654321-4321-4321-4321-210987654322"),
        organizationId = UUID.fromString("87654321-4321-4321-4321-210987654320"),
        updatedAt = parseIsoDateTime("2025-01-10T11:30:00Z"),
        address = "Красный проспект, 36",
        route = "центр города",
        city = "Новосибирск",
        peopleLimit = 400,
        registerEndDateTime = parseIsoDateTime("2025-02-14T23:59:59Z"),
        withRegister = true,
        open = true,
        
        // Existing fields
        price = 800.0,
        duration = 120, // 2 hours
        organizer = "Комedy Club",
        isUserParticipating = true,
        eventStatus = EventStatus.PUBLISHED,
        isPublic = true
    ),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FILMS),
        title = "Веном: Последний танец",
        content = "Приспособившись к совместному существованию, Эдди и Веном стали друзьями и " +
                "вместе сражаются со злодеями. Но теперь за Эдди охотятся военные, а за Веномом — " +
                "его инопланетные сородичи, угрожающие всему живому.",
        startDate = parseIsoDateTime("2024-10-24T14:00:00Z"),
        endDate = parseIsoDateTime("2024-10-24T16:30:00Z"),
        location = "Академгородок, ул. Кутателадзе, 4/4",
        posterUrl = "https://images.iptv.rt.ru/images/cvj4k3rir4sqiatdopl0.jpg",
        
        // API fields
        organizerId = UUID.fromString("87654321-4321-4321-4321-210987654323"),
        organizationId = UUID.fromString("87654321-4321-4321-4321-210987654321"),
        updatedAt = parseIsoDateTime("2024-10-20T10:15:00Z"),
        address = "ул. Кутателадзе, 4/4",
        route = "ст. метро Площадь Маркса",
        city = "Новосибирск",
        peopleLimit = 150,
        registerEndDateTime = parseIsoDateTime("2024-10-23T23:59:59Z"),
        withRegister = true,
        open = true,
        
        // Existing fields
        price = 250.0,
        duration = 150, // 2.5 hours
        organizer = "Кинотеатр Имя",
        isUserParticipating = true,
        eventStatus = EventStatus.COMPLETED,
        isPublic = true
    ),
//    // Additional events for better testing
//    Event(
//        id = UUID.randomUUID(),
//        category = listOf(EventCategory.ALL, EventCategory.MUSIC),
//        title = "Jazz Evening",
//        content = "Вечер джазовой музыки с известными исполнителями.",
//        startDate = "2025-07-20T18:00:00",
//        endDate = "2025-07-20T22:00:00",
//        location = "Новосибирск, Филармония",
//        posterUrl = "https://example.com/jazz.jpg",
//        price = 1200.0,
//        duration = 240, // 4 hours
//        organizer = "Новосибирская филармония",
//        organizerId = UUID.randomUUID(), // Different organizer
//        isUserParticipating = true,
//        eventStatus = EventStatus.PLANNED,
//        isPublic = true
//    ),
//    Event(
//        id = UUID.randomUUID(),
//        category = listOf(EventCategory.ALL, EventCategory.MEETINGS),
//        title = "Tech Conference 2025",
//        content = "Ежегодная технологическая конференция.",
//        startDate = "2025-08-15T09:00:00",
//        endDate = "2025-08-15T18:00:00",
//        location = "Новосибирск, Экспоцентр",
//        posterUrl = "https://example.com/tech.jpg",
//        price = 3000.0,
//        duration = 540, // 9 hours
//        organizer = "Tech Community",
//        organizerId = UUID.randomUUID(), // Different organizer
//        isUserParticipating = false,
//        eventStatus = EventStatus.PLANNED,
//        isPublic = true
//    ),
//    Event(
//        id = UUID.randomUUID(),
//        category = listOf(EventCategory.ALL, EventCategory.FESTIVALS),
//        title = "Private Party",
//        content = "Закрытая вечеринка для приглашенных.",
//        startDate = "2025-09-01T20:00:00",
//        endDate = "2025-09-02T02:00:00",
//        location = "Новосибирск, Приватное место",
//        posterUrl = "https://example.com/private.jpg",
//        price = null,
//        duration = 360, // 6 hours
//        organizer = "Private Organizer",
//        organizerId = UUID.randomUUID(), // Different organizer
//        isUserParticipating = true,
//        eventStatus = EventStatus.PLANNED,
//        isPublic = false
//    )
)

val mockUserFriendsList = listOf(
    Friend(
        id = UUID.randomUUID(),
        username = "Харченко Владимир Александрович"
    ),
    Friend(
        id = UUID.randomUUID(),
        username = "Мацько Александр Михайлович"
    ),
    Friend(
        id = UUID.randomUUID(),
        username = "Путин Владимир Владимирович"
    ),
    Friend(
        id = UUID.randomUUID(),
        username = "Пушкин Александр Сергеевич"
    )
)

val mockUsersList = listOf(
    User(
        userName = "alex",
        coverUrl = "https://t3.ftcdn.net/jpg/02/99/04/20/360_F_299042079_vGBD7wIlSeNl7vOevWHiL93G4koMM967.jpg"
    ),
    User(
        userName = "leo",
        coverUrl = "https://plus.unsplash.com/premium_photo-1689977968861-9c91dbb16049?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8cHJvZmlsZSUyMHBpY3R1cmV8ZW58MHx8MHx8fDA%3D"
    ),
    User(
        userName = "leo2",
        coverUrl = "https://plus.unsplash.com/premium_photo-1689977968861-9c91dbb16049?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8cHJvZmlsZSUyMHBpY3R1cmV8ZW58MHx8MHx8fDA%3D"
    )
)

val mockOrganization = Organization(
    name = "NSU",
    id = UUID.randomUUID(),
    address = "г. Новосибирск, ул. Пирогова, д. 1",
    description = "НГУ - классический университет в Новосибирске, известный своей тесной интеграцией с научными институтами Сибирского отделения РАН. Университет предлагает высшее образование на 6 факультетах и в 4 институтах, сочетая естественнонаучные, инженерные и гуманитарные направления.",
    admins = mockUsersList,
    events = mockEventList,
    coverUrl = "https://sesc.nsu.ru/upload/resize_cache/iblock/b4c/919_517_2/%D0%9D%D0%93%D0%A3.jpg",
    images = listOf(
        "https://ksonline.ru/wp-content/uploads/2022/05/NSU.jpg",
        "https://ksonline.ru/wp-content/uploads/2017/03/ngu.jpg"
    ),
    isSubscribed = true,
    isMine = false
)

private val baseCovers = listOf(
    "https://sesc.nsu.ru/upload/resize_cache/iblock/b4c/919_517_2/%D0%9D%D0%93%D0%A3.jpg",
    "https://ksonline.ru/wp-content/uploads/2022/05/NSU.jpg",
    "https://ksonline.ru/wp-content/uploads/2017/03/ngu.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/2/2f/Googleplex_HQ.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/f/fa/Apple_Store.jpg",
)

val mockOrganizationsPool: List<Organization> = buildList {
    val cities = listOf("Новосибирск", "Москва", "Санкт-Петербург", "Казань", "Екатеринбург")
    repeat(100) { i ->
        val city = cities[i % cities.size]
        add(
            Organization(
                id = UUID.randomUUID(),
                name = "NSU #$i",
                coverUrl = baseCovers[i % baseCovers.size],
                address = "г. $city, ул. Пирогова, д. ${Random.nextInt(1, 50)}",
                description = "Описание организации №$i. Короткий текст о деятельности.",
                admins = mockUsersList,
                events = mockEventList,
                images = baseCovers.shuffled().take(2),
                isSubscribed = i % 3 == 0,
                isMine = i % 5 == 0
            )
        )
    }
}