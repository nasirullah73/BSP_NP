package com.bsp.orderbooking.responce

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class UsersResponse (
    @SerializedName("data")
    @Expose
    var data: List<Data>? = null
){
    data class Data (
        @SerializedName("id")
        @Expose
        var id: String? = null,

        @SerializedName("name")
        @Expose
        var name: String? = null,

        @SerializedName("passwaor")
        @Expose
        var passwaor: String? = null,

        @SerializedName("companyEmail")
        @Expose
        var companyEmail: String? = null,

        @SerializedName("companyNumber")
        @Expose
        var companyNumber: Int? = null
    )
}