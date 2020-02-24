package com.example.photopractice

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_folder.*
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.util.*

class AddPhotoActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val str = String.format(Locale.US, "%d/%d/%d", year, monthOfYear+1, dayOfMonth)
        editDate.text = str
    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePick()
        newFragment.show(supportFragmentManager, "datePicker")

    }

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
                MainActivity.dbHandler.addPhoto(this, photo)
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
