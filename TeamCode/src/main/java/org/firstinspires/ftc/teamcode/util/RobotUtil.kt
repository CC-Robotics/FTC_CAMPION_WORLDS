package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState
import org.firstinspires.ftc.teamcode.subsystem.Lift.targetPosition

object RobotUtil {
    /**
     * Create a MotorGroup from motor names of a leader and followers. This method is used instead
     * of the constructor override as the logic contains a known fatal bug.
     */
    fun motorGroupFromNames(leader: String, vararg followers: String): MotorGroup {
        return MotorGroup(MotorEx(leader), * Array(followers.size) { i -> MotorEx(followers[i]) })
    }

    fun handleControl(name: String, useControl: Boolean,  motor: MotorEx, controlSystem: ControlSystem, targetPosition: Double) {
        if (useControl) {
            motor.power =
                controlSystem.calculate(KineticState(motor.currentPosition, motor.velocity))
            if (targetPosition != controlSystem.goal.position) {

            }
        } else {
            motor.power = 0.0
        }

        telemetry.addData("[$name] Target Position", targetPosition)
        telemetry.addData("[$name] Current Position", motor.currentPosition)
        telemetry.addData("[$name] Power", motor.power)
    }

    @JvmField val telemetry = MultipleTelemetry(FtcDashboard.getInstance().telemetry, OpModeData.telemetry)
}