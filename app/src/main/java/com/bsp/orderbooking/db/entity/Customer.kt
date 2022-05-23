package com.bsp.orderbooking.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsp.orderbooking.db.entity.Customer.Companion.TABLE_CUSTOMER

@Entity(tableName = TABLE_CUSTOMER)
class Customer (
    @PrimaryKey(autoGenerate = true) val _id: Int?,
    @ColumnInfo(name = COL_COMPCODE) val compcode: String?,
    @ColumnInfo(name = COL_UNITNUMBER) val unitnumber: String?,
    @ColumnInfo(name = COL_CUSTOMERID) val customerid: String?,
    @ColumnInfo(name = COL_ORGNAME) val orgname: String?,
    @ColumnInfo(name = COL_ADDRESS) val address: String?,
    @ColumnInfo(name = COL_CITYID) val cityid: String?,
    @ColumnInfo(name = COL_REGIONID) val regionid: String?,
    @ColumnInfo(name = COL_CATEGORY) val category: String?,
    @ColumnInfo(name = COL_CLASS) val _class: String?,
    @ColumnInfo(name = COL_RANKING) val ranking: String?,
    @ColumnInfo(name = COL_PHONENUMBER) val phonenumber: String?,
    @ColumnInfo(name = COL_MOBILENUMBER) val mobilenumber: String?,
    @ColumnInfo(name = COL_LICENSENUMBER9) val licensenumber9: String?,
    @ColumnInfo(name = COL_LICENSEEXPIRY9) val licenseexpiry9: String?,
    @ColumnInfo(name = COL_LICENSENUMBER11) val licensenumber11: String?,
    @ColumnInfo(name = COL_LICENSEEXPIRY11) val licenseexpiry11: String?,
    @ColumnInfo(name = COL_LATITUDE) val latitude: String?,
    @ColumnInfo(name = COL_LONGITUDE) val longitude: String?,
    ){
        companion object{
            const val TABLE_CUSTOMER = "customers"
            const val COL_COMPCODE = "compcode"
            const val COL_UNITNUMBER = "unitnumber"
            const val COL_CUSTOMERID = "customerid"
            const val COL_ORGNAME = "orgname"
            const val COL_ADDRESS = "address"
            const val COL_CITYID = "cityid"
            const val COL_REGIONID = "regionid"
            const val COL_CATEGORY = "category"
            const val COL_CLASS = "c_class"
            const val COL_RANKING = "ranking"
            const val COL_PHONENUMBER = "phonenumber"
            const val COL_MOBILENUMBER = "mobilenumber"
            const val COL_LICENSENUMBER9 = "licensenumber9"
            const val COL_LICENSEEXPIRY9 = "licenseexpiry9"
            const val COL_LICENSENUMBER11 = "licensenumber11"
            const val COL_LICENSEEXPIRY11 = "licenseexpiry11"
            const val COL_LATITUDE = "latitude"
            const val COL_LONGITUDE = "longitude"
        }

    }