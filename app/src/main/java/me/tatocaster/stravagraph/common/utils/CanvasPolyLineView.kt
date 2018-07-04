package me.tatocaster.stravagraph.common.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import me.tatocaster.stravagraph.entity.LatLng
import me.tatocaster.stravagraph.entity.StravaRecordedActivity
import timber.log.Timber

class CanvasPolyLineView : View {
    private lateinit var paint: Paint
    private lateinit var shapePath: Path
    lateinit var finishDrawListener: CanvasDrawEvent

    var myCanvasBitmap: Bitmap? = null
    var activityStatsBitmap: Bitmap? = null

    private lateinit var myCanvas: Canvas

    private lateinit var activityStatsCanvas: Canvas

    private lateinit var identityMatrix: Matrix


    var latLngs: MutableList<LatLng> = mutableListOf()
    lateinit var stravaActivity: StravaRecordedActivity

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.strokeWidth = 8f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        shapePath = Path()

        myCanvasBitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888)
        myCanvas = Canvas()
        myCanvas.setBitmap(myCanvasBitmap)

        activityStatsBitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888)
        activityStatsCanvas = Canvas()
        activityStatsCanvas.setBitmap(activityStatsBitmap)

        identityMatrix = Matrix()
    }

    override fun onDraw(canvas: Canvas?) {
        if (latLngs.isEmpty() || canvas == null)
            return

        val canvasMidPointX = 300f
        val canvasMidPointY = 300f
        val factor = 5000f

        shapePath.moveTo(canvasMidPointX, canvasMidPointY)
        for (i in 1 until latLngs.size) {
            val coordX = (latLngs[i].longitude - latLngs[0].longitude)
            val coordY = (latLngs[i].latitude - latLngs[0].latitude)

            val finalX = (coordX * factor + canvasMidPointX).toFloat()
            val finalY = (coordY * factor + canvasMidPointY).toFloat()

            if (i == 1) {
//                shapePath.addCircle(finalX, finalY, 13f, Path.Direction.CW)
            }

            shapePath.lineTo(finalX, finalY)

//            if (i == latLngs.size - 1)
//                shapePath.addCircle(finalX, finalY, 15f, Path.Direction.CW)

            Timber.d("coordinates: %s, %s", coordX * factor + canvasMidPointX,
                    coordY * factor + canvasMidPointY)
        }

//        canvas.save()
        myCanvas.scale(1f, -1f, canvasMidPointX, canvasMidPointY)
        myCanvas.drawPath(shapePath, paint)
//        identityMatrix.postScale(1f, -1f, canvasMidPointX, canvasMidPointY)
        canvas.drawBitmap(myCanvasBitmap, identityMatrix, null)
        //restore canvas
//        canvas.restore()

        drawActivitySummary()
        finishDrawListener.drawFinished()
    }

    private fun drawActivitySummary() {
        //Measure the view at the exact dimensions (otherwise the text won't center correctly)
        val view = ActivitySummaryView(context)
        view.setSummary(stravaActivity)

        val widthSpec = View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.EXACTLY)
        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)


        //Translate the Canvas into position and draw it
        activityStatsCanvas.save()
        view.draw(activityStatsCanvas)
        activityStatsCanvas.drawBitmap(activityStatsBitmap, identityMatrix, null)
        activityStatsCanvas.restore()
    }

    interface CanvasDrawEvent {
        fun drawFinished()
    }
}
