package com.example.photopractice

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_folder.*
import kotlinx.android.synthetic.main.activity_photo_edit.*


class AddFolderActivity : AppCompatActivity() {

    companion object{
        val MAX_FOLDER_LENGTH = 15
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_folder)

        editFolderName
        val lengthFilter = LengthFilter(MAX_FOLDER_LENGTH)
        editFolderName.setFilters(arrayOf<InputFilter>(lengthFilter))

        val folderId = intent.getIntExtra("folderId" , -1)

        //フォルダ名表示
        if(folderId != -1){
            Log.v("###" , "in folderId != -1: " + folderId)
            val folder = MainActivity.dbHandler.getFolder(this, folderId)
            editFolderName.setText(folder.folderName)

            //保存
            btnSaveFolder.setOnClickListener{
                //未入力時
                if(editFolderName.text.isEmpty()){
                    Toast.makeText(this, "Enter Folder Name", Toast.LENGTH_SHORT).show()
                    editFolderName.requestFocus()
                }else{
                    //保存処理
                    val folder = Folder()
                    folder._folderID = folderId
                    folder.folderName = editFolderName.text.toString()
                    MainActivity.dbHandler.updateFolder(this, folder)
                    clearEdits()
                    finish()
                }
            }
        }else{
            //保存
            btnSaveFolder.setOnClickListener{
                //未入力時
                if(editFolderName.text.isEmpty()){
                    Toast.makeText(this, "Enter Folder Name", Toast.LENGTH_SHORT).show()
                    editFolderName.requestFocus()
                }else{
                    //保存処理
                    val folder = Folder()
                    folder.folderName = editFolderName.text.toString()
                    MainActivity.dbHandler.addFolder(this, folder)
                    clearEdits()
                    finish()
                }
            }
        }

        //キャンセル
        btnCancelFolder.setOnClickListener{
            clearEdits()
            finish()
        }
    }
    
    private fun clearEdits(){
        editFolderName.text.clear()
    }
}
