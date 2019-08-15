package dz.esi.immob.view.viewmodel

import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Source
import dz.esi.immob.repositories.Annonce
import dz.esi.immob.repositories.AnnoncesRepo
import dz.esi.immob.repositories.UserData

class AnnoncesViewModel : ViewModel() {

    private val repo = AnnoncesRepo.instance
    private val userRepo = UserData.instance
    val feed = repo.getFeed()
    val items = MutableLiveData<List<Annonce>>().also {
        it.value = feed.value
    }
    val favorites = userRepo.getFavAnnonces()
    var state: Parcelable? = null

    fun updateItems(){
        items.value = feed.value
    }
    fun filter(title: String) {
        items.value = repo.filter(title)
    }

    fun cancelFilter(){
        items.value = feed.value
    }

    fun getItemById(id: String?): Annonce? {
        return feed.value?.find {
            it.id == id
        }
    }

    fun getFavoriteAnnonces() = userRepo.getFavAnnonces()

    fun addFavAnnonce(id: String) {
        userRepo.addFavAnnonce(
            FirebaseAuth.getInstance().currentUser?.uid!!,
            feed.value?.find {
                it.id == id
            }
        )
    }

    fun deleteFavAnnonce(id: String) {
        userRepo.deleteFavAnnonce(
            FirebaseAuth.getInstance().currentUser?.uid!!,
            id
        )
    }


}