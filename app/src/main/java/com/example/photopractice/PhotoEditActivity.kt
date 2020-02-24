package com.example.photopractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_photo_edit.*

class PhotoEditActivity : AppCompatActivity() {

    companion object{
        lateinit var dbHandlerPhoto: DBHandler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_edit)

        dbHandlerPhoto = DBHandler(this, null, null, 1)

        val folderId = intent.getIntExtra("folderId", -1)
        //写真表示
        viewPhotos(folderId)
        //フォルダ名表示
        if(folderId != -1){
            if(folderId != null){
                val folder = dbHandlerPhoto.getFolder(this, folderId)
                textFolderName.setText(folder.folderName)
            }
        }

        //写真追加
        fab2.setOnClickListener{
            val i = Intent(this, AddPhotoActivity::class.java).putExtra("folderId", folderId)
            startActivity(i)
        }
    }


    private fun viewPhotos(folderId: Int){
        val photosList = dbHandlerPhoto.getPhotos(this, folderId)
        val adapter = PhotoAdapter(this, photosList)
//        adapter.setOnItemClickListener{id->
//            Log.v("###","folder tapped")
//            val intent = Intent(this, PhotoEditActivity::class.java).putExtra("photoId", id)
//            startActivity(intent)
//        }
        var rvPhotos: RecyclerView = findViewById(R.id.rvPhotos)
        rvPhotos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvPhotos.adapter = adapter

    }

    override fun onResume(){
        val folderId = intent.getIntExtra("folderId", -1)
        viewPhotos(folderId)
        super.onResume()
    }
}
