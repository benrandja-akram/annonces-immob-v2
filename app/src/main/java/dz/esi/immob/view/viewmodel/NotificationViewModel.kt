package dz.esi.immob.view.viewmodel

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import dz.esi.immob.repositories.Annonce
import dz.esi.immob.repositories.NotificationsRepo
import dz.esi.immob.repositories.UserData

class NotificationViewModel: ViewModel() {

    val repo = NotificationsRepo.instance

    val notifications = repo.notifications

    var state: Parcelable? = null

    fun addNotification(id: String){
        repo.addNotification(id)
    }

}