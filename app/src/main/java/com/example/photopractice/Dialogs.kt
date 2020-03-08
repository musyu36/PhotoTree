package com.example.photopractice

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class FolderEditDialog : DialogFragment(){
    interface Listener{
        fun edit()
        fun close()
    }

    private var listener: Listener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when(context){
            //受け取ったcontextがListenerインターフェイスを持っているアクティビティなら格納
            is Listener -> listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //requireActivity()でフラグメントを呼び出したアクティビティを取得
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("編集")
        builder.setPositiveButton("編集"){dialog, which ->
            listener?.edit()
        }
        builder.setNegativeButton("閉じる"){dialog, which ->
            listener?.close()
        }

        return builder.create()
    }
}