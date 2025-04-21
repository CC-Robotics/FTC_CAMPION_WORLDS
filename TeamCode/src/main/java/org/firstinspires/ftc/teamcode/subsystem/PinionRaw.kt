package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.util.RobotGlobals
import org.firstinspires.ftc.teamcode.util.RobotUtil

/**
 * Controls the pinion component of the rack and pinion system of the robot which is mounted on the
 * vertical lift. It uses a PIDF controller to ensure smooth and accurate motion. Theoretically,
 * feedforward is not necessary at all, however potential use lies in overcoming friction.
 */
@Config
object PinionRaw : SubsystemEx() {
    lateinit var motor: MotorEx

    private const val NAME = "pinion"

    var started = false

    override fun initialize() {
        motor = MotorEx(NAME)
        motor.resetEncoder()
        started = false
    }

    override fun attach(keymap: Keymap) {
        started = true
    }

    override fun periodic() {
        if (started) motor.power = RobotGlobals.keymap.pinion.y.toDouble()
    }
}