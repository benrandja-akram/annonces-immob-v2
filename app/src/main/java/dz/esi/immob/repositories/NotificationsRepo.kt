package dz.esi.immob.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsRepo private constructor(){
    var _notifications = MutableLiveData<List<Annonce>>()
    val db = FirebaseFirestore.getInstance()
    val annoncesRepo = AnnoncesRepo.instance
    companion object {
        val instance = NotificationsRepo()
    }
    val notifications: MutableLiveData<List<Annonce>>
        get() {
            if (_notifications.value == null) {
                db.collection("users")
                    .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .collection("notifications")
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            return@addSnapshotListener
                        }

                        Log.i("observernotif", value?.toObjects(Annonce::class.java).toString())
                        _notifications.postValue(value?.toObjects(Annonce::class.java))
                    }
            }

            return _notifications
        }

    fun addNotification(id: String){
        annoncesRepo.findAnnonceById(id){annonce->
            annonce?.let {
                db.collection("users")
                    .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .collection("notifications")
                    .document(id)
                    .set(it)
            }

        }
    }

}