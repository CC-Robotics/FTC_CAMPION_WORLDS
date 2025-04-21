package org.firstinspires.ftc.teamcode.subsystem.novaclaw

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.IMU
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.subsystem.SubsystemEx

/**
 * The system controlling the drivetrain of the bot (the system facilitating
 * power delivery to the wheels.) It uses holonomic drive with mecanum
 * wheels allowing for the bot to move omnidirectionally without turning.
 * */
object NovaDrivetrain : SubsystemEx() {
    private const val FRONT_LEFT_NAME = "fL"
    private const val FRONT_RIGHT_NAME = "fR"
    private const val BACK_LEFT_NAME = "bL"
    private const val BACK_RIGHT_NAME = "bR"

    private lateinit var frontLeftMotor: MotorEx
    private lateinit var frontRightMotor: MotorEx
    private lateinit var backLeftMotor: MotorEx
    private lateinit var backRightMotor: MotorEx

    private lateinit var imu: IMU

    private lateinit var motors: Array<MotorEx>

    lateinit var driverControlled: Command

    override fun initialize() {
        frontLeftMotor = MotorEx(FRONT_LEFT_NAME)
        backLeftMotor = MotorEx(BACK_LEFT_NAME)
        backRightMotor = MotorEx(BACK_RIGHT_NAME)
        frontRightMotor = MotorEx(FRONT_RIGHT_NAME)

        imu = OpModeData.hardwareMap!!.get(IMU::class.java, "imu")

        val logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP
        val usbDirection = UsbFacingDirection.LEFT

        val orientationOnRobot = RevHubOrientationOnRobot(logoDirection, usbDirection)

        imu.initialize(IMU.Parameters(orientationOnRobot))

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
        driverControlled = MecanumDriverControlled(motors, GamepadManager.gamepad1, true, imu)
        driverControlled()
        keymap.resetIMU.pressedCommand = { InstantCommand { imu.resetYaw() } }
    }
}