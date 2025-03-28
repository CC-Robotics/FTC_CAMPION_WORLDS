package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoController
import com.rowanmcalpin.nextftc.ftc.OpModeData

/**
 * Allows you to manipulate two servos as one servo which may
 * prove useful for systems on the bot which use 2 servos to carry
 * a stronger load but still need servo-style positioning.
 * */
class DualServo(val leader: Servo, val follower: Servo) {
    constructor(leaderName: String, followerName: String): this (
        OpModeData.hardwareMap.get(Servo::class.java, leaderName),
        OpModeData.hardwareMap.get(Servo::class.java, followerName)
    )

    /**
     * Gets the position of the group of servos (uses leader position)
     * */
    var position: Double
        get() = leader.position
        set(value) { leader.position = value }

    /**
     * Allows you to enable and disable PWM as well as get
     * if it is enabled or not. Status is based on leader's.
     * */
    var pwmEnabled: Boolean
        get() = leader.controller.pwmStatus == ServoController.PwmStatus.ENABLED
        set(value) {
            if (value) {
                leader.controller.pwmEnable()
                follower.controller.pwmEnable()
            } else {
                leader.controller.pwmDisable()
                follower.controller.pwmDisable()
            }
        }
}