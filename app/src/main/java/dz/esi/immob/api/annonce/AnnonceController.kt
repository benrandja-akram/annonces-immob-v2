package dz.esi.immob.api.annonce

import api.ApiConsumer
import api.ApiController
import api.EndpointController

class AnnonceController: EndpointController<AnnonceService>(AnnonceService::class.java) {

    fun fetchAnnonces(consume: ApiConsumer<Feed>){
        val call = service.listCountries()
        val controller = ApiController<Feed>(consume)
        call.enqueue(controller)
    }
}