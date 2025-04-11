package org.firstinspires.ftc.teamcode.opmode.teleop.control

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Effector
import org.firstinspires.ftc.teamcode.subsystem.LiftRaw
import org.firstinspires.ftc.teamcode.subsystem.PinionRaw
import org.firstinspires.ftc.teamcode.util.RobotUtil

@TeleOp(name = "Everything Control", group = "Testing")
class EverythingControl : NextFTCOpMode() {
    private lateinit var liftLeft: MotorEx
    private lateinit var liftRight: MotorEx

    private lateinit var lift: MotorGroup

    private val subsystems = arrayOf(Effector, Drivetrain, PinionRaw, LiftRaw)

    override val components = Components()
        .useGamepads()
        .useSubsystems(*subsystems)
        .useBulkReading()

    lateinit var pinion: MotorEx

//    private lateinit var fL: MotorEx
//    private lateinit var fR: MotorEx
//    private lateinit var bL: MotorEx
//    private lateinit var bR: MotorEx

    lateinit var keymap: Keymap

    /**
     * Describe this function...
     */
    override fun onInit() {
//        fL = MotorEx("fL")
//        fR = MotorEx("fR")
//        bL = MotorEx("bL")
//        bR = MotorEx("bR")

        liftLeft = MotorEx("lift_left")
        liftRight = MotorEx("lift_right")

        pinion = MotorEx("pinion")

        lift = RobotUtil.motorGroupFromNames("lift_right", "lift_left")
        lift.leader.reverse()
        lift.leader.resetEncoder()

//        fL.direction = DcMotorSimple.Direction.FORWARD
//        bL.direction = DcMotorSimple.Direction.REVERSE
//        fR.direction = DcMotorSimple.Direction.REVERSE
//        bR.direction = DcMotorSimple.Direction.FORWARD
//
//        fL.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        bL.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        fR.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        bR.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        pinion.resetEncoder()
    }

    override fun onStartButtonPressed() {
        keymap = DefaultKeymap()

        // Attach the keymap to all subsystems. (Binds them to the gamepad)
        for (subsystem in subsystems)
            subsystem.attach(keymap)
    }

    override fun onUpdate() {
        // Lift and Pinion
        lift.power = keymap.lift.y.toDouble()

        telemetry.addData("pinion pos", pinion.currentPosition)
        telemetry.addData("lift", liftLeft.currentPosition)

        telemetry.addData("lift power", -gamepad2.right_stick_y)
        telemetry.addData("pinion power", -gamepad2.left_stick_y)

        telemetry.update()
    }


//    private fun updateDrivetrain() {
//        val y = GamepadManager.gamepad1.leftStick.y // Remember, Y stick value is reversed
//        val x = GamepadManager.gamepad1.leftStick.x * 1.1 // Counteract imperfect strafing
//        val rx = GamepadManager.gamepad1.rightStick.x.toDouble()
//
//
//        // Denominator is the largest motor power (absolute value) or 1
//        // This ensures all the powers maintain the same ratio,
//        // but only if at least one is out of the range [-1, 1]
//        val denominator = max(abs(y) + abs(x) + abs(rx), 1.0)
//        val frontLeftPower = (y + x + rx) / denominator
//        val backLeftPower = (y - x + rx) / denominator
//        val frontRightPower = (y - x - rx) / denominator
//        val backRightPower = (y + x - rx) / denominator
//
//        fL.power = frontLeftPower
//        bL.power = backLeftPower
//        fR.power = frontRightPower
//        bR.power = backRightPower
//    }
}