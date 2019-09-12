package dz.esi.immob.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging




class UserData private constructor(val uid: String?) {
    val db = FirebaseFirestore.getInstance()
    var annonces = MutableLiveData<List<Annonce>>()
    val wilaya = MutableLiveData<String>()

    companion object {
        private var lastInsance: UserData? = null

        val instance :UserData
            get(){
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if(lastInsance?.uid !==  uid) lastInsance = UserData(uid)

                return lastInsance!!
            }
    }

//    init {
//        getFavAnnonces()
//        getFavWilaya()
//    }

    fun setFavWilaya(wilaya: String?) {
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .set(User(wilaya), SetOptions.merge())
    }

    fun putString(key: String?, value: String?){
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .set(mapOf(key to value), SetOptions.merge())
    }

    fun getFavWilaya(): MutableLiveData<String> {

        if(wilaya.value == null) {
            db.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    wilaya.postValue(value?.toObject(User::class.java)?.wilaya)
                }
        }

        return wilaya
    }

    fun addFavAnnonce(userId: String, mAnnonce: Annonce?) {
        Log.i("mAnnonce", mAnnonce.toString())
        mAnnonce?.let { annonce ->
            db.collection("users")
                .document(userId)
                .collection("annonces")
                .document(annonce.id!!)
                .set(annonce)
                .addOnSuccessListener { documentReference ->
                    Log.i("addFavAnnonce", "prefred annocne $annonce")
                }
                .addOnFailureListener { e ->
                    throw e
                }
        }
    }

    fun deleteFavAnnonce(userId: String, guid: String) {

        db.collection("users")
            .document(userId)
            .collection("annonces")
            .document(guid)
            .delete()
            .addOnSuccessListener { documentReference ->
                Log.i("deletedPrefredAnnonce", "deleted annocne")
            }
            .addOnFailureListener { e ->
                throw e
            }

    }

    fun getFavAnnonces(): LiveData<List<Annonce>> {

        if (annonces.value == null) {
            db.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                .collection("annonces")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    annonces.postValue(value?.toObjects(Annonce::class.java))
                }
        }

        return annonces
    }

    fun subscribeToTopic(topic: String, onSuccess: Runnable){
        Log.i("fcmservice", "subscribing ...")

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess.run()
                    Log.i("fcmservice", "scuccesfult subscription ... ")

                }else
                    Log.i("fcmservice", "failed subscription ... ")

            }
            .addOnFailureListener{
                Log.i("fcmservice", "addOnFailureListener subscription ... ")

            }

    }



}

data class User(val wilaya: String? = null)
