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

package org.firstinspires.ftc.teamcode.archive.command

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState

/**
 * This implements a [Controller] to drive a [Controllable] to a specified target position. When it
 * finishes, it will set the [Controllable]'s power to 0. To have it hold position, set the default
 * command to a [HoldPosition] command.
 */
class RunToPosition @JvmOverloads constructor(val controllable: Controllable, val target: Double, val controlSystem: ControlSystem,
                                              override val subsystems: Set<Subsystem> = setOf()): Command() {

    constructor(controllable: Controllable, target: Double, controlSystem: ControlSystem, subsystem: Subsystem): this(controllable, target, controlSystem, setOf(subsystem))

    override val isDone: Boolean
        get() = controlSystem.isWithinTolerance(10.0)

    override fun start() {
        controlSystem.goal = KineticState(target)
    }

    override fun update() {
        controllable.power = controlSystem.calculate(KineticState(controllable.currentPosition, controllable.velocity))
    }

    override fun stop(interrupted: Boolean) {
        controllable.power = 0.0
    }
}