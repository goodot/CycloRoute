package me.tatocaster.stravagraph.common.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import me.tatocaster.stravagraph.entity.LatLng
import me.tatocaster.stravagraph.entity.StravaRecordedActivity
import timber.log.Timber


/*
class CanvasPolyLineView : SurfaceView, Runnable {
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var drawingLine: Paint
    private lateinit var path: Path
    private lateinit var drawingCanvas: Canvas
    var latLngs: ArrayList<HashMap<String, Float>> = arrayListOf()

    @Volatile
    private var isRunning = false

    private lateinit var thread: Thread

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        surfaceHolder = holder

        setPaint()

        drawingCanvas = Canvas()
    }

    fun pause() {
        isRunning = false
        while (true) {
            try {
                thread.join()
                return
            } catch (e: InterruptedException) {
                // try again
                Timber.d("destroy interrupted")
            }

        }
    }

    fun start() {
        if (!isRunning) {
            thread = Thread(this)
            thread.start()
            isRunning = true
            Timber.d("started")
        }
    }

    override fun run() {
        var canvas: Canvas
        while (isRunning) {
            if (surfaceHolder.surface.isValid) {
                try {
                    canvas = surfaceHolder.lockCanvas()
//                    drawCoordinates(canvas)
                    canvas.drawLine(0f, 0f, 20f, 20f, drawingLine)
                    canvas.drawLine(20f, 0f, 0f, 20f, drawingLine)
                    surfaceHolder.unlockCanvasAndPost(canvas)
                } catch (e: IllegalStateException) {
                    Timber.e(e)
                }
            }
        }
    }


    private fun setPaint() {
        drawingLine = Paint()
//        drawingLine.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        drawingLine.color = Color.BLUE
        drawingLine.strokeWidth = 20f
        drawingLine.style = Paint.Style.STROKE
        drawingLine.strokeCap = Paint.Cap.ROUND
        path = Path()
    }

    private fun drawCoordinates(canvas: Canvas) {
        for (i in 1 until latLngs.size) {
            canvas.drawLine(latLngs[i - 1]["lat"]!!, latLngs[i - 1]["lng"]!!, latLngs[i]["lat"]!!, latLngs[i]["lng"]!!, drawingLine)
            println(latLngs[i])
        }
        pause()
    }

}*/

class CanvasPolyLineView : View {
    private lateinit var paint: Paint
    private lateinit var activityStartPaint: Paint
    private lateinit var activityFinishPaint: Paint
    private lateinit var shapePath: Path


    var myCanvasBitmap: Bitmap? = null

    private lateinit var myCanvas: Canvas

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
        paint.color = Color.BLUE
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        shapePath = Path()

        activityStartPaint = Paint()
        paint.isAntiAlias = true
        activityStartPaint.color = Color.GREEN

        activityFinishPaint = Paint()
        paint.isAntiAlias = true
        activityFinishPaint.color = Color.RED


        myCanvasBitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888)
        myCanvas = Canvas()
        myCanvas.setBitmap(myCanvasBitmap)

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

            if (i == 1)
                canvas.drawCircle(finalX, finalY, 15f, activityStartPaint)

            shapePath.lineTo(finalX, finalY)

            /*if (i == latLngs.size - 1)
                canvas.drawCircle(finalX, finalY, 15f, activityFinishPaint)*/

            Timber.d("coordinates: %s, %s", coordX * factor + canvasMidPointX,
                    coordY * factor + canvasMidPointY)
        }

//        canvas.save()
//        canvas.scale(1f, -1f, canvasMidPointX, canvasMidPointY)

//        canvas.drawPath(shapePath, paint)
        myCanvas.scale(1f, -1f, canvasMidPointX, canvasMidPointY)
        myCanvas.drawPath(shapePath, paint)
//        identityMatrix.postScale(1f, -1f, canvasMidPointX, canvasMidPointY)
        canvas.drawBitmap(myCanvasBitmap, identityMatrix, null)

        //restore canvas
//        canvas.restore()

        drawActivitySummary(canvas)
    }

    private fun drawActivitySummary(canvas: Canvas) {
        //Measure the view at the exact dimensions (otherwise the text won't center correctly)
        val view = ActivitySummaryView(context)
        view.setSummary(stravaActivity)

        val widthSpec = View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.EXACTLY)
        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)


        //Translate the Canvas into position and draw it
        canvas.save()
        view.draw(canvas)
        canvas.restore()
    }
}
