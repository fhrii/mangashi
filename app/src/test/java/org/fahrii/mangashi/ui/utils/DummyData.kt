package org.fahrii.mangashi.ui.utils

import org.fahrii.mangashi.core.domain.model.Manga

internal object DummyData {
    fun getManga() = Manga(
        "martial-peak/",
        "Martial Peak",
        "https://cover.komiku.id/wp-content/uploads/2018/08/Komik-Martial-Peak.jpeg?resize=450,235&quality=60",
        "Manhua",
        null,
        null,
        null,
        false
    )

    fun getMangaList() = listOf(
        Manga(
            "martial-peak/",
            "Martial Peak",
            "https://cover.komiku.id/wp-content/uploads/2018/08/Komik-Martial-Peak.jpeg?resize=450,235&quality=60",
            "Manhua",
            null,
            null,
            null,
            false
        )
    )
}