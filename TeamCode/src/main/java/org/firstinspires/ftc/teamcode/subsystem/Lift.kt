package org.firstinspires.ftc.teamcode.subsystem

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition
import org.firstinspires.ftc.teamcode.keymap.Keymap

object Lift: Subsystem() {
    lateinit var motor: MotorEx

    private val controller = PIDFController(0.008, 0.002, 0.0002, StaticFeedforward(0.0005))

    private const val NAME = "slide"

    private val toLow: Command
        get() = RunToPosition(motor,
            0.0,
            controller,
            this)

    private val toMiddle: Command
        get() = RunToPosition(motor,
            500.0,
            controller,
            this)

    private val toHigh: Command
        get() = RunToPosition(motor,
            1200.0,
            controller,
            this)

    override fun initialize() {
        motor = MotorEx(NAME)
    }

    fun attach(keymap: Keymap) {
        keymap.highLift.pressedCommand = { toHigh }
        keymap.middleLift.pressedCommand = { toMiddle }
        keymap.lowLift.pressedCommand = { toLow }
    }
}