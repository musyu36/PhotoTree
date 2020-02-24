package com.example.photopractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_folder.*
import kotlinx.android.synthetic.main.activity_add_photo.*

class AddPhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //folderIdの取り出し
        val folderId = intent.getIntExtra("folderId", -1)

        //保存
        btnSavePhoto.setOnClickListener{
            //未入力時
            if(editMemo.text.isEmpty()){
                Toast.makeText(this, "Enter Photo Name", Toast.LENGTH_SHORT).show()
                editMemo.requestFocus()
            }else{
                //保存処理
                val photo = Photo()
                Log.v("###", "in btnSavePhoto 1 ")
                photo.memo = editMemo.text.toString()
                Log.v("###", "in btnSavePhoto 2 ")
                photo.affiliationID = folderId
                Log.v("###", "memo: " + photo.memo + "affiliationID: " + photo.affiliationID)
                Log.v("###", "in btnSavePhoto 3 before addPhoto")
                PhotoEditActivity.dbHandlerPhoto.addPhoto(this, photo)
                Log.v("###", "in btnSavePhoto 4 after addPhoto")
                clearEdits()
                Log.v("###", "in btnSavePhoto 5 ")
                finish()
            }
        }

        //キャンセル
        btnCancelPhoto.setOnClickListener{
            clearEdits()
            finish()
        }
    }

    private fun clearEdits(){
        editMemo.text.clear()
    }
}
