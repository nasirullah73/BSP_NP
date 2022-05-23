package com.bsp.orderbooking.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsp.orderbooking.db.entity.ProductEntity.Companion.TABLE_PRODUCT

@Entity(tableName = TABLE_PRODUCT)
class ProductEntity (
    @PrimaryKey(autoGenerate = true) val _id: Int?,
    @ColumnInfo(name = COL_BRANCHID) val col_branchid: Int?,
    @ColumnInfo(name = COL_COMPANYID) val col_companyid: Int?,
    @ColumnInfo(name = COL_SPOGROUPID) val col_spogroupid: Int?,
    @ColumnInfo(name = COL_PRODUCTID) val col_productid: String?,
    @ColumnInfo(name = COL_PRODUCTNAME) val col_productname: String?,
    @ColumnInfo(name = COL_UNITSIZE) val col_unitsize: String?,
    @ColumnInfo(name = COL_CARTONSIZE) val col_cartonsize: String?,
    @ColumnInfo(name = COL_UNITPRICE) val col_unitprice: Double?,
    @ColumnInfo(name = COL_RETAILPRICE) val col_retailprice: Double?,
    @ColumnInfo(name = COL_ISSHORT) val col_isshort: String?,
    @ColumnInfo(name = COL_GENERICNAME) val col_genericname: String?
    ){
        companion object{
            const val TABLE_PRODUCT = "product"
            const val COL_BRANCHID = "branchid"
            const val COL_COMPANYID = "companyid"
            const val COL_SPOGROUPID = "spogroupid"
            const val COL_PRODUCTID = "productid"
            const val COL_PRODUCTNAME = "productname"
            const val COL_UNITSIZE = "unitsize"
            const val COL_CARTONSIZE = "cartonsize"
            const val COL_UNITPRICE = "unitprice"
            const val COL_RETAILPRICE = "retailprice"
            const val COL_ISSHORT = "isshort"
            const val COL_GENERICNAME = "genericname"
        }

    }