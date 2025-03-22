package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoController
import com.rowanmcalpin.nextftc.ftc.OpModeData

class DualServo(val leader: Servo, val follower: Servo) {
    constructor(leaderName: String, followerName: String): this (
        OpModeData.hardwareMap.get(Servo::class.java, leaderName),
        OpModeData.hardwareMap.get(Servo::class.java, followerName)
    )

    var position: Double
        get() = leader.position
        set(value) { leader.position = value }

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