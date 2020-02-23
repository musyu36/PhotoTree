package com.example.photopractice

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DBHandler(context : Context, name : String?, factory: SQLiteDatabase.CursorFactory?, version : Int):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    companion object{
        private val DATABASE_NAME = "MyData.db"
        private val DATABASE_VERSION = 1

        val FOLDERS_TABLE_NAME = "Folders"
        val COLUMN_FOLDERID = "folderid"
        val COLUMN_FOLDERNAME = "foldername"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_FOLDERS_TABLE = ("CREATE TABLE $FOLDERS_TABLE_NAME(" +
                "$COLUMN_FOLDERID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_FOLDERNAME TEXT) ")
        db?.execSQL(CREATE_FOLDERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getFolders(mCtx: Context): ArrayList<Folder>{
        val qry = "Select * From $FOLDERS_TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val folders = ArrayList<Folder>()

        if(cursor.count == 0){
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show()
        }else{
            cursor.moveToFirst()
            while(!cursor.isAfterLast()){
                val folder = Folder()
                folder._folderID = cursor.getInt(cursor.getColumnIndex(COLUMN_FOLDERID))
                folder.folderName = cursor.getString(cursor.getColumnIndex(COLUMN_FOLDERNAME))
                folders.add(folder)
                cursor.moveToNext()
            }
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return folders
    }

    fun addFolder(mCtx: Context, folder: Folder){
        val values = ContentValues()
        values.put(COLUMN_FOLDERNAME, folder.folderName)

        val db = this.writableDatabase
        try{
            db.insert(FOLDERS_TABLE_NAME, null, values)
//            db.rawQuery("Insert Into $FOLDERS_TABLE_NAME ($COLUMN_FOLDERNAME, $COLUMN_MAXCREDIT) Values(?,?)")
            Toast.makeText(mCtx, "Folder Added", Toast.LENGTH_SHORT).show()
        }catch(e: Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }
}