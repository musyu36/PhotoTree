package com.example.photopractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_folder.*

class AddFolderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_folder)


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

        btnCancel.setOnClickListener{
            clearEdits()
            finish()
        }
    }
    
    private fun clearEdits(){
        editFolderName.text.clear()
    }
}
