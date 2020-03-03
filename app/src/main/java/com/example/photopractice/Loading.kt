//package com.example.photopractice
//
//import android.R
//
//import android.app.ProgressDialog
//import android.content.Context
//
//
//class Loading {
//
//    var mContext: Context? = null
//    var mProgressDialog: ProgressDialog? = null
//
//    fun Loading(context: Context?) {
//        mContext = context
//        mProgressDialog = ProgressDialog(context)
//    }
//
//    fun show() {
//        mProgressDialog!!.show()
//        mProgressDialog.setContentView(R.layout.loading)
//        mProgressDialog!!.setCancelable(false)
//    }
//
//    fun close() {
//        mProgressDialog!!.dismiss()
//    }
//}