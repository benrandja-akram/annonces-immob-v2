package dz.esi.immob.api.annonce

import java.util.*

data class Feed (val title: String, val description: String, val pubDate: String, val link: String, val items: List<Annonce>)