package ru.netology.nerecipe.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast

fun sendMyNotification(context: Context,message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .apply {
            this.setGravity(Gravity.BOTTOM, 0, 16)
            this.show()
        }
}