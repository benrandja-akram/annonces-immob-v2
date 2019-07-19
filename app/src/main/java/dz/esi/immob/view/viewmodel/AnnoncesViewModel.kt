package dz.esi.immob.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dz.esi.immob.api.annonce.Annonce
import dz.esi.immob.api.annonce.Feed
import dz.esi.immob.repositories.AnnoncesRepo

class AnnoncesViewModel : ViewModel(){

    private val repo = AnnoncesRepo()

    lateinit var feed: MutableLiveData<Feed>

    fun refreshFeed(){
        feed = repo.getFeed()
    }

    fun getItemByGuid(guid: String?): Annonce?{
        return feed.value?.items?.find {
            it.guid == guid
        }
    }

}