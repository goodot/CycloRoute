package me.tatocaster.stravagraph.features.create.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import com.fxn.pix.Pix
import com.xiaopo.flying.sticker.BitmapStickerIcon
import kotlinx.android.synthetic.main.activity_create.*
import me.tatocaster.stravagraph.CHOOSE_IMAGE_REQUEST_CODE
import me.tatocaster.stravagraph.R
import me.tatocaster.stravagraph.common.utils.*
import me.tatocaster.stravagraph.entity.LatLng
import me.tatocaster.stravagraph.entity.StravaRecordedActivity
import me.tatocaster.stravagraph.features.base.BaseActivity
import java.io.File
import javax.inject.Inject


class CreateActivity : BaseActivity(), CreateActivityContract.View {
    private lateinit var canvas: CanvasPolyLineView

    @Inject
    lateinit var presenter: CreateActivityContract.Presenter

    private var stravaRecordedActivity: StravaRecordedActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        stravaRecordedActivity = intent.extras?.getParcelable("strava_activity") as StravaRecordedActivity

        bitmapImage.setOnClickListener {
            //            val demoImageBitmap = BitmapFactory.decodeResource(resources, R.drawable.login_background)
            Pix.start(this, CHOOSE_IMAGE_REQUEST_CODE)
        }

        canvas = canvasPolyLine as CanvasPolyLineView
        canvas.finishDrawListener = object : CanvasPolyLineView.CanvasDrawEvent {
            override fun drawFinished() {
                canvas.visibility = View.GONE

                stickerView.addSticker(BitmapStickerIcon(BitmapDrawable(resources, canvas.myCanvasBitmap), BitmapStickerIcon.RIGHT_BOTOM))
                stickerView.addSticker(BitmapStickerIcon(BitmapDrawable(resources, canvas.activityStatsBitmap), BitmapStickerIcon.LEFT_BOTTOM))
            }
        }

        stickerView.isLocked = false
        stickerView.isConstrained = true

        finishEditing.setOnClickListener {
            val file = FileUtil.getNewFile(this, "Sticker")
            if (file != null) {
                stickerView.save(file)
                showSuccessAlert(this, "saved in " + file.absolutePath)
//                finish()
            } else {
                showErrorAlert(this, "the file is null", "")
            }
        }

        stravaRecordedActivity?.let {
            val paths = ArrayList<LatLng>()
            val polyList = decodePoly(it.polyLine)
            paths.addAll(polyList)
            canvas.latLngs = paths
            canvas.stravaActivity = it
            canvas.invalidate()
        }
    }

    override fun onGetImageFile(file: File) {
        val d = BitmapDrawable(resources, file.absolutePath).bitmap
//                val scaled = com.fxn.utility.Utility.getScaledBitmap(512, com.fxn.utility.Utility.getExifCorrectedBitmap(f))
//                val scaled = com.fxn.utility.Utility.getScaledBitmap(512, d)
        bitmapImage.setImageBitmap(d)
    }

    override fun showError(message: String) {
        showErrorAlert(this, "", message)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CHOOSE_IMAGE_REQUEST_CODE) {
            data?.let {
                val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                if (returnValue.isNotEmpty()) {
                    presenter.onChooseImageResult(returnValue[0])
                }
            }
        }
    }
}