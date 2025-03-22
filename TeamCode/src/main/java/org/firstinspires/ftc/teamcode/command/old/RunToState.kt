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

package org.firstinspires.ftc.teamcode.command.old

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState

/**
 * This implements a [ControlSystem] to hold a [Controllable] in its current position.
 *
 * @param controllable the [Controllable] to control
 * @param controlSystem the [ControlSystem] to implement
 */
class RunToState(private val controllable: Controllable, private val controlSystem: ControlSystem, private val state: KineticState) : Command() {
    override val isDone: Boolean
        get() = controlSystem.isWithinTolerance(10.0)

    private var oldVelocity = 0.0
    private var lastUpdateTime = System.nanoTime()

    private fun getAcceleration(): Double {
        val currentTime = System.nanoTime()
        val deltaTime = (currentTime - lastUpdateTime) / 1e9
        lastUpdateTime = currentTime

        return if (deltaTime > 0) (controllable.velocity - oldVelocity) / deltaTime else 0.0
    }

    override fun start() {
        controlSystem.goal = state
    }

    override fun update() {
        controllable.power = controlSystem.calculate(KineticState(controllable.currentPosition, controllable.velocity, getAcceleration()))
        oldVelocity = controllable.velocity
    }

    override fun stop(interrupted: Boolean) {
        controllable.power = 0.0
    }
}
