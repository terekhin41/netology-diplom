package ru.netology.nerecipe.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class ExitFromCreateRecipeDialog : DialogFragment() {

    private lateinit var listener: NoticeDialogListener

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        return builder
            .setTitle("Предупреждение")
            .setMessage("Все изменения будут удалены. Вы действительно хотите выйти?")
            .setPositiveButton("Отмена") { dialog, _ ->
                listener.onDialogPositiveClick(this)
                dialog.dismiss()
            }
            .setNegativeButton("Выйти") { dialog, _ ->
                listener.onDialogNegativeClick(this)
                dialog.dismiss()
            }
            .create()
    }
}