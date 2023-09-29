package com.renan.qrcodebarreader.presenter.home.pages.docscanning

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import javax.inject.Inject


@HiltViewModel
class DocScanningViewModel @Inject constructor() : ViewModel() {
    private var _state = MutableStateFlow(DocScanningState())
    var state = _state.asStateFlow()


    fun detectDocument(uri: Uri, context: Context) {
        val image: Bitmap? = convertURIToBitmap(uri = uri, context = context)
        if (image == null) _state.update {
            it.copy(
                errorMessage = "Imagem Inválida", loading = false
            )
        }
        if (!OpenCVLoader.initDebug()) _state.update {
            it.copy(
                errorMessage = "Ocorreu um erro ao iniciar detecção de documentos", loading = false
            )
        }

        val originalImage = Mat()
        Utils.bitmapToMat(image, originalImage)
        val shrunkImageHeight = 500.0
        Imgproc.resize(
            originalImage,
            originalImage,
            Size(shrunkImageHeight * image!!.width / image.height, shrunkImageHeight)
        )

        Imgproc.cvtColor(originalImage, originalImage, Imgproc.COLOR_BGR2Luv)
        val imageSplitByColorChannel: List<Mat> = mutableListOf()
        Core.split(originalImage, imageSplitByColorChannel)
        val documentCorners: List<Point>? =
            imageSplitByColorChannel
                .mapNotNull { findCorners(it) }
                .maxByOrNull { Imgproc.contourArea(it) }
                ?.toList()
                ?.map {
                    Point(it.x * image.height / shrunkImageHeight, it.y * image.height / shrunkImageHeight)
                }
        documentCorners
            ?.sortedBy { it.y }
            ?.chunked(2)
            ?.map { it.sortedBy { point -> point.x } }
            ?.flatten()
        println(documentCorners)

    }
    private fun findCorners(image: Mat): MatOfPoint? {
        val outputImage = Mat()

        // blur image to help remove noise
        Imgproc.GaussianBlur(image, outputImage, Size(5.0, 5.0), 0.0)

        // convert all pixels to either black or white (document should be black after this), but
        // there might be other parts of the photo that turn black
        Imgproc.threshold(
            outputImage,
            outputImage,
            0.0,
            255.0,
            Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU
        )

        // detect the document's border using the Canny edge detection algorithm
        Imgproc.Canny(outputImage, outputImage, 50.0, 200.0)

        // the detect edges might have gaps, so try to close those
        Imgproc.morphologyEx(
            outputImage,
            outputImage,
            Imgproc.MORPH_CLOSE,
            Mat.ones(Size(5.0, 5.0), CvType.CV_8U)
        )

        // get outline of document edges, and outlines of other shapes in photo
        val contours: MutableList<MatOfPoint> = mutableListOf()
        Imgproc.findContours(
            outputImage,
            contours,
            Mat(),
            Imgproc.RETR_LIST,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        // approximate outlines using polygons
        var approxContours =
            contours.map {
                val approxContour = MatOfPoint2f()
                val contour2f = MatOfPoint2f(*it.toArray())
                Imgproc.approxPolyDP(
                    contour2f,
                    approxContour,
                    0.02 * Imgproc.arcLength(contour2f, true),
                    true
                )
                MatOfPoint(*approxContour.toArray())
            }

        // We now have many polygons, so remove polygons that don't have 4 sides since we
        // know the document has 4 sides. Calculate areas for all remaining polygons, and
        // remove polygons with small areas. We assume that the document takes up a large portion
        // of the photo. Remove polygons that aren't convex since a document can't be convex.
        approxContours =
            approxContours.filter {
                it.height() == 4 && Imgproc.contourArea(it) > 1000 && Imgproc.isContourConvex(it)
            }

        // Once we have all large, convex, 4-sided polygons find and return the 1 with the
        // largest area
        return approxContours.maxByOrNull { Imgproc.contourArea(it) }
    }




//    fun detectDocument(uri: Uri, context: Context) {
//        _state.update { it.copy(loading = true) }
//        val image: Bitmap? = convertURIToBitmap(uri = uri, context = context)
//        if (image == null) _state.update {
//            it.copy(
//                errorMessage = "Imagem Inválida", loading = false
//            )
//        }
//        if (!OpenCVLoader.initDebug()) _state.update {
//            it.copy(
//                errorMessage = "Ocorreu um erro ao iniciar detecção de documentos", loading = false
//            )
//        }
//
//        val originalImage = Mat()
//        Utils.bitmapToMat(image, originalImage)
//        val grayImage = Mat()
//        Imgproc.cvtColor(originalImage, grayImage, Imgproc.COLOR_BGR2GRAY)
//        val gaussianImage = Mat()
//        Imgproc.GaussianBlur(grayImage, gaussianImage, Size(5.0, 5.0), 0.0)
//        val binaryImage = Mat()
//        Imgproc.threshold(gaussianImage, binaryImage, 127.0, 255.0, Imgproc.THRESH_BINARY)
//        val edges = Mat()
//        Imgproc.Canny(binaryImage, edges, 50.0, 150.0)
//        val contours = mutableListOf<MatOfPoint>()
//        val hierarchy = Mat()
//        Imgproc.findContours(
//            edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE
//        )
//        val resultImage = originalImage.clone()
//        for (contour in contours) {
//            val epsilon = 0.04 * Imgproc.arcLength(MatOfPoint2f(*contour.toArray()), true)
//            val approx = MatOfPoint2f()
//            Imgproc.approxPolyDP(MatOfPoint2f(*contour.toArray()), approx, epsilon, true)
//            if (approx.total() == 4L) {
//                Imgproc.drawContours(resultImage, listOf(contour), -1, Scalar(0.0, 255.0, 0.0), 3)
//                println("Possível folha de papel encontrada.")
//            }
//
//        }
//
//        _state.update {
//            it.copy(
//                image = matToBitmap(resultImage)
//            )
//        }
//    }


    private fun convertURIToBitmap(uri: Uri, context: Context): Bitmap? {
        var bitmap: Bitmap? = null
        viewModelScope.launch {
            bitmap = when {
                Build.VERSION.SDK_INT < 28 -> {
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver, uri
                    )
                }

                else -> {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(
                        source
                    ) { decoder, _, _ -> decoder.isMutableRequired = true }
                }
            }
        }
        return bitmap
    }

    private fun matToBitmap(mat: Mat): Bitmap {
        val bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, bitmap)
        return bitmap
    }

}