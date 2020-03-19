package com.phototree.PhotoTree

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

        val PHOTOS_TABLE_NAME = "Photos"
        val COLUMN_PHOTOID = "photoid"
        val COLUMN_AFFILIATIONID = "affiliationid"
        val COLUMN_MEMO = "memo"
        val COLUMN_DATE = "date"
        val COLUMN_TIME = "time"
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
                "$COLUMN_TIME TEXT, " +
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
            Toast.makeText(mCtx, "フォルダはまだありません", Toast.LENGTH_SHORT).show()
        }else{
            cursor.moveToFirst()
            while(!cursor.isAfterLast()){
                val folder = Folder()
                folder._folderID = cursor.getInt(cursor.getColumnIndex(COLUMN_FOLDERID))
                folder.folderName = cursor.getString(cursor.getColumnIndex(COLUMN_FOLDERNAME))
                folders.add(folder)
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return folders
    }

    //フォルダ追加
    fun addFolder(mCtx: Context, folder: Folder){
        val values = ContentValues()
        values.put(COLUMN_FOLDERNAME, folder.folderName)

        val db = this.writableDatabase
        try{
            db.insert(FOLDERS_TABLE_NAME, null, values)
            Toast.makeText(mCtx, "フォルダを追加しました", Toast.LENGTH_SHORT).show()
        }catch(e: Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    //フォルダ編集
    fun updateFolder(mCtx: Context, folder: Folder){
        val values = ContentValues()
        values.put(COLUMN_FOLDERNAME, folder.folderName)
        val db = this.writableDatabase
        val folderId = folder._folderID
        try{
            db.update(FOLDERS_TABLE_NAME, values, "$COLUMN_FOLDERID = ?", arrayOf(folderId.toString()))
            Toast.makeText(mCtx, "編集しました", Toast.LENGTH_SHORT).show()
        }catch(e: Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    //フォルダ削除
    fun deleteFolder(mCtx: Context, folder: Folder){
        val qry = "DELETE FROM $FOLDERS_TABLE_NAME WHERE $COLUMN_FOLDERID = ${folder._folderID}"
        val db = this.writableDatabase
        try{
            db.execSQL(qry)
        }catch(e: Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }


    //フォルダ取得
    fun getFolder( mCtx: Context, id : Int) : Folder {
        val qry = "Select * From $FOLDERS_TABLE_NAME WHERE $COLUMN_FOLDERID = ${id}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val folder = Folder()

        if(cursor.count == 0){

        }else{
            cursor.moveToFirst()
            folder._folderID = cursor.getInt(cursor.getColumnIndex(COLUMN_FOLDERID))
            folder.folderName = cursor.getString(cursor.getColumnIndex(COLUMN_FOLDERNAME))
            cursor.close()
            db.close()
        }
        return folder
    }

    //写真追加
    fun addPhoto(mCtx: Context, photo: Photo){
        val valuesPhoto = ContentValues()
        valuesPhoto.put(COLUMN_AFFILIATIONID, photo.affiliationID)
        valuesPhoto.put(COLUMN_MEMO, photo.memo)
        valuesPhoto.put(COLUMN_DATE, photo.date)
        valuesPhoto.put(COLUMN_TIME, photo.time)
        valuesPhoto.put(COLUMN_IMAGE, photo.image)

        val db = this.writableDatabase
        try{
            db.insert(PHOTOS_TABLE_NAME, null, valuesPhoto)
            Toast.makeText(mCtx, "写真を追加しました", Toast.LENGTH_SHORT).show()
        }catch(e: Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }



    //フォルダ内の写真を取得
    fun getPhotos(mCtx: Context, folderId: Int): ArrayList<Photo>{
        val qry = "Select * From $PHOTOS_TABLE_NAME WHERE $COLUMN_AFFILIATIONID = ${folderId}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val photos = ArrayList<Photo>()

        if(cursor.count == 0){
            Toast.makeText(mCtx, "写真はまだありません", Toast.LENGTH_SHORT).show()
        }else{
            cursor.moveToFirst()
            while(!cursor.isAfterLast()){
                val photo = Photo()
                photo._photoID = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTOID))
                photo.affiliationID = cursor.getInt(cursor.getColumnIndex(COLUMN_AFFILIATIONID))
                photo.memo = cursor.getString(cursor.getColumnIndex(COLUMN_MEMO))
                photo.date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                photo.time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME))
                photo.image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))

                photos.add(photo)
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return photos
    }

    //写真取得
    fun getPhoto( mCtx: Context, id : Int) : Photo {
        val qry = "Select * From $PHOTOS_TABLE_NAME WHERE $COLUMN_PHOTOID = ${id}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val photo = Photo()

        if(cursor.count == 0){
        }else{
            cursor.moveToFirst()
            photo._photoID = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTOID))
            photo.affiliationID = cursor.getInt(cursor.getColumnIndex(COLUMN_AFFILIATIONID))
            photo.memo = cursor.getString(cursor.getColumnIndex(COLUMN_MEMO))
            photo.date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
            photo.time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME))
            photo.image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))

            cursor.close()
            db.close()
        }
        return photo
    }

    //写真編集
    fun updatePhoto(mCtx: Context, photo: Photo){
        val valuesPhoto = ContentValues()
        valuesPhoto.put(COLUMN_MEMO, photo.memo)
        valuesPhoto.put(COLUMN_DATE, photo.date)
        valuesPhoto.put(COLUMN_TIME, photo.time)
        valuesPhoto.put(COLUMN_IMAGE, photo.image)

        val db = this.writableDatabase
        val photoId = photo._photoID
        try{
            db.update(PHOTOS_TABLE_NAME, valuesPhoto, "$COLUMN_PHOTOID = ?", arrayOf(photoId.toString()))
            Toast.makeText(mCtx, "編集しました", Toast.LENGTH_SHORT).show()
        }catch(e: Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    //写真削除
    fun deletePhoto(mCtx: Context, photo: Photo){
        val qry = "DELETE FROM $PHOTOS_TABLE_NAME WHERE $COLUMN_PHOTOID = ${photo._photoID}"
        val db = this.writableDatabase
        try{
            db.execSQL(qry)
        }catch(e: Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }



}