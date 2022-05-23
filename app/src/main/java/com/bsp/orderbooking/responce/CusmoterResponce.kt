package com.bsp.orderbooking.responce

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


data class CusmoterResponce (
    @SerializedName("data")
    @Expose
    var data: List<Data>? = null
){
    data class Data(
        @SerializedName("CLASS")
        @Expose
        var class_: String? = null,

        @SerializedName("CITYID")
        @Expose
        var cityid: Int? = null,

        @SerializedName("ADDRESS")
        @Expose
        var address: String? = null,

        @SerializedName("ORGNAME")
        @Expose
        var orgname: String? = null,

        @SerializedName("RANKING")
        @Expose
        var ranking: String? = null,

        @SerializedName("CATEGORY")
        @Expose
        var category: String? = null,

        @SerializedName("COMPCODE")
        @Expose
        var compcode: Int? = null,

        @SerializedName("LATITUDE")
        @Expose
        var latitude: String? = null,

        @SerializedName("REGIONID")
        @Expose
        var regionid: Int? = null,

        @SerializedName("LONGITUDE")
        @Expose
        var longitude: String? = null,

        @SerializedName("CUSTOMERID")
        @Expose
        var customerid: Int? = null,

        @SerializedName("UNITNUMBER")
        @Expose
        var unitnumber: Int? = null,

        @SerializedName("PHONENUMBER")
        @Expose
        var phonenumber: String? = null,

        @SerializedName("MOBILENUMBER")
        @Expose
        var mobilenumber: String? = null,

        @SerializedName("LICENSEEXPIRY9")
        @Expose
        var licenseexpiry9: String? = null,

        @SerializedName("LICENSENUMBER9")
        @Expose
        var licensenumber9: String? = null,

        @SerializedName("LICENSEEXPIRY11")
        @Expose
        var licenseexpiry11: String? = null,

        @SerializedName("LICENSENUMBER11")
        @Expose
        var licensenumber11: String? = null
    )
}