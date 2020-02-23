package com.example.photopractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_photo_edit.*

class PhotoEditActivity : AppCompatActivity() {

    private val dbHandler = DBHandler(this, null, null, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_edit)

        Log.v("###" , "before folderId")
        val folderId = intent?.getIntExtra("folderId", -1)
        Log.v("###" , "after folderId")
        if(folderId != -1){
            if(folderId != null){
                Log.v("###" , "before getFolder")
                val folder = dbHandler.getFolder(this, folderId)
                Log.v("###" , "after getFolder")
                textFolderName.setText(folder.folderName)
                Log.v("###" , "after setText to textFolderName")
            }
        }
    }
}
