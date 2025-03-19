package org.firstinspires.ftc.teamcode.keymap

import com.rowanmcalpin.nextftc.ftc.gamepad.Button
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick

interface Keymap {
    val highLift: Button
    val middleLift: Button
    val lowLift: Button

    val toggleClaw: Button

    val arm: Joystick
}