package com.bsp.orderbooking.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsp.orderbooking.db.entity.ProductSale.Companion.TABLE_PRODUCT_SALE

@Entity(tableName = TABLE_PRODUCT_SALE)
class ProductSale (
    @PrimaryKey(autoGenerate = true) val _id: Int?,
    @ColumnInfo(name = COL_BOOKINGDATE) val col_bookingdate: String?,
    @ColumnInfo(name = COL_UPLOADDATE) val col_uploaddate: String?,
    @ColumnInfo(name = COL_CUSTOMERID) val col_customerid: String?,
    @ColumnInfo(name = COL_PORDUCTID) val col_porductid: String?,
    @ColumnInfo(name = ProductEntity.COL_PRODUCTNAME) val col_productname: String?,
    @ColumnInfo(name = COL_QTY) var col_qty: Int?,
    @ColumnInfo(name = ProductEntity.COL_RETAILPRICE) val col_retailprice: Double?,
    @ColumnInfo(name = COL_STATUS) var col_status: String?,
    @ColumnInfo(name = COL_USERID) val col_userid: Int?,
    @ColumnInfo(name = COL_SALEID) val col_saleid: String?,
    @ColumnInfo(name = COL_EXPORT_STATUS) val col_export_status: String?,
    @ColumnInfo(name = COL_MID) val col_mid: String?,
    @ColumnInfo(name = COL_REASON) val col_reason: String?,
    @ColumnInfo(name = COL_LATITUDE) val latitude: String?,
    @ColumnInfo(name = COL_LONGITUDE) val longitude: String?,
    @ColumnInfo(name = COL_DATASOURCE) val col_datasource: String?,
    @ColumnInfo(name = COL_CLOUDUPLOADSTATUS) val col_clouduploadstatus: String?,
    @ColumnInfo(name = COL_PID) val col_pid: String?,
    @ColumnInfo(name = COL_ORDERID) var col_orderid: String?,
    ){
        companion object{
            const val TABLE_PRODUCT_SALE = "product_sale"
            const val COL_BOOKINGDATE = "bookingdate"
            const val COL_UPLOADDATE = "uploaddate"
            const val COL_CUSTOMERID = "customerid"
            const val COL_PORDUCTID = "porductid"
            const val COL_QTY = "qty"
            const val COL_STATUS = "status"
            const val COL_USERID = "userid"
            const val COL_SALEID = "saleid"
            const val COL_EXPORT_STATUS = "export_status"
            const val COL_MID = "mid"
            const val COL_REASON = "reason"
            const val COL_LATITUDE = "latitude"
            const val COL_LONGITUDE = "longitude"
            const val COL_DATASOURCE = "datasource"
            const val COL_CLOUDUPLOADSTATUS = "clouduploadstatus"
            const val COL_PID = "pid"
            const val COL_ORDERID = "orderid"
        }

    }