package com.bsp.orderbooking.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsp.orderbooking.db.entity.CustomerSchedule.Companion.TABLE_C_SCHDULE

@Entity(tableName = TABLE_C_SCHDULE)
class CustomerSchedule (
    @PrimaryKey(autoGenerate = true) val _id: Int?,
    @ColumnInfo(name = COL_REGIONID) val col_regionid: Int?,
    @ColumnInfo(name = COL_CUSTOMERID) val col_customerid: Int?,
    ){
        companion object{
            const val TABLE_C_SCHDULE = "cussch"
            const val COL_REGIONID = "regionid"
            const val COL_CUSTOMERID = "customerid"
        }

    }