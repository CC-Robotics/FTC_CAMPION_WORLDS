package org.firstinspires.ftc.teamcode.vision

import android.annotation.SuppressLint
import com.rowanmcalpin.nextftc.ftc.OpModeData
import org.opencv.calib3d.Calib3d
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.tan

class CrocSight : OpenCvPipeline() {

    // Fixed color threshold values based on tuning
    // Red thresholds (two ranges due to hue wrapping)
    private val lowerRed1 = Scalar(0.0, 100.0, 20.0)
    private val upperRed1 = Scalar(10.0, 255.0, 255.0)
    private val lowerRed2 = Scalar(160.0, 100.0, 20.0)
    private val upperRed2 = Scalar(179.0, 255.0, 255.0)

    // Blue thresholds
    private val lowerBlue = Scalar(105.0, 120.0, 17.0)
    private val upperBlue = Scalar(130.0, 255.0, 255.0)

    // Yellow thresholds
    private val lowerYellow = Scalar(18.0, 85.0, 87.0)
    private val upperYellow = Scalar(30.0, 255.0, 255.0)

    // Fixed morphology parameters
    private val erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(3.5, 3.5))
    private val dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(2.5, 2.5))
    private val closeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(5.0, 5.0))

    // 640x480 Camera Matrix
    private val cameraMatrix = Mat(3, 3, CvType.CV_64F).apply {
        put(0, 0, 821.993, 0.0, 330.489) // fx, 0, cx
        put(1, 0, 0.0, 821.993, 248.997) // 0, fy, cy
        put(2, 0, 0.0, 0.0, 1.0)         // 0, 0, 1
    }

    // 640x480 Distortion Coefficients
    private val distCoeffs = Mat(1, 5, CvType.CV_64F).apply {
        put(0, 0, -0.018522, 1.03979, 0.0, 0.0, -3.3171)
    }


    // Camera mounting configuration
    private val cameraMountingAngle = 0.0

    // Working matrices
    private val hsvMat = Mat()
    private val redMask1 = Mat()
    private val redMask2 = Mat()
    private val redMaskCombined = Mat()
    private val blueMask = Mat()
    private val yellowMask = Mat()
    private val morphedRedMask = Mat()
    private val morphedBlueMask = Mat()
    private val morphedYellowMask = Mat()
    private val edgesMat = Mat()
    private val dilatedEdges = Mat()
    private val outputMat = Mat()

    data class AnalyzedContour(
        val rect: RotatedRect,
        val angle: Double,
        val color: OpModeData.Alliance,
        val distance: Double,
        val normalizedCoords: Pair<Double, Double>,
        val sampleCoords: Pair<Double, Double>,
        val area: Double
    )

    private val analyzedContours = mutableListOf<AnalyzedContour>()

    override fun processFrame(input: Mat): Mat {
        // Clear previous contours
        synchronized(analyzedContours) {
            analyzedContours.clear()
        }

        // Create a copy for drawing
        input.copyTo(outputMat)

        // Convert to HSV
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV)

        // Create color masks
        Core.inRange(hsvMat, lowerRed1, upperRed1, redMask1)
        Core.inRange(hsvMat, lowerRed2, upperRed2, redMask2)
        Core.inRange(hsvMat, lowerBlue, upperBlue, blueMask)
        Core.inRange(hsvMat, lowerYellow, upperYellow, yellowMask)

        // Combine red masks
        Core.bitwise_or(redMask1, redMask2, redMaskCombined)

        // Apply morphology to clean up the masks
        morphMask(redMaskCombined, morphedRedMask)
        morphMask(blueMask, morphedBlueMask)
        morphMask(yellowMask, morphedYellowMask)

        // Process each color separately
        processColorContours(morphedRedMask, outputMat, "Red")
        processColorContours(morphedBlueMask, outputMat, "Blue")
        processColorContours(morphedYellowMask, outputMat, "Yellow")

        Imgproc.putText(
            outputMat,
            "Contours: ${analyzedContours.size}",
            Point(10.0, 50.0),
            Imgproc.FONT_HERSHEY_SIMPLEX,
            0.5,
            Scalar(255.0, 255.0,  255.0),
            1
        )

        //Release Mats to prevent memory leaks
        release()
        return outputMat
    }

    private fun morphMask(input: Mat, output: Mat) {
        // Apply morphological operations to clean up masks
        Imgproc.erode(input, output, erodeElement, Point(-1.0, -1.0), 2)
        Imgproc.dilate(output, output, dilateElement, Point(-1.0, -1.0), 1)
        Imgproc.morphologyEx(output, output, Imgproc.MORPH_CLOSE, closeElement)
    }

    @SuppressLint("DefaultLocale")
    private fun processColorContours(mask: Mat, drawingMat: Mat, color: String) {
        // Apply Canny edge detection
        Imgproc.Canny(mask, edgesMat, 50.0, 100.0)

        // Dilate edges to connect nearby edges
        val dilationElement = Imgproc.getStructuringElement(
            Imgproc.MORPH_RECT,
            Size(2.0, 2.0)
        )
        Imgproc.dilate(edgesMat, dilatedEdges, dilationElement)
        dilationElement.release()

        // Find contours on the dilated edges
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            dilatedEdges.clone(),
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        hierarchy.release()

        // Process each contour
        for (contour in contours) {
            val area = Imgproc.contourArea(contour)

            // Skip tiny contours to reduce noise
            if (area < 4500) {
                contour.release()
                continue
            }

            val contour2f = MatOfPoint2f(*contour.toArray())
            val rotatedRect = Imgproc.minAreaRect(contour2f)

            // Calculate normalized coordinates and angle
            val moments = Imgproc.moments(contour)
            val m00 = moments.m00
            if (m00 == 0.0) {
                contour2f.release()
                contour.release()
                continue
            }

            val cX = (moments.m10 / m00).toInt()
            val cY = (moments.m01 / m00).toInt()

            // Estimate distance
            val distanceRaw = estimateDistance(rotatedRect, 451.07, 8.6, 3.7)
            val distanceCm = distanceRaw * cos(Math.toRadians(cameraMountingAngle))

            // Real world coordinates
            val (sampleX, sampleY) = getSamplePositions(drawingMat, cX, cY, distanceCm)

            // Normalize coordinates
            val (normalizedX, normalizedY) = getNormalizedCoordinates(drawingMat, cX, cY)

            // Calculate adjusted angle
            var rotRectAngle = rotatedRect.angle
            if (rotatedRect.size.width < rotatedRect.size.height) {
                rotRectAngle += 90.0
            }
            val angle = -(rotRectAngle - 180)

            // Draw the contour and information
            drawRotatedRect(rotatedRect, drawingMat, color)

            // Draw angle information
            drawTagText(rotatedRect, "${Math.round(angle)} deg", drawingMat, color)

            // Add position, coordinate, and area text
            val position = Point(cX.toDouble(), cY.toDouble())
            Imgproc.putText(drawingMat, "Norm X: ${String.format("%.2f", normalizedX)}",
                Point(position.x, position.y + 20),
                Imgproc.FONT_HERSHEY_COMPLEX, 0.5, Scalar(255.0, 255.0, 255.0))
            Imgproc.putText(drawingMat, "Norm Y: ${String.format("%.2f", normalizedY)}",
                Point(position.x, position.y + 40),
                Imgproc.FONT_HERSHEY_COMPLEX, 0.5, Scalar(255.0, 255.0, 255.0))
            Imgproc.putText(drawingMat, "Area: ${String.format("%.0f", area)}",
                Point(position.x, position.y + 60),
                Imgproc.FONT_HERSHEY_COMPLEX, 0.5, Scalar(255.0, 255.0, 255.0))

            // Store analyzed contour data
            synchronized(analyzedContours) {
                analyzedContours.add(
                    AnalyzedContour(
                        rotatedRect,
                        angle,
                        stringToAlliance(color),
                        distanceCm,
                        Pair(normalizedX, normalizedY),
                        Pair(sampleX, sampleY),
                        area
                    )
                )
            }

            contour2f.release()
            contour.release()
        }
    }

    private fun stringToAlliance(string: String): OpModeData.Alliance {
        return when (string) {
            "Red" -> OpModeData.Alliance.RED
            "Blue" -> OpModeData.Alliance.BLUE
            else -> OpModeData.Alliance.NONE
        }
    }

    private fun getNormalizedCoordinates(input: Mat, cX: Int, cY: Int): Pair<Double, Double> {
        val width = input.width().toDouble()
        val height = input.height().toDouble()

        // Calculate center offsets
        val offsetX = cX - (width / 2)
        val offsetY = (height / 2) - cY  // Flipping Y so positive is up

        // Normalize to [-1, 1] range
        val normalizedX = (offsetX / (width / 2)).coerceIn(-1.0, 1.0)
        val normalizedY = (offsetY / (height / 2)).coerceIn(-1.0, 1.0)

        return Pair(normalizedX, normalizedY)
    }

    private fun getSamplePositions(input: Mat, cX: Int, cY: Int, distance: Double): Pair<Double, Double> {

        val centerX = input.width() / 2.0
        val centerY = input.height() / 2.0

        val offsetX = cX - centerX
        val offsetY = centerY - cY

        val angleX = atan(offsetX / 400)
        val angleY = atan(offsetY / 400)

        //Calculate Horizontal Offset (Requires Distance)
        val sampleX = distance * tan(angleX)
        val sampleY = distance * tan(angleY)
        return Pair(sampleX, sampleY)

    }

    private fun drawRotatedRect(rect: RotatedRect, mat: Mat, color: String) {
        val points = arrayOfNulls<Point>(4)
        rect.points(points)
        val colorScalar = getColorScalar(color)
        for (i in points.indices) {
            Imgproc.line(mat, points[i], points[(i + 1) % 4], colorScalar, 2)
        }
    }

    private fun drawTagText(rect: RotatedRect, text: String, mat: Mat, color: String) {
        val colorScalar = getColorScalar(color)
        Imgproc.putText(
            mat,
            text,
            Point(rect.center.x - 25, rect.center.y),
            Imgproc.FONT_HERSHEY_PLAIN,
            1.0,
            colorScalar,
            1
        )
    }

    private fun getColorScalar(color: String): Scalar {
        return when (color) {
            "Blue" -> Scalar(0.0, 0.0, 255.0)
            "Yellow" -> Scalar(255.0, 255.0, 0.0)
            else -> Scalar(255.0, 0.0, 0.0) // Red or default
        }
    }

    private fun undistortObjectPoints(rect: RotatedRect, cameraMatrix: Mat, distCoeffs: Mat): RotatedRect {
        // Create input object points (center of the rotated rectangle)
        val objectPoints = MatOfPoint2f(Point(rect.center.x, rect.center.y))

        // Prepare Mat for undistorted points
        val undistortedPoints = MatOfPoint2f()

        // Apply undistortion using the camera matrix and distortion coefficients
        Calib3d.undistortPoints(objectPoints, undistortedPoints, cameraMatrix, distCoeffs)

        // Retrieve the undistorted point (center) after transformation
        val undistortedCenter = undistortedPoints.toArray()[0]

        // Return a new RotatedRect with the undistorted center, same size, and angle
        return RotatedRect(undistortedCenter, rect.size, rect.angle)
    }

    private fun estimateDistance(rect: RotatedRect, fEffective: Double, realObjectWidth: Double, realObjectHeight: Double): Double {
        // Undistort the object first
        val undistortedRect = undistortObjectPoints(rect, cameraMatrix, distCoeffs)

        // Use the corrected object dimensions
        val detectedSizePixels = max(undistortedRect.size.width, undistortedRect.size.height)
        val realObjectSize = max(realObjectWidth, realObjectHeight)

        // Calculate estimated distance
        return (realObjectSize * fEffective) / detectedSizePixels
    }

    override fun onViewportTapped() {
        release()
    }

    fun getAnalyzedContours(): List<AnalyzedContour> {
        return analyzedContours
    }

    private fun release() {
        // Release all Mats to prevent memory leaks
        hsvMat.release()
        redMask1.release()
        redMask2.release()
        redMaskCombined.release()
        blueMask.release()
        yellowMask.release()
        morphedRedMask.release()
        morphedBlueMask.release()
        morphedYellowMask.release()
        edgesMat.release()
        dilatedEdges.release()
        erodeElement.release()
        dilateElement.release()
        closeElement.release()
    }
}