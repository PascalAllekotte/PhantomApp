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
    private var bitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private lateinit var canvasBitmap: Canvas
    private val history = mutableListOf<Bitmap>()

    private val paintClear = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        strokeWidth = 20f
        color = Color.TRANSPARENT
        style = Paint.Style.FILL_AND_STROKE
    }

    var myWidth = 0
        private set
    var myHeight = 0
        private set

    private lateinit var initialBitmap: Bitmap

    init {
        canvasBitmap = Canvas(bitmap)
        initialBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true) // Kopie des initialen Zustands speichern
        history.add(initialBitmap)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        myWidth = MeasureSpec.getSize(widthMeasureSpec)
        myHeight = MeasureSpec.getSize(heightMeasureSpec)

        bitmap = Bitmap.createScaledBitmap(bitmap, myWidth, myHeight, false)
        canvasBitmap = Canvas(bitmap)
        setMeasuredDimension(myWidth, myHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                history.add(bitmap.copy(Bitmap.Config.ARGB_8888, true))
                if (history.size > 10) {
                    history.removeAt(0)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                val radius = 30f
                canvasBitmap.drawCircle(x, y, radius, paintClear)
                invalidate()
            }
        }
        return true
    }

    fun undo() {

        if (history.size > 1) {
            history.removeAt(history.size - 1) // Letzten Eintrag entfernen
            bitmap = history.last().copy(Bitmap.Config.ARGB_8888, true)
            canvasBitmap = Canvas(bitmap)
            invalidate() // Neu zeichnen
        }
    }

    fun setInitialBitmap(newBitmap: Bitmap) {
        initialBitmap = Bitmap.createScaledBitmap(newBitmap, width, height, true)
        bitmap = initialBitmap.copy(Bitmap.Config.ARGB_8888, true)
        canvasBitmap.setBitmap(bitmap)
        history.clear()
        history.add(bitmap.copy(Bitmap.Config.ARGB_8888, true))
        invalidate()
    }


    fun updateStrokeWidth(strokeWidth: Float) {
        paintClear.strokeWidth = strokeWidth
        invalidate()
    }
}

