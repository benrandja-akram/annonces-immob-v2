package dz.esi.immob.api.annonce

import java.util.*

data class Annonce (
    val title: String,
    val description: String?,
    val link: String?,
    val address: String?,
    val guid: String?,
    val image: String?,
    val publishDate: Date?
)
