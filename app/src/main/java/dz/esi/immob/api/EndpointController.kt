package api

open class EndpointController<T>(serviceClass: Class<T>){
    val service: T = retrofit.create(serviceClass)
}