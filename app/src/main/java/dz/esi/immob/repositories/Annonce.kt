package dz.esi.immob.repositories

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import dz.esi.immob.R
import java.text.SimpleDateFormat
import java.util.*



data class Annonce (
    val id: String?= null,
    val title: String? = null,
    val summary: String?= null,
    val category: String?= null,
    val type: String?= null,
    val wilaya: String?= null,
    val address: String? = "Not mentioned",
    val price: String? = "Not mentioned",
    val surface: String? = "Not mentioned",
    val contact: String? = "Not mentioned",
    val link: String? = "No Link",
    val published: Date? = null,
    var favorite: Int? = null,
    var image: String? = null
){
    val publish: String?
        get() = SimpleDateFormat("yyyy-MM-dd").format(published)


}
