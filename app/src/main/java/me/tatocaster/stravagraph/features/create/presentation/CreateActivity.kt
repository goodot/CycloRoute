package me.tatocaster.stravagraph.features.create.presentation

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.TextView
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
    private lateinit var activityTitleTextView: TextView

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

        canvas = canvasPolyLine as CanvasPolyLineView

        activityTitleTextView = titleTextView as TextView


        stickerView.setBackgroundColor(Color.WHITE)
        stickerView.isLocked = false
        stickerView.isConstrained = true

        getBitmap.setOnClickListener {
            val canvasBitmap = canvas.myCanvasBitmap

            val demoImageBitmap = BitmapFactory.decodeResource(resources, R.drawable.login_background)
            bitmapImage.setImageBitmap(demoImageBitmap)

            val sticker = DrawableSticker(BitmapDrawable(resources, canvasBitmap))

            stickerView.addSticker(sticker)
        }

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
        activityTitleTextView.text = stravaRecordedActivity.name
        val polyList = decodePoly(stravaRecordedActivity.polyLine)
        paths.addAll(polyList)
        canvas.latLngs = paths
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