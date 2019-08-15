package dz.esi.immob.repositories

import androidx.preference.PreferenceDataStore

class UserPreferenceDataStore: PreferenceDataStore(){
    val userRepo = UserData.instance

    override fun getString(key: String?, defValue: String?): String? {
        return userRepo.wilaya.value.also(::println)
    }

    override fun putString(key: String?, value: String?) {
        userRepo.setFavWilaya(value)
    }

}