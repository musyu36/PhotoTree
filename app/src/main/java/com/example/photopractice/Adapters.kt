package com.example.photopractice

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lo_folders.view.*

class FolderAdapter(mCtx: Context, val folders: List<Folder>): RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    companion object{
        private val DATABASE_NAME = "MyData.db"
        private val DATABASE_VERSION = 1

        val FOLDERS_TABLE_NAME = "Folders"
        val COLUMN_FOLDERID = "folderid"
        val COLUMN_FOLDERNAME = "foldername"
    }



    val mCtx = mCtx

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtFolderName = itemView.txtFolderName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lo_folders,parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return folders.size
    }


    override fun onBindViewHolder(holder: FolderAdapter.ViewHolder, position: Int) {
        val folder: Folder = folders[position]
        holder.txtFolderName.text = folder.folderName

        holder.itemView.setOnClickListener{
            listener?.invoke(folder._folderID)
        }
    }



    private var listener: ((Int?)-> Unit)? = null

    fun setOnItemClickListener(listener:(Int?)-> Unit){
        this.listener = listener
    }

}