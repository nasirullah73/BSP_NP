package com.bsp.orderbooking.responce

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ScheduleResponse (
    @SerializedName("data")
    @Expose
    var data: List<Data>? = null
){
    data class Data (
        @SerializedName("EMPLOYEEID")
        @Expose
        var employeeid: Int? = null,

        @SerializedName("GROUPID")
        @Expose
        var groupid: Int? = null,

        @SerializedName("REGIONID")
        @Expose
        var regionid: Int? = null,

        @SerializedName("GROUPNAME")
        @Expose
        var groupname: String? = null,

        @SerializedName("REGIONDESC")
        @Expose
        var regiondesc: String? = null,

        @SerializedName("WEEKDAY")
        @Expose
        var weekday: String? = null
    )
}