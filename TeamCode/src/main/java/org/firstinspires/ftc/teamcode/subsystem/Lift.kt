package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.clamp
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import dev.nextftc.nextcontrol.builder.controlSystem
import dev.nextftc.nextcontrol.feedback.PIDCoefficients
import dev.nextftc.nextcontrol.feedforward.GravityFeedforwardParameters
import org.firstinspires.ftc.teamcode.command.RunToPosition
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.util.RobotUtil

/**
 * Controls the lift (linear slide) system of the robot running vertically in which the rack
 * and pinion is mounted on. It uses a PIDF controller to ensure smooth and accurate motion.
 * A static base feedforward is used but variation is implemented to mitigate additional
 * torque from the extension of the somewhat heavy rack.
 */
@Config
object Lift : SubsystemEx() {
    private lateinit var motors: MotorGroup

    /**
     * Boolean indicating if the control system should update the controllable
     * in the periodic function of the subsystem in each loop.
     * */
    @JvmField
    var useControl = true

    @JvmField
    var coefficients = PIDCoefficients(0.027, 0.0, 0.0)

    @JvmField
    var feedforwardParameters = GravityFeedforwardParameters(0.001) // Feedforward term

    @JvmField
    var targetPosition = 0.0

    @JvmField
    var upwardMultiplier = 10.0

    @JvmField
    var downwardMultiplier = 25.0

    /**
     * PID Control system with a static feedforward term to counteract gravity
     */
    private val controlSystem = controlSystem {
        posPid(coefficients)
        elevatorFF(feedforwardParameters)
    }

    // Movement commands, which are also bound to the gamepad

//    val toLow: Command
//        get() = RunToPosition(
//            0.0,
//            controlSystem,
//            this
//        )
//
//    val toMiddle: Command
//        get() = RunToPosition(
//            500.0,
//            controlSystem,
//            this
//        )
//
//    val toHigh: Command
//        get() = RunToPosition(
//            1200.0,
//            controlSystem,
//            this
//        )

    private val runToPosition: RunToPosition
        get() = RunToPosition(targetPosition, controlSystem, this)

    override fun initialize() {
        targetPosition = 0.0
        motors = MotorGroup("lift_right", "lift_left")
        motors.leader.reverse()
        motors.leader.resetEncoder()
    }

    val toLow: Command
        get() = RunToPosition(
            0.0,
            controlSystem,
            this
        )

    val toMiddle: Command
        get() = RunToPosition(
            500.0,
            controlSystem,
            this
        )

    val toHigh: Command
        get() = RunToPosition(
            1200.0,
            controlSystem,
            this
        )

    override fun periodic() {
        RobotUtil.handleControl(
            "Lift",
            useControl,
            motors,
            controlSystem,
            targetPosition,
            runToPosition
        )
    }

    override fun attach(keymap: Keymap) {
        keymap.lift.heldCommand = { xy ->
            targetPosition += if (xy.second > 0) {
                xy.second * upwardMultiplier
            } else {
                xy.second * downwardMultiplier
            }
            targetPosition = clamp(targetPosition, 0.0, 9030.0)
            runToPosition
        }
        keymap.highLift.pressedCommand = { toHigh }
        keymap.middleLift.pressedCommand = { toMiddle }
        keymap.lowLift.pressedCommand = { toLow }
//        keymap.highLift.pressedCommand = { toHigh }
//        keymap.middleLift.pressedCommand = { toMiddle }
//        keymap.lowLift.pressedCommand = { toLow }
    }
}