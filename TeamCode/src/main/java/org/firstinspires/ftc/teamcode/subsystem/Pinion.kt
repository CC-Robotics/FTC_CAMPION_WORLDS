package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.clamp
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import dev.nextftc.nextcontrol.builder.controlSystem
import dev.nextftc.nextcontrol.feedback.PIDCoefficients
import dev.nextftc.nextcontrol.feedforward.BasicFeedforwardParameters
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
    private lateinit var motor: MotorEx

    /**
     * Boolean indicating if the control system should update the controllable
     * in the periodic function of the subsystem in each loop.
     * */
    @JvmField
    var useControl = true

    @JvmField
    var coefficients = PIDCoefficients(0.03, 0.0, 0.0)

    @JvmField
    var targetPosition = 0.0

    @JvmField
    var feedforwardParameters = BasicFeedforwardParameters()

    @JvmField
    var multiplier = 1.0

    /**
     * PID Control system with a static feedforward term for that extra "push" to get it
     * moving quickly.
     */
    private val controlSystem = controlSystem {
        posPid(coefficients)
        basicFF(feedforwardParameters)
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
        motor.resetEncoder()
    }

    override fun periodic() {
        RobotUtil.handleControl("Pinion", useControl, motor, controlSystem, targetPosition)
    }

    private val runToPosition: RunToPosition
        get() = RunToPosition(targetPosition, controlSystem, this)

    override fun attach(keymap: Keymap) {
        keymap.pinion.heldCommand = { xy ->
            targetPosition += xy.second * multiplier
            targetPosition = clamp(targetPosition, 0.0, Double.POSITIVE_INFINITY)
            runToPosition
        }
//        keymap.highLift.pressedCommand = { toHigh }
    }
}