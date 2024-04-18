package de.syntax.androidabschluss.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CustomDrawView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var bitmap: Bitmap
    private lateinit var canvasBitmap: Canvas
    private val paintClear = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        strokeWidth = 45f // Set the line width
        color = Color.WHITE // Set the color to transparent
        style = Paint.Style.STROKE // Set the paint style to stroke
    }

    private var lastX: Float = 0f
    private var lastY: Float = 0f
    var myWidth = 0
    var myHeight = 0

    init {
        // Initialize with a default bitmap (can be empty or a placeholder)
        bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        canvasBitmap = Canvas(bitmap)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        myWidth = MeasureSpec.getSize(widthMeasureSpec)
        myHeight = MeasureSpec.getSize(heightMeasureSpec)

        // Set the width and height of the bitmap to match the view's dimensions
        bitmap = Bitmap.createScaledBitmap(bitmap, myWidth, myHeight, false)
        canvasBitmap = Canvas(bitmap)
        setMeasuredDimension(myWidth, myHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw the bitmap onto the canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Store the initial touch coordinates
                lastX = event.x
                lastY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                // Draw a line from the previous touch point to the current touch point
                val x = event.x
                val y = event.y
                canvasBitmap.drawLine(lastX, lastY, x, y, paintClear)
                lastX = x
                lastY = y
                invalidate() // Request a redraw of the view
            }
        }
        return true
    }

    // Method to set the bitmap dynamically
    fun setBitmap(newBitmap: Bitmap) {
        bitmap = Bitmap.createScaledBitmap(newBitmap, myWidth, myHeight, false)
        canvasBitmap = Canvas(bitmap)
        invalidate() // Redraw the view with the new bitmap
    }

}