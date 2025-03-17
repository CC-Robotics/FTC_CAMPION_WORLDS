package org.firstinspires.ftc.teamcode.keymap

import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick

class DefaultKeymap(gamepadManager: GamepadManager) : Keymap {
    override val highLift = gamepadManager.gamepad2.dpadUp
    override val middleLift = gamepadManager.gamepad2.dpadLeft
    override val lowLift = gamepadManager.gamepad2.dpadDown

    override val arm = gamepadManager.gamepad2.leftStick
}