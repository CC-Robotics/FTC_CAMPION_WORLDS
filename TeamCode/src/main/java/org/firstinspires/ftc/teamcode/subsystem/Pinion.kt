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
 * Controls the pinion component of the rack and pinion system of the robot which is mounted on the
 * vertical lift. It uses a PIDF controller to ensure smooth and accurate motion. Theoretically,
 * feedforward is not necessary at all, however potential use lies in overcoming friction.
 */
object Pinion : SubsystemEx() {
    lateinit var motor: MotorEx

    /**
     * Boolean indicating if the control system should update the controllable
     * in the periodic function of the subsystem in each loop.
     * */
    @JvmField
    var useControl = false

    @JvmField
    var coefficients = PIDCoefficients(0.0, 0.0, 0.0)

    /**
     * PID Control system with a static feedforward term for that extra "push" to get it
     * moving quickly.
     */
    val controlSystem = ControlSystem(
        PIDElement(PIDType.POSITION, coefficients),
        { 0.0 },
        FilterElement(),
        ConstantInterpolator(KineticState())
    )

    private const val NAME = "pinion"

    // Movement commands, which are also bound to the gamepad

//    val toHigh: Command
//        get() = RunToPosition(
//            1200.0,
//            controlSystem,
//            this
//        )

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
//        keymap.highLift.pressedCommand = { toHigh }
    }
}