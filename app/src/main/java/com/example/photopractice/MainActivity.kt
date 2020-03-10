package com.example.photopractice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    companion object{
        lateinit var dbHandler: DBHandler

        lateinit var folderslist:List<Folder>
        lateinit var adapter:FolderAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DBHandler(this, null, null, 1)

        viewFolders()

        //フォルダ追加
        fab1.setOnClickListener{
            val i = Intent(this, AddFolderActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun viewFolders(){
        val foldersList = dbHandler.getFolders(this)
        val adapter = FolderAdapter(this, foldersList)
        adapter.setOnItemClickListener{id->
            val intent = Intent(this, PhotoEditActivity::class.java).putExtra("folderId", id)
            startActivity(intent)
        }
        var rvFolders: RecyclerView = findViewById(R.id.rvFolders)
        rvFolders.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvFolders.adapter = adapter

    }

    override fun onResume(){
        viewFolders()
        super.onResume()
    }
}
