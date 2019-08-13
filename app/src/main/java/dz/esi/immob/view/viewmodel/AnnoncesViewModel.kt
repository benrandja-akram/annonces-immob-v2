package dz.esi.immob.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Source
import dz.esi.immob.repositories.Annonce
import dz.esi.immob.repositories.AnnoncesRepo
import dz.esi.immob.repositories.UserData

class AnnoncesViewModel : ViewModel(){

    private val repo = AnnoncesRepo.instance
    private val userRepo = UserData.instance
    val feed = repo.getFeed()

    fun filter(criteria: Map<String, Any>) = repo.filter(criteria)





    fun getItemById(id: String?): Annonce?{
        return feed.value?.find {
            it.id == id
        }
    }

    fun addFavAnnonce(id: String){
        userRepo.addFavAnnonce(
            FirebaseAuth.getInstance().currentUser?.uid!!,
            feed.value?.find {
                it.id == id
            }
        )
    }

    fun deleteFavAnnonce(id: String){
        userRepo.deleteFavAnnonce(
            FirebaseAuth.getInstance().currentUser?.uid!!,
            id
        )
    }

}