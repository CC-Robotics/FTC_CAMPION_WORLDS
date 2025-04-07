package org.firstinspires.ftc.teamcode.subsystem

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState
import dev.nextftc.nextcontrol.feedback.PIDCoefficients
import dev.nextftc.nextcontrol.feedback.PIDElement
import dev.nextftc.nextcontrol.feedback.PIDType
import dev.nextftc.nextcontrol.filters.FilterElement
import dev.nextftc.nextcontrol.interpolators.ConstantInterpolator
import org.firstinspires.ftc.teamcode.command.RunToPosition
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.util.Utils

/**
 * Controls the dual lift (linear slide) system of the robot running vertically in which the rack
 * and pinion is mounted on. It uses a PIDF controller to ensure smooth and accurate motion.
 * A static base feedforward is used but variation is implemented to mitigate additional
 * torque from the extension of the somewhat heavy rack.
 */
object Lifts : SubsystemEx() {
    lateinit var motors: MotorGroup

    /**
     * Boolean indicating if the control system should update the controllable
     * in the periodic function of the subsystem in each loop.
     * */
    @JvmField
    var useControl = false

    @JvmField
    var coefficients = PIDCoefficients(0.0, 0.0, 0.0)

    @JvmField
    var kF = 0.0 // Feedforward term

    @JvmField
    var kPinion = 0.0 // Multiplier of pinion position to add to the feedforward term

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

    override fun initialize() {
        motors = Utils.motorGroupFromNames("lift1", "lift2")
    }

    override fun periodic() {
        if (useControl) {
            motors.power =
                controlSystem.calculate(KineticState(motors.currentPosition, motors.velocity))
        }
    }

    override fun attach(keymap: Keymap) {
        keymap.highLift.pressedCommand = { toHigh }
        keymap.middleLift.pressedCommand = { toMiddle }
        keymap.lowLift.pressedCommand = { toLow }
    }
}