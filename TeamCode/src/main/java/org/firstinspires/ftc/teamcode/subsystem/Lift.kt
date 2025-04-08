package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.clamp
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState
import dev.nextftc.nextcontrol.feedback.PIDCoefficients
import dev.nextftc.nextcontrol.feedback.PIDElement
import dev.nextftc.nextcontrol.feedback.PIDType
import dev.nextftc.nextcontrol.filters.FilterElement
import dev.nextftc.nextcontrol.interpolators.ConstantInterpolator
import org.firstinspires.ftc.teamcode.archive.subsystem.Arm
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
    var kF = 0.0 // Feedforward term

    @JvmField
    var kPinion = 0.0 // Multiplier of pinion position to add to the feedforward term

    @JvmField
    var targetPosition = 0.0

    /**
     * PID Control system with a static feedforward term for that extra "push" to get it
     * moving quickly.
     */
    val controlSystem = ControlSystem(
        PIDElement(PIDType.POSITION, coefficients),
        { kF + Pinion.motor.currentPosition * kPinion },
        FilterElement(),
        ConstantInterpolator(KineticState())
    )

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
        motor = MotorEx("lift")
    }

    override fun periodic() {
        if (useControl) {
            motor.power =
                controlSystem.calculate(KineticState(motor.currentPosition, motor.velocity))
        }

        RobotUtil.telemetry.addData("[Lift] Target Position", targetPosition)
        RobotUtil.telemetry.addData("[Lift] Current Position", motor.currentPosition)
        RobotUtil.telemetry.addData("[Lift] Power", motor.power)
    }

    override fun attach(keymap: Keymap) {
        keymap.lift.heldCommand = { xy ->
            targetPosition += xy.second
            targetPosition = clamp(targetPosition, 0.0, Double.POSITIVE_INFINITY)
            runToPosition
        }
//        keymap.highLift.pressedCommand = { toHigh }
//        keymap.middleLift.pressedCommand = { toMiddle }
//        keymap.lowLift.pressedCommand = { toLow }
    }
}