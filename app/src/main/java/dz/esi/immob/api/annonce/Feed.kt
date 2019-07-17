package dz.esi.immob.api.annonce

import java.util.*

data class Feed (var title: String, var description: String, var pubDate: String, val link: String, var items: List<Annonce>?)