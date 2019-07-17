package dz.esi.immob.api.annonce

data class Annonce (
    val title: String,
    val description: String?,
    val link: String?,
    val address: String?,
    val guid: String?,
    val image: String?
)
