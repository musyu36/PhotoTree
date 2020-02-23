package com.example.photopractice

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lo_folders.view.*

class FolderAdapter(mCtx: Context, val folders: ArrayList<Folder>): RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

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


    }

}