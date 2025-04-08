package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.clamp
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import dev.nextftc.nextcontrol.KineticState
import dev.nextftc.nextcontrol.builder.controlSystem
import dev.nextftc.nextcontrol.feedback.PIDCoefficients
import org.firstinspires.ftc.teamcode.command.RunToPosition
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.util.RobotUtil

/**
 * Controls the pinion component of the rack and pinion system of the robot which is mounted on the
 * vertical lift. It uses a PIDF controller to ensure smooth and accurate motion. Theoretically,
 * feedforward is not necessary at all, however potential use lies in overcoming friction.
 */
@Config
object Pinion : SubsystemEx() {
    lateinit var motor: MotorEx

    /**
     * Boolean indicating if the control system should update the controllable
     * in the periodic function of the subsystem in each loop.
     * */
    @JvmField
    var useControl = true

    @JvmField
    var coefficients = PIDCoefficients(0.0, 0.0, 0.0)

    @JvmField
    var targetPosition = 0.0

    @JvmField
    var kF = 0.0

    /**
     * PID Control system with a static feedforward term for that extra "push" to get it
     * moving quickly.
     */
    val controlSystem = controlSystem {
        posPid(coefficients)
        basicFF(kS = kF)
    }

    private const val NAME = "pinion"

    // Movement commands, which are also bound to the gamepad

//    val toHigh: Command
//        get() = RunToPosition(
//            1200.0,
//            controlSystem,
//            this
//        )

    override fun initialize() {
        targetPosition = 0.0
        motor = MotorEx(NAME)
    }

    override fun periodic() {
        if (useControl) {
            motor.power =
                controlSystem.calculate(KineticState(motor.currentPosition, motor.velocity))
        }

        RobotUtil.telemetry.addData("[Pinion] Target Position", targetPosition)
        RobotUtil.telemetry.addData("[Pinion] Current Position", motor.currentPosition)
        RobotUtil.telemetry.addData("[Pinion] Power", motor.power)
    }

    private val runToPosition: RunToPosition
        get() = RunToPosition(targetPosition, controlSystem, this)

    override fun attach(keymap: Keymap) {
        keymap.pinion.heldCommand = { xy ->
            targetPosition += xy.second
            targetPosition = clamp(targetPosition, 0.0, Double.POSITIVE_INFINITY)
            runToPosition
        }
//        keymap.highLift.pressedCommand = { toHigh }
    }
}