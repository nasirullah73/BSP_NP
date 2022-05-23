package com.bsp.orderbooking.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsp.orderbooking.db.entity.Schedule.Companion.TABLE_SCHDULE

@Entity(tableName = TABLE_SCHDULE)
class Schedule (
    @PrimaryKey(autoGenerate = true) val _id: Int?,
    @ColumnInfo(name = COL_EMPLOYEEID) val col_employeeid: Int?,
    @ColumnInfo(name = COL_GROUPID) val col_groupid: Int?,
    @ColumnInfo(name = COL_REEGIONID) val col_reegionid: Int?,
    @ColumnInfo(name = COL_GROUPNAME) val col_groupname: String?,
    @ColumnInfo(name = COL_REGIONDESC) val col_regiondesc: String?,
    @ColumnInfo(name = COL_WEEKDAY) val col_weekday: String?
    ){
        companion object{
            const val TABLE_SCHDULE = "schdule"
            const val COL_EMPLOYEEID = "employeeid"
            const val COL_GROUPID = "groupid"
            const val COL_REEGIONID = "reegionid"
            const val COL_GROUPNAME = "groupname"
            const val COL_REGIONDESC = "regiondesc"
            const val COL_WEEKDAY = "weekday"
        }

    }