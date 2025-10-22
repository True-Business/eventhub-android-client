package ru.truebusiness.liveposter_android_client.repository.mocks

import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.data.EventStatus
import ru.truebusiness.liveposter_android_client.data.Friend
import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.data.User
import java.util.UUID

// Mock current user ID
val mockCurrentUserId = UUID.fromString("12345678-1234-1234-1234-123456789abc")

val mockEventList = listOf(
    // Events organized by current user
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FESTIVALS),
        title = "Мой Фестиваль",
        content = "Ежегодное мероприятие с множеством развлечений, концертом и т.п.",
        startDate = "2025-05-07T13:00:00",
        endDate = "2025-05-07T18:00:00",
        location = "Академгородок, ул. Николаева, 11",
        posterUrl = "https://nadvizh.ru/media/events_img/67/smart-piknik_logo.jpg",
        price = 500.0,
        duration = 300, // 5 hours
        organizer = "NSU Events",
        organizerId = mockCurrentUserId,
        isUserParticipating = true,
        eventStatus = EventStatus.PLANNED,
        isPublic = true
    ),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.MEETINGS),
        title = "Моя Конференция",
        content = "Выставка факультетов, общение со студентами и многое другое.",
        startDate = "2025-06-10T10:00:00",
        endDate = "2025-06-10T15:00:00",
        location = "Академгородок, ул. Пирогова, 1",
        posterUrl = "https://static.tildacdn.com/tild3337-6465-4835-a130-623838656562/1680900--------2.jpg",
        price = null, // free
        duration = 300, // 5 hours
        organizer = "НГУ",
        organizerId = mockCurrentUserId,
        isUserParticipating = true,
        eventStatus = EventStatus.PLANNED,
        isPublic = true
    ),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.SHOWS),
        title = "Мое Шоу",
        content = "Самое смешное юмористическое соревнование в Академгородке.",
        startDate = "2024-11-15T19:00:00",
        endDate = "2024-11-15T21:00:00",
        location = "Академгородок, Проспект Строителей, 21",
        posterUrl = "https://sun9-12.userapi.com/Tjc3E_Yysjm5NfzuxndPMHgTXAO1S7T6-Ks87Q/iSYEIaVpiX4.jpg",
        price = 300.0,
        duration = 120, // 2 hours
        organizer = "Студенческий совет НГУ",
        organizerId = mockCurrentUserId,
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
        startDate = "2025-12-01T20:00:00",
        endDate = "2025-12-01T23:00:00",
        location = "Новосибирск, Локомотив-Арена",
        posterUrl = "https://geopro-photos.storage.yandexcloud.net/resize_cache/48245238/e14e74968349be09ee1354fc509cee5d/iblock/aea/aeabbb2f275ef6812990534d33cb64d2/photo_2024_08_29-00.27.34.jpeg",
        price = 1500.0,
        duration = 180, // 3 hours
        organizer = "Концертное агентство",
        organizerId = mockCurrentUserId,
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
        startDate = "2025-04-15T12:00:00",
        endDate = "2025-04-15T16:00:00",
        location = "Академгородок, ул. Пирогова, 1",
        posterUrl = "https://static.tildacdn.com/tild3230-3163-4439-b733-366633643931/DSC_4453.jpg",
        price = null, // free
        duration = 240, // 4 hours
        organizer = "НГУ",
        organizerId = UUID.randomUUID(), // Different organizer
        isUserParticipating = true,
        eventStatus = EventStatus.COMPLETED,
        isPublic = true
    ),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.SHOWS),
        title = "Андрей Бебуришвили",
        content = "Стендап 18+",
        startDate = "2025-02-15T19:00:00",
        endDate = "2025-02-15T21:00:00",
        location = "Новосибирск, ККК им.Маяковского",
        posterUrl = "https://live.mts.ru/image/full/505d782c-73eb-2170-a0e5-b28853d839b0.jpg",
        price = 800.0,
        duration = 120, // 2 hours
        organizer = "Комedy Club",
        organizerId = UUID.randomUUID(), // Different organizer
        isUserParticipating = true,
        eventStatus = EventStatus.PLANNED,
        isPublic = true
    ),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FILMS),
        title = "Веном: Последний танец",
        content = "Приспособившись к совместному существованию, Эдди и Веном стали друзьями и " +
                "вместе сражаются со злодеями. Но теперь за Эдди охотятся военные, а за Веномом — " +
                "его инопланетные сородичи, угрожающие всему живому.",
        startDate = "2024-10-24T14:00:00",
        endDate = "2024-10-24T16:30:00",
        location = "Академгородок, ул. Кутателадзе, 4/4",
        posterUrl = "https://images.iptv.rt.ru/images/cvj4k3rir4sqiatdopl0.jpg",
        price = 250.0,
        duration = 150, // 2.5 hours
        organizer = "Кинотеатр Имя",
        organizerId = UUID.randomUUID(), // Different organizer
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
)