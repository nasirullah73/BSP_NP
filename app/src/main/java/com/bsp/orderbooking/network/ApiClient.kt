package com.bsp.orderbooking.network

import com.bsp.orderbooking.responce.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface ApiClient {

    @GET(ServiceUrls.GET_USERS)
    suspend fun login(): Response<UsersResponse>

    @GET(ServiceUrls.GET_URLS)
    suspend fun getUrls(): Response<UrlResponse>

    @GET
    suspend fun getCustomers(
        @Url url:String?
    ) : Response<CusmoterResponce>

    @GET
    suspend fun getProducts(
        @Url url:String?
    ) : Response<ProductResponce>

    @GET
    suspend fun getSchduleCustomer(
        @Url url:String?
    ) : Response<ScheduleCustomerResponse>

    @GET
    suspend fun getSchdule(
        @Url url:String?
    ) : Response<ScheduleResponse>


}