package com.example.headsupprep

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

//details.dp is the database name
class DBHelper(
    context: Context
) : SQLiteOpenHelper(context, "celebrities.dp", null, 2) {
    var sqLightDatabase: SQLiteDatabase = writableDatabase
    override fun onCreate(dp: SQLiteDatabase?) {
        if (dp != null) {
            dp.execSQL("create table celebrity ( name text,taboo1 text,taboo2 text,taboo3 text,taboo4 text )")
        }

    }

    override fun onUpgrade(dp: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion && dp != null) {
            dp.execSQL("DROP TABLE IF EXISTS note")
            onCreate(dp)
        }
    }

    fun saveData(celebrityObj: CelebrityData): Long {
        val cv = ContentValues()
        cv.put("name", celebrityObj.name)
        cv.put("taboo1", celebrityObj.taboo1)
        cv.put("taboo2", celebrityObj.taboo2)
        cv.put("taboo3", celebrityObj.taboo3)


        var status = sqLightDatabase.insert("celebrity", null, cv)//status
        return status

    }

    @SuppressLint("Range")
    fun readData() :ArrayList<CelebrityData>{
        var selectQuery = "SELECT  * FROM celebrity"
        var cursor: Cursor? = null
        try {

            cursor = sqLightDatabase.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            sqLightDatabase.execSQL(selectQuery)
        }
        var notes=ArrayList<CelebrityData>()
        var name: String
        var taboo1: String
        var taboo2: String
        var taboo3: String
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {

                    name = cursor.getString(cursor.getColumnIndex("name"))
                    taboo1 = cursor.getString(cursor.getColumnIndex("taboo1"))
                    taboo2 = cursor.getString(cursor.getColumnIndex("taboo2"))
                    taboo3= cursor.getString(cursor.getColumnIndex("taboo3"))
                    notes.add(CelebrityData(name,taboo1,taboo2,taboo3))

                } while (cursor.moveToNext())
            }
        }
        return notes
    }
    fun updateCelebrity(celebrityOBJ:CelebrityData,oldName: String): Int {
        val cv = ContentValues()
        cv.put("name", celebrityOBJ.name)
        cv.put("taboo1", celebrityOBJ.taboo1)
        cv.put("taboo2", celebrityOBJ.taboo2)
        cv.put("taboo3", celebrityOBJ.taboo3)

        var rowNum = sqLightDatabase.update("celebrity", cv,"name = ?", arrayOf(oldName))
        return rowNum

    }
    fun deleteCelebrity(name: String){
        sqLightDatabase.delete("celebrity","name=?", arrayOf(name))
    }
}