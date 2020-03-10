package com.example.photopractice

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.io.ByteArrayOutputStream
import java.util.*


class AddPhotoActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    companion object {
        private const val READ_REQUEST_CODE: Int = 42
    }

    //選択した日付を表示
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val str = String.format(Locale.US, "%d/%d/%d", year, monthOfYear+1, dayOfMonth)
        editDate.text = str
    }

    //選択した画像を表示
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
                        imageView.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //日付選択ダイアログを表示
    fun showDatePickerDialog(v: View) {
        val newFragment = DatePick()
        newFragment.show(supportFragmentManager, "datePicker")

    }

    //選択した時間を表示
    override fun onTimeSet(view: android.widget.TimePicker, hourOfDay: Int, minute: Int) {
        val str = String.format(Locale.US, "%d:%d", hourOfDay, minute)
        editTime.text = str
    }


    //時間選択ダイアログを表示
    fun showTimePickerDialog(v: View) {
        val newFragment = TimePick()
        newFragment.show(supportFragmentManager, "timePicker")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //folderIdの取り出し
        val folderId = intent.getIntExtra("folderId", -1)

        //photoIdの取り出し
        val photoId = intent.getIntExtra("photoId", -1)

        Log.v("### " , "photoId : " + photoId)

        //編集
        if(photoId != -1){
            //既存のデータをセット
            val photo = MainActivity.dbHandler.getPhoto(this, photoId)
            editImage.setImageBitmap(getImage(photo.image))
            editDate.setText(photo.date)
            editTime.setText(photo.time)
            editMemo.setText(photo.memo)

            //保存
            btnSavePhoto.setOnClickListener{
                val bitmap = editImage.getDrawable()

                //未入力時
                if (editImage.height == 0 || editImage.width == 0 || bitmap == null){
                    Toast.makeText(this, "画像を選択してください", Toast.LENGTH_SHORT).show()
                }else{
                    //保存ボタンを無効化
                    btnSavePhoto.isEnabled = false

                    //保存処理
                    val photo = Photo()
                    if(editDate.text == "選択してください"){
                        photo.date = ""
                    }else{
                        photo.date = editDate.text.toString()
                    }
                    if(editTime.text == "選択してください"){
                        photo.time = ""
                    }else{
                        photo.time = editTime.text.toString()
                    }
                    photo._photoID = photoId
                    photo.memo = editMemo.text.toString()
                    photo.image = getBytes(editImage.drawToBitmap())
                    photo.affiliationID = folderId

                    MainActivity.dbHandler.updatePhoto(this, photo)
                    clearEdits()
                    finish()
                }
            }
        }else{
            //新規
            //保存
            btnSavePhoto.setOnClickListener{
                val bitmap = editImage.getDrawable()

                //未入力時
                if (editImage.height == 0 || editImage.width == 0 || bitmap == null){
                    Toast.makeText(this, "画像を選択してください", Toast.LENGTH_SHORT).show()
                }else{
                    //保存ボタンを無効化
                    btnSavePhoto.isEnabled = false

                    //保存処理
                    val photo = Photo()
                    if(editDate.text == "選択してください"){
                        photo.date = ""
                    }else{
                        photo.date = editDate.text.toString()
                    }
                    if(editTime.text == "選択してください"){
                        photo.time = ""
                    }else{
                        photo.time = editTime.text.toString()
                    }
                    photo.memo = editMemo.text.toString()
                    photo.image = getBytes(editImage.drawToBitmap())
                    photo.affiliationID = folderId
                    MainActivity.dbHandler.addPhoto(this, photo)
                    clearEdits()
                    finish()
                }
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

    fun getImage(image: ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image!!.size)
    }

}
