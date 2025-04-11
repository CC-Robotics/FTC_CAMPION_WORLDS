package org.firstinspires.ftc.teamcode.opmode.teleop.control

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.abs
import kotlin.math.max

@TeleOp(name = "Pinion + Lift + Drivetrain (Raw Power)", group = "Testing")
class PinionLiftDrivetrainControl : LinearOpMode() {
    lateinit var liftLeft: DcMotor
    lateinit var liftRight: DcMotor

    lateinit var pinion: DcMotor

    lateinit var fL: DcMotor
    lateinit var fR: DcMotor
    lateinit var bL: DcMotor
    lateinit var bR: DcMotor

    /**
     * Describe this function...
     */
    override fun runOpMode() {
        fL = hardwareMap.get(DcMotor::class.java, "fL")
        fR = hardwareMap.get(DcMotor::class.java, "fR")
        bL = hardwareMap.get(DcMotor::class.java, "bL")
        bR = hardwareMap.get(DcMotor::class.java, "bR")

        fL.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        bL.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        fR.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        bR.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        liftLeft = hardwareMap.get(DcMotor::class.java, "lift_left")
        liftRight = hardwareMap.get(DcMotor::class.java, "lift_right")

        pinion = hardwareMap.get(DcMotor::class.java, "pinion")

        fL.direction = DcMotorSimple.Direction.FORWARD
        bL.direction = DcMotorSimple.Direction.REVERSE
        fR.direction = DcMotorSimple.Direction.REVERSE
        bR.direction = DcMotorSimple.Direction.FORWARD

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

                updateDrivetrain()

                telemetry.addData("pinion pos", pinion.currentPosition)
                telemetry.addData("lift", liftLeft.currentPosition)
                telemetry.addData("lift power", -gamepad2.right_stick_y)
                telemetry.addData("pinion power", -gamepad2.left_stick_y)

                telemetry.update()
            }
        }
    }

    private fun updateDrivetrain() {
        val y = -gamepad1.left_stick_y.toDouble() // Remember, Y stick value is reversed
        val x = gamepad1.left_stick_x * 1.1 // Counteract imperfect strafing
        val rx = gamepad1.right_stick_x.toDouble()


        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        val denominator = max(abs(y) + abs(x) + abs(rx), 1.0)
        val frontLeftPower = (y + x + rx) / denominator
        val backLeftPower = (y - x + rx) / denominator
        val frontRightPower = (y - x - rx) / denominator
        val backRightPower = (y + x - rx) / denominator

        fL.power = frontLeftPower
        bL.power = backLeftPower
        fR.power = frontRightPower
        bR.power = backRightPower
    }
}