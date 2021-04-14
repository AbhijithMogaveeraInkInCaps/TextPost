package com.inkincaps.textpost

import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes

fun View.beVisible(){
    beVisibleIf(true)
}

fun View.beInVisible(){
    beVisibleIf(false)
}

fun View.beVisibleIf(predicate: Boolean){
    visibility = if(predicate) View.VISIBLE else View.INVISIBLE
}


fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.beGone() {
    this.visibility = View.GONE
}

fun View.getVisibleAreaOffset():Float{
    val drawRect = Rect()
    val playerRect = Rect()
    getDrawingRect(drawRect)

    val drawArea = drawRect.width() * drawRect.height()
    val isVisible = getGlobalVisibleRect(playerRect, Point())
    if(isVisible && drawArea>0){
        val visibleArea = playerRect.height() * playerRect.width()
        return visibleArea / drawArea.toFloat()
    }
    return 0f
}