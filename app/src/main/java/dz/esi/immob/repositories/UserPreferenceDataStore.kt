package dz.esi.immob.repositories

import android.content.Intent
import androidx.preference.PreferenceDataStore
import dz.esi.immob.services.FcmIntentService

class UserPreferenceDataStore: PreferenceDataStore(){

    override fun getString(key: String?, defValue: String?): String? {
        return UserData.instance.wilaya.value.also(::println)
    }

    override fun putString(key: String?, value: String?) {

        UserData.instance.setFavWilaya(value)
    }

}