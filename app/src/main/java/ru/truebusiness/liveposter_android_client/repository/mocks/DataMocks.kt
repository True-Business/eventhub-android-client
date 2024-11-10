package ru.truebusiness.liveposter_android_client.repository.mocks

import ru.truebusiness.liveposter_android_client.data.Event
import java.util.UUID

val mockList = listOf<Event>(
    Event(
        id = UUID.randomUUID(),
        title = "Smart Picnic",
        content = "Ежегодное мероприятие с множеством развлечений, концертом и т.п.",
        startDate = "2024-12-01",
        endDate = "2024-12-02",
        location = "Академгородок, ул. Николаева, 11"),
    Event(
        id = UUID.randomUUID(),
        title = "OpenSpacePicnic",
        content = "Пикник в дворике нового корпуса НГУ посвящённый дню знаний.",
        startDate = "2024-12-01",
        endDate = "2024-12-02",
        location = "Академгородок, ул. Пирогова, 1"),
    Event(
        id = UUID.randomUUID(),
        title = "День открытых дверей в НГУ",
        content = "Выставка факультетов, общение со студентами и многое другое.",
        startDate = "2024-12-01",
        endDate = "2024-12-02",
        location = "Академгородок, ул. Пирогова, 1"),
    Event(
        id = UUID.randomUUID(),
        title = "ХАХА BATTLE НГУ",
        content = "Самое смешное юмористическое соревнование в Академгородке.",
        startDate = "2024-11-015",
        endDate = "2024-11-15",
        location = "Академгородок, Проспект Строителей, 21")
)