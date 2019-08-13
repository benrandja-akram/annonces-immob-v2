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
    val price: String? = null,
    val surface: String? = null,
    val contact: String? = null,
    val published: Date? = null,
    var favorite: Int? = null,
    var image: String? = null
){
    val publish: String?
        get() = SimpleDateFormat("yyyy-MM-dd").format(published)

//    @BindingAdapter("image")
//    fun loadImage(view: ImageView, url: String){
//        Glide.with(view)
//            .load(url)
//            .error(R.drawable.no_image_available)
//            .fitCenter()
//            .into(view)
//    }

}
