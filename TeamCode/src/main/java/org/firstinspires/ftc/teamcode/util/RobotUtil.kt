package org.firstinspires.ftc.teamcode.util

import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup

object RobotUtil {
    /**
     * Create a MotorGroup from motor names of a leader and followers. This method is used instead
     * of the constructor override as the logic contains a known fatal bug.
     */
    fun motorGroupFromNames(leader: String, vararg followers: String): MotorGroup {
        return MotorGroup(MotorEx(leader), * Array(followers.size) { i -> MotorEx(followers[i]) })
    }
}