package com.bsp.orderbooking.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsp.orderbooking.db.entity.OrderEntity.Companion.TABLE_ORDER

@Entity(tableName = TABLE_ORDER)
class OrderEntity(
    @PrimaryKey(autoGenerate = true) val _id: Int?,
    @ColumnInfo(name = COL_DATE) val col_date: String?,
    @ColumnInfo(name = COL_QTY) val col_qty: String?,
    @ColumnInfo(name = COL_PRICE) val col_price: String?,
    @ColumnInfo(name = COL_CUSTOMERID) val col_customerid: String?,
    @ColumnInfo(name = COL_CUSTOMERNAME) val col_customername: String?,
) {
    companion object {
        const val TABLE_ORDER = "orders"
        const val COL_DATE = "date"
        const val COL_QTY = "qty"
        const val COL_PRICE = "price"
        const val COL_CUSTOMERID = "customerid"
        const val COL_CUSTOMERNAME = "customername"
    }

}