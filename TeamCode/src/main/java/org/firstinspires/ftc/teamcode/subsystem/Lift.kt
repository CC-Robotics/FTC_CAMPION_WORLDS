package org.firstinspires.ftc.teamcode.subsystem

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState
import dev.nextftc.nextcontrol.feedback.PIDElement
import dev.nextftc.nextcontrol.feedback.PIDType
import dev.nextftc.nextcontrol.filters.FilterElement
import dev.nextftc.nextcontrol.interpolators.ConstantInterpolator
import org.firstinspires.ftc.teamcode.command.RunToPosition
import org.firstinspires.ftc.teamcode.keymap.Keymap

object Lift: SubsystemEx() {
    private lateinit var motor: MotorEx

    @JvmField var useControl = false

    private val controlSystem = ControlSystem(
        PIDElement(PIDType.POSITION, 0.008, 0.002, 0.0002),
        { 0.0005 },
        FilterElement(),
        ConstantInterpolator(KineticState())
    )

    private const val NAME = "slide"

    val toLow: Command
        get() = RunToPosition(
            0.0,
            controlSystem,
            this)

    val toMiddle: Command
        get() = RunToPosition(
            500.0,
            controlSystem,
            this)

    val toHigh: Command
        get() = RunToPosition(
            1200.0,
            controlSystem,
            this)

    override fun initialize() {
        motor = MotorEx(NAME)
    }

    override fun periodic() {
        if (useControl) {
            motor.power = controlSystem.calculate(KineticState(motor.currentPosition, motor.velocity))
        }
    }

    override fun attach(gamepadManager: GamepadManager, keymap: Keymap) {
        keymap.highLift.pressedCommand = { toHigh }
        keymap.middleLift.pressedCommand = { toMiddle }
        keymap.lowLift.pressedCommand = { toLow }
    }
}