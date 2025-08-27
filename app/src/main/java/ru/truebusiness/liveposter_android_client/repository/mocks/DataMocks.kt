package ru.truebusiness.liveposter_android_client.repository.mocks

import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import java.util.UUID

val mockList = listOf(
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FESTIVALS),
        title = "Smart Picnic",
        content = "Ежегодное мероприятие с множеством развлечений, концертом и т.п.",
        startDate = "2024-12-01",
        endDate = "2024-12-02",
        location = "Академгородок, ул. Николаева, 11",
        posterUrl = 1),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.FESTIVALS),
        title = "OpenSpacePicnic",
        content = "Пикник в дворике нового корпуса НГУ посвящённый дню знаний.",
        startDate = "2024-12-01",
        endDate = "2024-12-02",
        location = "Академгородок, ул. Пирогова, 1",
        posterUrl = 2),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.MEETINGS),
        title = "День открытых дверей в НГУ",
        content = "Выставка факультетов, общение со студентами и многое другое.",
        startDate = "2024-12-01",
        endDate = "2024-12-02",
        location = "Академгородок, ул. Пирогова, 1",
        posterUrl = 3),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.SHOWS),
        title = "ХАХА BATTLE НГУ",
        content = "Самое смешное юмористическое соревнование в Академгородке.",
        startDate = "2024-11-15",
        endDate = "2024-11-15",
        location = "Академгородок, Проспект Строителей, 21",
        posterUrl = 4),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.MUSIC),
        title = "3 дня дождя",
        content = "Концерт группы 3 дня дождя ",
        startDate = "2024-11-13",
        endDate = "2024-11-13",
        location = "Новосибирск, Локомотив-Арена",
        posterUrl = 5),
    Event(
        id = UUID.randomUUID(),
        category = listOf(EventCategory.ALL, EventCategory.SHOWS),
        title = "Андрей Бебуришвили",
        content = "Стендап 18+",
        startDate = "2025-02-15",
        endDate = "2025-02-15",
        location = "Новосибирск, ККК им.Маяковского",
        posterUrl = 6),
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
        posterUrl = 7),
)