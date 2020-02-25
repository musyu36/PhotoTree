package com.example.photopractice

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.photopractice.MainActivity.Companion.dbHandler

class DBHandler(context : Context, name : String?, factory: SQLiteDatabase.CursorFactory?, version : Int):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    companion object{
        private val DATABASE_NAME = "MyData.db"
        private val DATABASE_VERSION = 1

        val FOLDERS_TABLE_NAME = "Folders"
        val COLUMN_FOLDERID = "folderid"
        val COLUMN_FOLDERNAME = "foldername"

        val PHOTOS_TABLE_NAME = "Photos"
        val COLUMN_PHOTOID = "photoid"
        val COLUMN_AFFILIATIONID = "affiliationid"
        val COLUMN_MEMO = "memo"
        val COLUMN_DATE = "date"
        val COLUMN_IMAGE = "image"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_FOLDERS_TABLE = ("CREATE TABLE $FOLDERS_TABLE_NAME(" +
                "$COLUMN_FOLDERID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_FOLDERNAME TEXT) ")

        val CREATE_PHOTOS_TABLE = ("CREATE TABLE $PHOTOS_TABLE_NAME(" +
                "$COLUMN_PHOTOID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_AFFILIATIONID INTEGER, " +
                "$COLUMN_MEMO TEXT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_IMAGE BLOB) ")

        db?.execSQL(CREATE_FOLDERS_TABLE)
        db?.execSQL(CREATE_PHOTOS_TABLE)
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
            Toast.makeText(mCtx, "Folder Added", Toast.LENGTH_SHORT).show()
        }catch(e: Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }


    // キー(Type,date)を指定してmemoを取得
    fun getFolder( mCtx: Context, id : Int) : Folder {
        val qry = "Select * From $FOLDERS_TABLE_NAME WHERE $COLUMN_FOLDERID = ${id}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val folder = Folder()

        if(cursor.count == 0){
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show()
        }else{
            cursor.moveToFirst()
            folder._folderID = cursor.getInt(cursor.getColumnIndex(COLUMN_FOLDERID))
            folder.folderName = cursor.getString(cursor.getColumnIndex(COLUMN_FOLDERNAME))
            cursor.close()
            db.close()
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        return folder
    }

    //写真追加
    fun addPhoto(mCtx: Context, photo: Photo){
        Log.v("###", "in addPhoto 1 ")
        val valuesPhoto = ContentValues()
        Log.v("###", "in addPhoto 2 ")
        valuesPhoto.put(COLUMN_AFFILIATIONID, photo.affiliationID)
        Log.v("###", "in addPhoto 3 ")
        valuesPhoto.put(COLUMN_MEMO, photo.memo)
        valuesPhoto.put(COLUMN_DATE, photo.date)
        valuesPhoto.put(COLUMN_IMAGE, photo.image)
        Log.v("###", "in addPhoto 4 ")

        val db = this.writableDatabase
        Log.v("###", "in addPhoto 5 ")
        try{
            db.insert(PHOTOS_TABLE_NAME, null, valuesPhoto)
            Log.v("###", "in addPhoto 6 ")
            Toast.makeText(mCtx, "Photo Added", Toast.LENGTH_SHORT).show()
            Log.v("###", "in addPhoto 7 ")
        }catch(e: Exception){
            Log.v("###", "in addPhoto 8 ")
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
            Log.v("###", "in addPhoto 9 ")
        }
        Log.v("###", "in addPhoto 10 ")
        db.close()
    }



    fun getPhotos(mCtx: Context, folderId: Int): ArrayList<Photo>{
        val qry = "Select * From $PHOTOS_TABLE_NAME WHERE $COLUMN_AFFILIATIONID = ${folderId}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val photos = ArrayList<Photo>()

        if(cursor.count == 0){
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show()
        }else{
            cursor.moveToFirst()
            while(!cursor.isAfterLast()){
                val photo = Photo()
                photo._photoID = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTOID))
                photo.affiliationID = cursor.getInt(cursor.getColumnIndex(COLUMN_AFFILIATIONID))
                photo.memo = cursor.getString(cursor.getColumnIndex(COLUMN_MEMO))
                photo.date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))

                photos.add(photo)
                cursor.moveToNext()
            }
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return photos
    }



}