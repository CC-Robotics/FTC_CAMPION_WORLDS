package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import org.firstinspires.ftc.teamcode.keymap.Keymap

/**
 * Controls the pinion component of the rack and pinion system of the robot which is mounted on the
 * vertical lift. It uses a PIDF controller to ensure smooth and accurate motion. Theoretically,
 * feedforward is not necessary at all, however potential use lies in overcoming friction.
 */
@Config
object PinionRaw : SubsystemEx() {
    private lateinit var motor: MotorEx

    private const val NAME = "pinion"

    override fun initialize() {
        motor = MotorEx(NAME)
        motor.resetEncoder()
    }

    override fun attach(keymap: Keymap) {
        keymap.pinion.heldCommand = { xy ->
            InstantCommand { motor.power = xy.second.toDouble() }
        }
    }
}