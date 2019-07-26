package dz.esi.immob.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dz.esi.immob.api.annonce.Annonce
import dz.esi.immob.api.annonce.Feed
import dz.esi.immob.repositories.AnnoncesRepo
import dz.esi.immob.repositories.UserData

class AnnoncesViewModel : ViewModel(){

    private val repo = AnnoncesRepo()
    private val userRepo = UserData()
    lateinit var feed: MutableLiveData<Feed>

    fun refreshFeed(){
        feed = repo.getFeed()
    }

    fun getItemByGuid(guid: String?): Annonce?{
        return feed.value?.items?.find {
            it.guid == guid
        }
    }

    fun addFavAnnonce(guid: String){
        userRepo.addFavAnnonce(
            FirebaseAuth.getInstance().currentUser?.uid!!,
            feed.value?.items?.find {
                it.guid == guid
            }
        )
    }
    fun deleteFavAnnonce(guid: String){
        userRepo.deleteFavAnnonce(
            FirebaseAuth.getInstance().currentUser?.uid!!,
            guid
        )
    }
}