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


// Zeichenbereich für benutzerdefinierte Grafikoperationen
class CustomDrawView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    // Bitmap für Zeichenoperationen
    private var bitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    // Canvas zum Zeichnen auf Bitmap
    private lateinit var canvasBitmap: Canvas
    // Historie der Bitmaps für Undo-Funktion
    private val history = mutableListOf<Bitmap>()

    // Paint für Löschoperationen
    private val paintClear = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        strokeWidth = 20f
        color = Color.TRANSPARENT
        style = Paint.Style.FILL_AND_STROKE
    }

    // Größe des Views
    var myWidth = 0
        private set
    var myHeight = 0
        private set

    // Erstes Bitmap für Rücksetzungen
    private lateinit var initialBitmap: Bitmap

    init {
        // Initialisiert Canvas und speichert Startzustand
        canvasBitmap = Canvas(bitmap)
        initialBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        history.add(initialBitmap)
    }

    // Passt Größe des Bitmap an View-Größe an
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        myWidth = MeasureSpec.getSize(widthMeasureSpec)
        myHeight = MeasureSpec.getSize(heightMeasureSpec)

        bitmap = Bitmap.createScaledBitmap(bitmap, myWidth, myHeight, false)
        canvasBitmap = Canvas(bitmap)
        setMeasuredDimension(myWidth, myHeight)
    }

    // Zeichnet Bitmap auf Canvas
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    // Behandelt Touch-Events für Zeichnen und Löschen
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Fügt aktuellen Zustand zur Historie hinzu
                history.add(bitmap.copy(Bitmap.Config.ARGB_8888, true))
                if (history.size > 10) {
                    history.removeAt(0)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // Zeichnet Kreis an berührter Position
                val x = event.x
                val y = event.y
                val radius = 30f
                canvasBitmap.drawCircle(x, y, radius, paintClear)
                invalidate()
            }
        }
        return true
    }

    // Stellt letzten gültigen Zustand wieder her
    fun undo() {
        if (history.size > 1) {
            history.removeAt(history.size - 1)
            bitmap = history.last().copy(Bitmap.Config.ARGB_8888, true)
            canvasBitmap = Canvas(bitmap)
            invalidate()
        }
    }

    // Setzt Bitmap zurück und aktualisiert Anzeige
    fun setInitialBitmap(newBitmap: Bitmap) {
        initialBitmap = Bitmap.createScaledBitmap(newBitmap, width, height, true)
        bitmap = initialBitmap.copy(Bitmap.Config.ARGB_8888, true)
        canvasBitmap.setBitmap(bitmap)
        history.clear()
        history.add(bitmap.copy(Bitmap.Config.ARGB_8888, true))
        invalidate()
    }

    // Aktualisiert die Strichbreite des Löschwerkzeugs
    fun updateStrokeWidth(strokeWidth: Float) {
        paintClear.strokeWidth = strokeWidth
        invalidate()
    }
}
