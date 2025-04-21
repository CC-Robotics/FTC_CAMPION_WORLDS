package org.firstinspires.ftc.teamcode.keymap

import com.rowanmcalpin.nextftc.ftc.gamepad.Button
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick

open class DefaultKeymap : Keymap {
    override val highLift = GamepadManager.gamepad2.dpadUp
    override val middleLift = GamepadManager.gamepad2.dpadLeft
    override val lowLift = GamepadManager.gamepad2.dpadDown

    // Wrist pitch
    override val upWristPitch = GamepadManager.gamepad2.y
    override val downWristPitch = GamepadManager.gamepad2.a

    // Wrist yaw
    override val upWristYaw = GamepadManager.gamepad2.rightBumper
    override val downWristYaw = GamepadManager.gamepad2.leftBumper

    // One-press actions (toggle claw, retract pinion)
    override val toggleClaw = GamepadManager.gamepad2.x
    override val retractPinion = GamepadManager.gamepad2.b

    // Outdated arm control
    override val arm = Joystick({ 0f }, { 0f }, { true }, 0f, 0f)

    // Lift control
    override val lift = GamepadManager.gamepad2.leftStick

    // Pinion control
    override val pinion = GamepadManager.gamepad2.rightStick

    override val flatPitch = GamepadManager.gamepad2.dpadRight
    override val straightPitch = GamepadManager.gamepad2.dpadUp
    override val resetIMU = GamepadManager.gamepad2.options
    override val toggleYaw = GamepadManager.gamepad2.dpadLeft
    override val quickAction = GamepadManager.gamepad2.dpadDown
}