package com.bsp.orderbooking.responce

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ProductResponce (
    @SerializedName("data")
    @Expose
    var data: List<Data>? = null
){
    data class Data (
        @SerializedName("ISSHORT")
        @Expose
        var isshort: String? = null,

        @SerializedName("BRANCHID")
        @Expose
        var branchid: Int? = null,

        @SerializedName("UNITSIZE")
        @Expose
        var unitsize: String? = null,

        @SerializedName("COMPANYID")
        @Expose
        var companyid: Int? = null,

        @SerializedName("PRODUCTID")
        @Expose
        var productid: Int? = null,

        @SerializedName("UNITPRICE")
        @Expose
        var unitprice: Double? = null,

        @SerializedName("CARTONSIZE")
        @Expose
        var cartonsize: String? = null,

        @SerializedName("SPOGROUPID")
        @Expose
        var spogroupid: Int? = null,

        @SerializedName("GENERICNAME")
        @Expose
        var genericname: String? = null,

        @SerializedName("PRODUCTNAME")
        @Expose
        var productname: String? = null,

        @SerializedName("RETAILPRICE")
        @Expose
        var retailprice: Double? = null
    )
}