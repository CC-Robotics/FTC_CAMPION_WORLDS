package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import org.firstinspires.ftc.teamcode.MecanumDrive
import org.firstinspires.ftc.teamcode.command.ActionCommand

class ActionCommandFactory(private val drive: MecanumDrive) {
    fun create(build: TrajectoryActionBuilder.() -> Unit): ActionCommand {
        val builder = drive.actionBuilder(drive.localizer.pose)
        return ActionCommand(builder.apply(build).build())
    }
}
