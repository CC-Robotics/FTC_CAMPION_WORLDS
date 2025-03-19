package org.firstinspires.ftc.teamcode.subsystem

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import org.firstinspires.ftc.teamcode.keymap.Keymap

open class SubsystemEx : Subsystem() {
    open fun attach(gamepadManager: GamepadManager, keymap: Keymap) {}
}