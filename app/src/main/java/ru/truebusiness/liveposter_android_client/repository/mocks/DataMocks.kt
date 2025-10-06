package ru.truebusiness.liveposter_android_client.repository.mocks

import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.data.Friend
import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.data.User
import java.util.UUID

val mockEventList = listOf(
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FESTIVALS),
        title = "Smart Picnic",
        content = "Ежегодное мероприятие с множеством развлечений, концертом и т.п.",
        startDate = "2024-12-01",
        endDate = "2024-12-02",
        location = "Академгородок, ул. Николаева, 11",
        posterUrl = "https://nadvizh.ru/media/events_img/67/smart-piknik_logo.jpg "),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FESTIVALS),
        title = "OpenSpacePicnic",
        content = "Пикник в дворике нового корпуса НГУ посвящённый дню знаний.",
        startDate = "2024-12-01",
        endDate = "2024-12-02",
        location = "Академгородок, ул. Пирогова, 1",
        posterUrl = "https://static.tildacdn.com/tild3230-3163-4439-b733-366633643931/DSC_4453.jpg"),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.MEETINGS),
        title = "День открытых дверей в НГУ",
        content = "Выставка факультетов, общение со студентами и многое другое.",
        startDate = "2024-12-01",
        endDate = "2024-12-02",
        location = "Академгородок, ул. Пирогова, 1",
        posterUrl = "https://static.tildacdn.com/tild3337-6465-4835-a130-623838656562/1680900--------2.jpg"),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.SHOWS),
        title = "ХАХА BATTLE НГУ",
        content = "Самое смешное юмористическое соревнование в Академгородке.",
        startDate = "2024-11-15",
        endDate = "2024-11-15",
        location = "Академгородок, Проспект Строителей, 21",
        posterUrl = "https://sun9-12.userapi.com/Tjc3E_Yysjm5NfzuxndPMHgTXAO1S7T6-Ks87Q/iSYEIaVpiX4.jpg"),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.MUSIC),
        title = "3 дня дождя",
        content = "Концерт группы 3 дня дождя ",
        startDate = "2024-11-13",
        endDate = "2024-11-13",
        location = "Новосибирск, Локомотив-Арена",
        posterUrl = "https://geopro-photos.storage.yandexcloud.net/resize_cache/48245238/e14e74968349be09ee1354fc509cee5d/iblock/aea/aeabbb2f275ef6812990534d33cb64d2/photo_2024_08_29-00.27.34.jpeg"),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.SHOWS),
        title = "Андрей Бебуришвили",
        content = "Стендап 18+",
        startDate = "2025-02-15",
        endDate = "2025-02-15",
        location = "Новосибирск, ККК им.Маяковского",
        posterUrl = "https://live.mts.ru/image/full/505d782c-73eb-2170-a0e5-b28853d839b0.jpg"),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FILMS),
        title = "Веном: Последний танец",
        content = "Приспособившись к совместному существованию, Эдди и Веном стали друзьями и " +
                "вместе сражаются со злодеями. Но теперь за Эдди охотятся военные, а за Веномом — " +
                "его инопланетные сородичи, угрожающие всему живому.",
        startDate = "2024-10-24",
        endDate = "2024-11-30",
        location = "Академгородок, ул. Кутателадзе, 4/4",
        posterUrl = "https://images.iptv.rt.ru/images/cvj4k3rir4sqiatdopl0.jpg"),
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