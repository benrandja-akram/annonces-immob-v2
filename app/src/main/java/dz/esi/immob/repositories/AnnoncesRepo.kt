package dz.esi.immob.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import dz.esi.immob.api.annonce.Annonce
import dz.esi.immob.api.annonce.AnnonceController
import dz.esi.immob.api.annonce.Feed

class AnnoncesRepo{
    var mFeed = MutableLiveData<Feed>()
    var userRepo =  UserData()

    fun getFeed(): MutableLiveData<Feed>{
        AnnonceController().fetchAnnonces{feed ->

            Log.i("tagtag", FirebaseAuth.getInstance().currentUser?.uid.toString())
            userRepo.getFavAnnonces(
                {query ->
                    query.documents.forEach{ snapshot ->
                        val annonce = snapshot.toObject<Annonce>(Annonce::class.java)
                        feed?.items?.find {a ->
                            a.guid == annonce?.guid
                        }?.favorite = 1
                    }
                    mFeed.value = feed
                },
                {}
            )
        }
        return mFeed
    }

}