package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.rowanmcalpin.nextftc.ftc.OpModeData
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.util.RobotGlobals
import org.firstinspires.ftc.teamcode.util.RobotUtil
import org.firstinspires.ftc.teamcode.util.nearTo
import org.firstinspires.ftc.teamcode.vision.CrocSight
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvWebcam

@Config
object Vision : SubsystemEx() {

    private lateinit var camera: OpenCvWebcam
    private lateinit var pipeline: CrocSight

    @JvmField var xOffset = 0
    @JvmField var yOffset = 0

    lateinit var camName: WebcamName

    private fun createCamera(): OpenCvWebcam {
        val cameraMonitorViewId = OpModeData.hardwareMap!!.appContext.resources.getIdentifier(
            "cameraMonitorViewId",
            "id",
            OpModeData.hardwareMap!!.appContext.packageName,

            )

        val camera = OpenCvCameraFactory.getInstance().createWebcam(camName, cameraMonitorViewId)

        return camera
    }


    fun isCentered(): Boolean {
        val contour = getBestContour() ?: return false
        return (contour.normalizedCoords.first + xOffset).nearTo(0.0, 0.1) &&
                (contour.normalizedCoords.second + yOffset).nearTo(0.0, 0.15)
    }

    override fun initialize() {
        camName = OpModeData.hardwareMap!!.get(WebcamName::class.java, "CrocCam")
        camera = createCamera()
        pipeline = CrocSight()
        camera.openCameraDeviceAsync(object : AsyncCameraOpenListener {
            override fun onOpened() {
                // Start the streaming session with desired resolution and orientation
                camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT)
                FtcDashboard.getInstance().startCameraStream(camera, 0.0)


                // Attach the processing pipeline
                camera.setPipeline(pipeline)
            }

            override fun onError(errorCode: Int) {
                // Handle error (e.g., log the error code)
                RobotUtil.telemetry.addData("Camera Error", errorCode)
                RobotUtil.telemetry.update()
            }
        })
    }

    fun getContours(): List<CrocSight.AnalyzedContour> {
        val oppositeColor = if (OpModeData.alliance == OpModeData.Alliance.RED) OpModeData.Alliance.BLUE else OpModeData.Alliance.RED
        val contours = pipeline.getAnalyzedContours().filter { it.color !== oppositeColor }.sortedBy { it.sampleCoords.first + it.sampleCoords.second  }
        return contours
    }

    fun getBestContour(): CrocSight.AnalyzedContour? {
        var contours = getContours()

        if (RobotGlobals.strategy == RobotGlobals.Strategy.SAMPLE) {
            contours = contours.filter { it.color != OpModeData.alliance }
        } else if (RobotGlobals.strategy == RobotGlobals.Strategy.SPECIMEN) {
            contours = contours.filter { it.color != OpModeData.Alliance.NONE }
        }

        if (contours.isEmpty()) return null

        return contours[0]
    }
}