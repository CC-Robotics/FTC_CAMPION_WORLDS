package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.IMU
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadEx
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import org.firstinspires.ftc.teamcode.keymap.Keymap

/**
 * The system controlling the drivetrain of the bot (the system facilitating
 * power delivery to the wheels.) It uses holonomic drive with mecanum
 * wheels allowing for the bot to move omnidirectionally without turning.
 * */
object Drivetrain : SubsystemEx() {
    private val frontLeftName = "fL"
    private val frontRightName = "fR"
    private val backLeftName = "bL"
    private val backRightName = "bR"

    private lateinit var frontLeftMotor: MotorEx
    private lateinit var frontRightMotor: MotorEx
    private lateinit var backLeftMotor: MotorEx
    private lateinit var backRightMotor: MotorEx

    private lateinit var imu: IMU

    private lateinit var motors: Array<MotorEx>

    lateinit var driverControlled: Command

    override fun initialize() {
        frontLeftMotor = MotorEx(frontLeftName)
        backLeftMotor = MotorEx(backLeftName)
        backRightMotor = MotorEx(backRightName)
        frontRightMotor = MotorEx(frontRightName)

        imu = OpModeData.hardwareMap!!.get(IMU::class.java, "imu")

//        frontLeftMotor.direction = DcMotorSimple.Direction.REVERSE
//        backLeftMotor.direction = DcMotorSimple.Direction.REVERSE
//        // One side of wheels must have reversed motors
//        frontRightMotor.direction = DcMotorSimple.Direction.FORWARD
//        backRightMotor.direction = DcMotorSimple.Direction.FORWARD

        frontLeftMotor.direction = DcMotorSimple.Direction.FORWARD
        backLeftMotor.direction = DcMotorSimple.Direction.REVERSE
        frontRightMotor.direction = DcMotorSimple.Direction.REVERSE
        backRightMotor.direction = DcMotorSimple.Direction.FORWARD

        frontLeftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backLeftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        frontRightMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backRightMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motors = arrayOf(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor)
    }

    fun brake() {
        frontLeftMotor.power = 0.0
        backLeftMotor.power = 0.0
        frontRightMotor.power = 0.0
        backRightMotor.power = 0.0
    }

    override fun attach(keymap: Keymap) {
        // Use method which attaches mecanum drive control to the gamepad
        driverControlled = MecanumDriverControlled(motors, GamepadManager.gamepad1, false, imu)
        driverControlled()
    }
}