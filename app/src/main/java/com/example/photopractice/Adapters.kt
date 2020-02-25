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
import kotlinx.android.synthetic.main.lo_photos.view.*

class FolderAdapter(mCtx: Context, val folders: List<Folder>): RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //lo_folders
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


class PhotoAdapter(mCtx: Context, val photos: List<Photo>): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //lo_photos
        val txtPhotoMemo = itemView.txtPhotoMemo
        val txtPhotoDate = itemView.txtPhotoDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lo_photos,parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: PhotoAdapter.ViewHolder, position: Int) {
        val photo: Photo = photos[position]
        holder.txtPhotoMemo.text = photo.memo
        holder.txtPhotoDate.text = photo.date

        holder.itemView.setOnClickListener{
            listener?.invoke(photo._photoID)
        }
    }

    private var listener: ((Int?)-> Unit)? = null

    fun setOnItemClickListener(listener:(Int?)-> Unit){
        this.listener = listener
    }
}