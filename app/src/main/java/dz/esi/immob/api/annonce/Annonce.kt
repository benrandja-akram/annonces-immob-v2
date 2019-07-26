package dz.esi.immob.api.annonce

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

data class Annonce (
    val title: String? = null,
    val description: String?= null,
    val link: String?= null,
    val address: String?= null,
    val guid: String?= null,
    val image: String?= null,
    val publishDate: Date?= null,
    var favorite: Int?= null
){
    val publish: String?
        get() = SimpleDateFormat("yyyy-MM-dd").format(publishDate)
}

