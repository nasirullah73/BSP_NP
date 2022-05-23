package com.bsp.orderbooking.responce

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class UrlResponse (
    @SerializedName("data")
    @Expose
    var data: List<Data>? = null
){
    data class Data (
        @SerializedName("schdule")
        @Expose
        var schdule: String? = null,

        @SerializedName("customor")
        @Expose
        var customor: String? = null,

        @SerializedName("products")
        @Expose
        var products: String? = null,

        @SerializedName("companyid")
        @Expose
        var companyid: Int? = null,

        @SerializedName("custmerschdule")
        @Expose
        var custmerSchdule: String? = null
    )
}