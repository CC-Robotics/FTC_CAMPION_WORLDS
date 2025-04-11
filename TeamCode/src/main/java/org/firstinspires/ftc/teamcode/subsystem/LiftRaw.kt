package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.clamp
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import dev.nextftc.nextcontrol.builder.controlSystem
import dev.nextftc.nextcontrol.feedback.PIDCoefficients
import dev.nextftc.nextcontrol.feedforward.GravityFeedforwardParameters
import org.firstinspires.ftc.teamcode.command.RunToPosition
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.util.RobotUtil

/**
 * Controls the lift (linear slide) system of the robot running vertically in which the rack
 * and pinion is mounted on. It uses a PIDF controller to ensure smooth and accurate motion.
 * A static base feedforward is used but variation is implemented to mitigate additional
 * torque from the extension of the somewhat heavy rack.
 */
object LiftRaw : SubsystemEx() {
    private lateinit var motors: MotorGroup

    override fun initialize() {
        motors = MotorGroup("lift_right", "lift_left")
        motors.leader.reverse()
        motors.leader.resetEncoder()
    }

    override fun attach(keymap: Keymap) {
        keymap.lift.heldCommand = { xy ->
            InstantCommand { motors.power = xy.second.toDouble() }
        }
    }
}