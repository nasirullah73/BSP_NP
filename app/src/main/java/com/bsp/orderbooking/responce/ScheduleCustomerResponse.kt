package com.bsp.orderbooking.responce

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ScheduleCustomerResponse (
    @SerializedName("data")
    @Expose
    var data: List<Data>? = null
){
    data class Data (
        @SerializedName("REGIONID")
        @Expose
        var regionid: Int? = null,

        @SerializedName("CUSTOMERID")
        @Expose
        var customerid: Int? = null
    )
}