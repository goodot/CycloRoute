package me.tatocaster.stravagraph.features.create.presentation

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import com.xiaopo.flying.sticker.DrawableSticker
import kotlinx.android.synthetic.main.activity_create.*
import me.tatocaster.stravagraph.R
import me.tatocaster.stravagraph.common.utils.*
import me.tatocaster.stravagraph.entity.LatLng
import me.tatocaster.stravagraph.entity.StravaRecordedActivity
import me.tatocaster.stravagraph.features.base.BaseActivity
import javax.inject.Inject


class CreateActivity : BaseActivity(), CreateActivityContract.View {
    private lateinit var canvas: CanvasPolyLineView

    @Inject
    lateinit var presenter: CreateActivityContract.Presenter

    private lateinit var stravaRecordedActivity: StravaRecordedActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        stravaRecordedActivity = intent.extras?.getParcelable("strava_activity") as StravaRecordedActivity

        chooseImage.setOnClickListener {
            val demoImageBitmap = BitmapFactory.decodeResource(resources, R.drawable.login_background)
            bitmapImage.setImageBitmap(demoImageBitmap)
        }

        canvas = canvasPolyLine as CanvasPolyLineView
        canvas.finishDrawListener = object : CanvasPolyLineView.CanvasDrawEvent {
            override fun drawFinished() {
                val canvasBitmap = canvas.myCanvasBitmap
                canvas.visibility = View.GONE

                val sticker = DrawableSticker(BitmapDrawable(resources, canvasBitmap))
                stickerView.addSticker(sticker)

                stickerView.addSticker(DrawableSticker(BitmapDrawable(resources, canvas.activityStatsBitmap)))
            }
        }

        stickerView.isLocked = false
        stickerView.isConstrained = true

        finishEditing.setOnClickListener {
            val file = FileUtil.getNewFile(this, "Sticker")
            if (file != null) {
                stickerView.save(file)
                showSuccessAlert(this, "saved in " + file.absolutePath)
            } else {
                showErrorAlert(this, "the file is null", "")
            }
        }

        val paths = ArrayList<LatLng>()
        val polyList = decodePoly(stravaRecordedActivity.polyLine)
        paths.addAll(polyList)
        canvas.latLngs = paths
        canvas.stravaActivity = stravaRecordedActivity
        canvas.invalidate()
    }

    override fun showError(message: String) {
        showErrorAlert(this, "", message)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}