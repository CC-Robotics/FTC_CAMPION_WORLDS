package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadEx
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import org.firstinspires.ftc.teamcode.keymap.Keymap

object Drivetrain : SubsystemEx() {
    private val frontLeftName = "fL"
    private val frontRightName = "fR"
    private val backLeftName = "bL"
    private val backRightName = "bR"

    private lateinit var frontLeftMotor: MotorEx
    private lateinit var frontRightMotor: MotorEx
    private lateinit var backLeftMotor: MotorEx
    private lateinit var backRightMotor: MotorEx

    private lateinit var motors: Array<MotorEx>

    lateinit var driverControlled: Command

    override fun initialize() {
        frontLeftMotor = MotorEx(frontLeftName)
        backLeftMotor = MotorEx(backLeftName)
        backRightMotor = MotorEx(backRightName)
        frontRightMotor = MotorEx(frontRightName)

        // Change the motor directions to suit your robot.
        frontLeftMotor.direction = DcMotorSimple.Direction.FORWARD
        backLeftMotor.direction = DcMotorSimple.Direction.FORWARD
        frontRightMotor.direction = DcMotorSimple.Direction.REVERSE
        backRightMotor.direction = DcMotorSimple.Direction.REVERSE

        motors = arrayOf(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor)
    }

    override fun attach(gamepadManager: GamepadManager, keymap: Keymap) {
        driverControlled = MecanumDriverControlled(motors, gamepadManager.gamepad1)
        driverControlled()
    }
}