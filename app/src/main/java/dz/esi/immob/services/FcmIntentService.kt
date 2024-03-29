package dz.esi.immob.services


import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dz.esi.immob.AnnonceDetails
import dz.esi.immob.R
import dz.esi.immob.repositories.NotificationsRepo
import android.graphics.BitmapFactory

import java.net.URL
import kotlin.random.Random
import android.media.RingtoneManager



const val CHANNEL_ID = "dz.immob.annonces"


class FcmIntentService : FirebaseMessagingService() {
    private val TAG = "fcmservice"
    private var id = 0
    private val notificationRepo = NotificationsRepo.instance

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        Log.d(TAG, "From: " + remoteMessage!!.from!!)

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            val title = remoteMessage.data["title"]
            val annonceId = remoteMessage.data["id"]
            val contentText = remoteMessage.data["contentText"]
            val image = remoteMessage.data["image"]
            showNotification(annonceId, title, contentText, Random.nextInt(0, Int.MAX_VALUE), image)
            annonceId?.apply {
                notificationRepo.addNotification(this)
            }
        }
    }

    private fun showNotification(id: String?, title: String?, contentText: String?, notifId: Int, image: String?) {

        val resultIntent = Intent(this, AnnonceDetails::class.java)
        resultIntent.putExtra("id", id)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.home_256)
            .setContentTitle(title)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)

            .apply {
                image?.let {image ->
                    val url = URL(image)
                    val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    setStyle(
                        NotificationCompat
                            .BigPictureStyle()
                            .bigPicture(bitmap)
                    )
                }

            }

        NotificationManagerCompat.from(this).notify(notifId, builder.build())
    }

}

