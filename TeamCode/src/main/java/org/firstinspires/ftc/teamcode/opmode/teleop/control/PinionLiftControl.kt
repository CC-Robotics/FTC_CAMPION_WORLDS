package org.firstinspires.ftc.teamcode.opmode.teleop.control

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp(name = "Pinion + Lift (Raw Power)", group = "Testing")
class PinionLiftControl : LinearOpMode() {
    lateinit var liftLeft: DcMotor
    lateinit var liftRight: DcMotor

    lateinit var pinion: DcMotor

    /**
     * Describe this function...
     */
    override fun runOpMode() {
        liftLeft = hardwareMap.get(DcMotor::class.java, "lift_left")
        liftRight = hardwareMap.get(DcMotor::class.java, "lift_right")

        pinion = hardwareMap.get(DcMotor::class.java, "pinion")

        pinion.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        liftLeft.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        liftRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        pinion.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        liftLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        liftRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        liftRight.direction = DcMotorSimple.Direction.REVERSE

        waitForStart()
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                // Lift and Pinion
                liftLeft.power = -gamepad2.right_stick_y.toDouble()
                liftRight.power = -gamepad2.right_stick_y.toDouble()
                pinion.power = -gamepad2.left_stick_y.toDouble()

                telemetry.addData("pinion pos", pinion.currentPosition)
                telemetry.addData("lift", liftLeft.currentPosition)
                telemetry.addData("lift power", -gamepad2.right_stick_y)
                telemetry.addData("pinion power", -gamepad2.left_stick_y)

                telemetry.update()
            }
        }
    }
}