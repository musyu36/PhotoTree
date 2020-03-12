package com.example.PhotoTree

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_photo_edit.*

class PhotoEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_edit)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val folderId = intent.getIntExtra("folderId", -1)
        //写真表示
        viewPhotos(folderId)
        //フォルダ名表示
        if(folderId != -1){
                val folder = MainActivity.dbHandler.getFolder(this, folderId)
                textFolderName.setText(folder.folderName)
        }

        //写真追加
        fab2.setOnClickListener{
            val i = Intent(this, AddPhotoActivity::class.java).putExtra("folderId", folderId)
            startActivity(i)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun viewPhotos(folderId: Int){
        val photosList = MainActivity.dbHandler.getPhotos(this, folderId)
        val adapter = PhotoAdapter(this, photosList)
        adapter.setOnItemClickListener{id->

        }
        var rvPhotos: RecyclerView = findViewById(R.id.rvPhotos)
        rvPhotos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        rvPhotos.adapter = adapter
    }

    override fun onResume(){
        val folderId = intent.getIntExtra("folderId", -1)
        viewPhotos(folderId)
        super.onResume()
    }
}
