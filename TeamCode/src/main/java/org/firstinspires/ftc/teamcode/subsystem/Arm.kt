package org.firstinspires.ftc.teamcode.subsystem

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.GainScheduledArmFeedforward
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import kotlin.math.cos

object Arm : Subsystem() {
    private lateinit var arm: MotorGroup

    private val controller = PIDFController(0.008, 0.002, 0.0002, GainScheduledArmFeedforward({
        1.0 // Replace with Lift.motor.currentPosition calculation
    }, { input ->

        val thetaDegrees = 80 + (80 * input / 1010) // Convert encoder input to degrees
        val thetaRadians = Math.toRadians(thetaDegrees)    // Convert to radians
        (Math.PI / 2) - thetaRadians           // Adjust to arm feedforward logic
    }))


    override val defaultCommand: Command
        get() = HoldPosition(arm, controller, this)

    override fun initialize() {
        arm = MotorGroup("right", "left")
        arm.leader.reverse()

        arm.leader.resetEncoder()
    }
}