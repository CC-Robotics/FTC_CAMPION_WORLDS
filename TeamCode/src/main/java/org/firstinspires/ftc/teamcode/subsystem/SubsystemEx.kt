package org.firstinspires.ftc.teamcode.subsystem

import com.rowanmcalpin.nextftc.core.Subsystem
import org.firstinspires.ftc.teamcode.keymap.Keymap

/**
 * A little class which extends the subsystem class and implements an attach method for automatic
 * Subsystem keybind loading.
 * */
open class SubsystemEx : Subsystem() {
    open fun attach(keymap: Keymap) {}
}