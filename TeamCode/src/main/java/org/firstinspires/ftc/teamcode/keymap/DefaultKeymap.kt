package org.firstinspires.ftc.teamcode.keymap

import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager

open class DefaultKeymap : Keymap {
    override val highLift = GamepadManager.gamepad2.dpadUp
    override val middleLift = GamepadManager.gamepad2.dpadLeft
    override val lowLift = GamepadManager.gamepad2.dpadDown

    override val toggleClaw = GamepadManager.gamepad2.x

    override val arm = GamepadManager.gamepad2.leftStick
}