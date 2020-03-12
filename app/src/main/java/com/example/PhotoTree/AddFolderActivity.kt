package com.example.PhotoTree

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_folder.*


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

        //フォルダ編集
        if(folderId != -1){
            val folder = MainActivity.dbHandler.getFolder(this, folderId)
            editFolderName.setText(folder.folderName)

            //削除
            btnDeleteFolder.setOnClickListener{
                var alertDialog = AlertDialog.Builder(this)
                    .setTitle(folder.folderName)
                    .setMessage("フォルダを消しますか?")
                    .setPositiveButton("削除"){dialog, which ->
                        MainActivity.dbHandler.deleteFolder(this, folder)
                        finish()
                    }
                    .setNegativeButton("キャンセル"){dialog, which ->
                    }
                alertDialog.show()
            }

            //保存
            btnSaveFolder.setOnClickListener{
                //未入力時
                if(editFolderName.text.isEmpty()){
                    Toast.makeText(this, "フォルダ名を入力して下さい", Toast.LENGTH_SHORT).show()
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
            btnDeleteFolder.setVisibility(View.GONE)
            //フォルダ追加
            //保存
            btnSaveFolder.setOnClickListener{
                //未入力時
                if(editFolderName.text.isEmpty()){
                    Toast.makeText(this, "フォルダ名を入力して下さい", Toast.LENGTH_SHORT).show()
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
