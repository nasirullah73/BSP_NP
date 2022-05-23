package com.bsp.orderbooking.models

class Chemist(
    var ordered: Boolean?,var item : Customer
){
    data class Customer (
        var _id: Int?,
        var compcode: String?,
        var unitnumber: String?,
        var customerid: String?,
        var orgname: String?,
        var address: String?,
        var cityid: String?,
        var regionid: String?,
        var category: String?,
        var _class: String?,
        var ranking: String?,
        var phonenumber: String?,
        var mobilenumber: String?,
        var licensenumber9: String?,
        var licenseexpiry9: String?,
        var licensenumber11: String?,
        var licenseexpiry11: String?,
        var latitude: String?,
        var LONGITUDE: String?,
    )
}