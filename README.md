# DocScannerAndroidKotlin
This example is about detecting the borders of any document and then capturing the image of the same.

<b>Features : </b>

- Scanning of documents.
- Editing scaned image with crop and filter option.
- Save scanned image to your album.
- Lightweight dependency.

<b>Requirements :</b>
- Android Studio

<b>Usage :</b>
1. Clone or download <b>scanlibrary</b> and import it to your project

2. Import below code to your build.gradle(Module: app) file

    ```implementation project(':scanlibrary')```

3. Change below code to your settings.gradle file

    ```include ':app',':scanlibrary'```

4. Add mehod in button click to start scanning any document

    ```kotlin
    protected fun startScan(preference: Int) {
        val intent = Intent(this@MainActivity, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        startActivityForResult(intent, REQUEST_CODE)
    }
    
5. Add below code in onActivityResult method to get generated scanned file.

    ```java
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
              
              } catch (e: IOException) {
                e.printStackTrace()
              }

                }
            }
