package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp(name = "Pinion + Lift (Raw Power)", group = "Testing")
class PinionLiftTest : LinearOpMode() {
    lateinit var lift: DcMotor
    lateinit var pinion: DcMotor

    /**
     * Describe this function...
     */
    override fun runOpMode() {
        lift = hardwareMap.get(DcMotor::class.java, "lift")
        pinion = hardwareMap.get(DcMotor::class.java, "pinion")

        pinion.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        pinion.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lift.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        waitForStart()
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                lift.power = -gamepad2.right_stick_y.toDouble()
                pinion.power = -gamepad2.left_stick_y.toDouble()
                telemetry.addData("pinion pos", pinion.currentPosition)
                telemetry.addData("lift", lift.currentPosition)
                telemetry.addData("right power", -gamepad2.right_stick_y)
                telemetry.addData("left power", -gamepad2.left_stick_y)
                telemetry.update()
            }
        }
    }
}