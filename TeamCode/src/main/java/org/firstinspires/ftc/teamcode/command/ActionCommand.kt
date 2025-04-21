/*
NextFTC: a user-friendly control library for FIRST Tech Challenge
    Copyright (C) 2025 Rowan McAlpin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.firstinspires.ftc.teamcode.command

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.util.RobotUtil

/**
 * This implements a [Controller] to drive a [Controllable] to a specified target position. When it
 * finishes, it will set the [Controllable]'s power to 0. To have it hold position, set the default
 * command to a [ActionCommand] command.
 */
class ActionCommand(private val action: Action, override val subsystems: Set<Subsystem> = setOf()): Command() {
    override var isDone = false

    override fun update() {
        val packet = TelemetryPacket()
        action.preview(packet.fieldOverlay())
        isDone = !action.run(packet)
        RobotUtil.telemetry.addLine(isDone.toString())
        action.run {  }
        FtcDashboard.getInstance().sendTelemetryPacket(packet)
    }

    override fun stop(interrupted: Boolean) {
        Drivetrain.brake()
    }
}