package org.firstinspires.ftc.teamcode.subsystem

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState
import dev.nextftc.nextcontrol.feedback.PIDCoefficients
import dev.nextftc.nextcontrol.feedback.PIDElement
import dev.nextftc.nextcontrol.feedback.PIDType
import dev.nextftc.nextcontrol.filters.FilterElement
import dev.nextftc.nextcontrol.interpolators.ConstantInterpolator
import org.firstinspires.ftc.teamcode.command.RunToPosition
import org.firstinspires.ftc.teamcode.keymap.Keymap

/**
 * Controls the lift (linear slide) system of the robot which is mounted on the arm (pivot.)
 * It uses a PIDF controller to ensure smooth and accurate motion. Only static feedforward is
 * necessary here.
 */
object Lift : SubsystemEx() {
    lateinit var motor: MotorEx

    /**
     * Boolean indicating if the control system should update the controllable
     * in the periodic function of the subsystem in each loop.
     * */
    @JvmField
    var useControl = false

    @JvmField
    var coefficients = PIDCoefficients(0.0, 0.0, 0.0)

    @JvmField
    var kF = 0.0

    /**
     * PID Control system with a static feedforward term for that extra "push" to get it
     * moving quickly.
     */
    val controlSystem = ControlSystem(
        PIDElement(PIDType.POSITION, coefficients),
        { kF },
        FilterElement(),
        ConstantInterpolator(KineticState())
    )

    private const val NAME = "slide"

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
        motor = MotorEx(NAME)
    }

    override fun periodic() {
        if (useControl) {
            motor.power =
                controlSystem.calculate(KineticState(motor.currentPosition, motor.velocity))
        }
    }

    override fun attach(keymap: Keymap) {
        keymap.highLift.pressedCommand = { toHigh }
        keymap.middleLift.pressedCommand = { toMiddle }
        keymap.lowLift.pressedCommand = { toLow }
    }
}