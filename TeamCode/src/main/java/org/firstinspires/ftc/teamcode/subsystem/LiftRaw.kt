package org.firstinspires.ftc.teamcode.subsystem

import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.util.RobotGlobals
import org.firstinspires.ftc.teamcode.util.RobotUtil

/**
 * Controls the lift (linear slide) system of the robot running vertically in which the rack
 * and pinion is mounted on. It uses a PIDF controller to ensure smooth and accurate motion.
 * A static base feedforward is used but variation is implemented to mitigate additional
 * torque from the extension of the somewhat heavy rack.
 */
object LiftRaw : SubsystemEx() {
    lateinit var motors: MotorGroup

    var started = false

    override fun initialize() {
        motors = RobotUtil.motorGroupFromNames("lift_right", "lift_left")
        motors.leader.reverse()
        motors.leader.resetEncoder()
        started = false
    }

    override fun attach(keymap: Keymap) {
        started = true
    }

    override fun periodic() {
        if (started) motors.power = RobotGlobals.keymap.lift.y.toDouble()
    }
}