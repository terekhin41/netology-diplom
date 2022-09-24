package ru.netology.nerecipe.adapter.dragAndDrop

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemDrop()
}