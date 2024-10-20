package com.example.projeklabmobile
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "wishlist.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_WISHLIST = "wishlist"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAMA_BARANG = "nama_barang"
        private const val COLUMN_HARGA_BARANG = "harga_barang"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_WISHLIST ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAMA_BARANG TEXT, "
                + "$COLUMN_HARGA_BARANG REAL)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WISHLIST")
        onCreate(db)
    }

    // Add wishlist item
    fun addWishlistItem(item: WishlistItem): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAMA_BARANG, item.namaBarang)
        values.put(COLUMN_HARGA_BARANG, item.hargaBarang)
        return db.insert(TABLE_WISHLIST, null, values)
    }

    // Get all wishlist items
    fun getAllWishlistItems(): List<WishlistItem> {
        val itemList = ArrayList<WishlistItem>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_WISHLIST"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val item = WishlistItem(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    namaBarang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_BARANG)),
                    hargaBarang = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_HARGA_BARANG))
                )
                itemList.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return itemList
    }

    // Delete wishlist item
    fun deleteWishlistItem(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_WISHLIST, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
}
