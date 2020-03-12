package com.example.PhotoTree

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lo_folders.view.*
import kotlinx.android.synthetic.main.lo_photos.view.*


class FolderAdapter(val mCtx: Context, val folders: List<Folder>): RecyclerView.Adapter<FolderAdapter.ViewHolder>(){

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

        holder.itemView.setOnLongClickListener{
            var alertDialog = AlertDialog.Builder(mCtx)
                .setTitle(folder.folderName)
                .setPositiveButton("編集"){dialog, which ->
                    val intent = Intent(mCtx, AddFolderActivity::class.java).putExtra("folderId" , folder._folderID)
                    it.getContext().startActivity(intent)
                }
                .setNegativeButton("閉じる"){dialog, which ->
                }
            alertDialog.show()
            true
        }
    }

    private var listener: ((Int?)-> Unit)? = null
    fun setOnItemClickListener(listener:(Int?)-> Unit){
        this.listener = listener
    }
}


class PhotoAdapter(val mCtx: Context, val photos: List<Photo>): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //lo_photos
        val txtPhotoMemo = itemView.txtPhotoMemo
        val txtPhotoDate = itemView.txtPhotoDate
        val txtPhotoTime = itemView.txtPhotoTime
        val imgPhotoImage = itemView.imgPhotoImage
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
        holder.txtPhotoTime.text = photo.time
        holder.imgPhotoImage.setImageBitmap(getImage(photo.image))

        holder.itemView.setOnClickListener{
            listener?.invoke(photo._photoID)
            val params: ViewGroup.LayoutParams = it.clipBody.getLayoutParams()
            //メモ全文表示
            if(it.txtPhotoMemo.maxLines == 1){
                it.txtPhotoMemo.maxLines = Integer.MAX_VALUE
            }else{
                it.txtPhotoMemo.maxLines = 1
            }
        }

        holder.itemView.setOnLongClickListener{
            var alertDialog = AlertDialog.Builder(mCtx)
                .setTitle("編集しますか?")
                .setPositiveButton("編集"){dialog, which ->
                    val intent = Intent(mCtx, AddPhotoActivity::class.java).putExtra("photoId" , photo._photoID)
                    it.getContext().startActivity(intent)
                }
                .setNegativeButton("閉じる"){dialog, which ->
                }
            alertDialog.show()
            true
        }
    }

    private var listener: ((Int?)-> Unit)? = null

    fun setOnItemClickListener(listener:(Int?)-> Unit){
        this.listener = listener
    }

    // convert from byte array to bitmap
    fun getImage(image: ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image!!.size)
    }

}