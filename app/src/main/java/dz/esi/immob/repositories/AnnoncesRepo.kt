package dz.esi.immob.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import api.ApiConsumer
import dz.esi.immob.api.annonce.AnnonceController
import dz.esi.immob.api.annonce.Feed

class AnnoncesRepo{
    var mFeed = MutableLiveData<Feed>()

    fun getFeed(): MutableLiveData<Feed>{
        AnnonceController().fetchAnnonces{
            mFeed.value = it
            Log.i("feed", mFeed.value.toString())
        }
        return mFeed
    }

}