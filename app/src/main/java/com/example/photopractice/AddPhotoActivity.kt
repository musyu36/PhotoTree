package com.example.photopractice

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.io.ByteArrayOutputStream
import java.util.*


class AddPhotoActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    companion object {
        private const val READ_REQUEST_CODE: Int = 42
    }

    //選択した日付を表示
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val str = String.format(Locale.US, "%d/%d/%d", year, monthOfYear+1, dayOfMonth)
        editDate.text = str
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {
                try {
                    resultData?.data?.also { uri ->
                        val inputStream = contentResolver?.openInputStream(uri)
                        val image = BitmapFactory.decodeStream(inputStream)
                        val imageView = findViewById<ImageView>(R.id.editImage)
                        Log.v("###" , "image.hieght" + image.height)
                        Log.v("###" , "image.width" + image.width)
                        imageView.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
                }
            }
        }
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
            if (editImage.height == 0 || editImage.width == 0){
                Toast.makeText(this, "画像を選択してください", Toast.LENGTH_SHORT).show()
            }else{
                //保存ボタンを無効化
                btnSavePhoto.isEnabled = false

                //保存処理
                val photo = Photo()
                Log.v("###", "in btnSavePhoto 1 ")
                if(editDate.text == "選択してください"){
                    photo.date = ""
                }else{
                    photo.date = editDate.text.toString()
                }
                photo.memo = editMemo.text.toString()
//                photo.image = getBytes(image)
                photo.image = getBytes(editImage.drawToBitmap())
                Log.v("###", "editImage.drawToBitmap(): " + editImage)

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


        //画像取得
        btnSelectImage.setOnClickListener{
            selectPhoto()
        }

        //キャンセル
        btnCancelPhoto.setOnClickListener{
            clearEdits()
            finish()
        }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    private fun clearEdits(){
        editMemo.text.clear()
    }


    // convert from bitmap to byte array
    fun getBytes(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }


}
