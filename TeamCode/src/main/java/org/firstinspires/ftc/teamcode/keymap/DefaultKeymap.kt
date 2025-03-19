package org.firstinspires.ftc.teamcode.keymap

import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager

class DefaultKeymap(gamepadManager: GamepadManager) : Keymap {
    override val highLift = gamepadManager.gamepad2.dpadUp
    override val middleLift = gamepadManager.gamepad2.dpadLeft
    override val lowLift = gamepadManager.gamepad2.dpadDown

    override val toggleClaw = gamepadManager.gamepad2.x

    override val arm = gamepadManager.gamepad2.leftStick
}