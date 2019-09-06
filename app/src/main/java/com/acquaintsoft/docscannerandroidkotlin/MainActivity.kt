package com.acquaintsoft.docscannerandroidkotlin

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var btnScanYourDoc: Button? = null
    val REQUEST_CODE = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById()


        btnScanYourDoc?.setOnClickListener(View.OnClickListener {
            Dexter.withActivity(this@MainActivity)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {

                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                        startScan(ScanConstants.OPEN_CAMERA)

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {/* ... */
                    }
                }).check()
        })
    }

    private fun findViewById() {

        btnScanYourDoc = findViewById(R.id.btnScanYourDoc)

    }

    protected fun startScan(preference: Int) {
        val intent = Intent(this@MainActivity, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        startActivityForResult(intent, REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {


            REQUEST_CODE -> {

                var uri: Uri? = null

                if (data != null) {

                    uri = data.extras!!.getParcelable(ScanConstants.SCANNED_RESULT)

                    var bitmap: Bitmap? = null
                    try {

                        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                        val imageFileName = "JPEG_" + timeStamp + "_"
                        val storageDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                        )
                        val image = File.createTempFile(
                            imageFileName, /* prefix */
                            ".jpg", /* suffix */
                            storageDir      /* directory */
                        )


                        val bos = ByteArrayOutputStream()
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        contentResolver.delete(uri!!, null, null)
                        bitmap!!.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
                        val bitmapdata = bos.toByteArray()


                        val fos = FileOutputStream(image)
                        fos.write(bitmapdata)
                        fos.flush()
                        fos.close()

                        Log.e("file1", image.toString())

                        val intent = Intent(this@MainActivity, GeneratedDocActivity::class.java)
                        intent.putExtra("img", bitmapdata)
                        intent.putExtra("path", image.toString())
                        startActivity(intent)

                        Toast.makeText(applicationContext, image.toString(), Toast.LENGTH_SHORT).show()


                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }
}
