package org.firstinspires.ftc.teamcode.keymap

import com.rowanmcalpin.nextftc.ftc.gamepad.Button
import com.rowanmcalpin.nextftc.ftc.gamepad.Joystick

interface Keymap {
    val highLift: Button
    val middleLift: Button
    val lowLift: Button

    val toggleClaw: Button
    val retractPinion: Button

    val upWristYaw: Button
    val downWristYaw: Button

    val upWristPitch: Button
    val downWristPitch: Button

    val straightPitch: Button
    val flatPitch: Button
    val resetIMU: Button
    val toggleYaw: Button
    val quickAction: Button

    val arm: Joystick
    val lift: Joystick
    val pinion: Joystick
}