package dz.esi.immob.api.annonce

import api.ANNONCES_END
import retrofit2.Call
import retrofit2.http.GET

interface AnnonceService {
    @GET(ANNONCES_END)
    fun listCountries(): Call<Feed>
}



