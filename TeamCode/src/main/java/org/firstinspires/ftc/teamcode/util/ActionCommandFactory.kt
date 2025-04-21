package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.MecanumDrive
import org.firstinspires.ftc.teamcode.command.ActionCommand

class ActionCommandFactory(private val drive: MecanumDrive) {
    fun createTrajectory(build: TrajectoryActionBuilder.() -> Unit): ActionCommand {
        val builder = drive.actionBuilder(drive.localizer.pose)
        return ActionCommand(builder.apply(build).build())
    }

    fun splineTo(pos: Vector2d, heading: Double): ActionCommand {
        return ActionCommand(drive.actionBuilder(drive.localizer.pose).splineTo(pos, heading).build())
    }

    fun strafeToLinearHeading(pos: Vector2d, heading: Double): ActionCommand {
        return ActionCommand(drive.actionBuilder(drive.localizer.pose).strafeToLinearHeading(pos, heading).build())
    }

    fun turn(heading: Double): ActionCommand {
        return ActionCommand(drive.actionBuilder(drive.localizer.pose).turn(heading).build())
    }

    fun turnTo(heading: Double): ActionCommand {
        return ActionCommand(drive.actionBuilder(drive.localizer.pose).turnTo(heading).build())
    }

    fun strafeTo(pos: Vector2d): ActionCommand {
        return ActionCommand(drive.actionBuilder(drive.localizer.pose).strafeTo(pos).build())
    }

    fun strafe(pos: Vector2d): ActionCommand {
        val pose = drive.localizer.pose
        val newPos = Vector2d(
            pose.position.x + pos.x,
            pose.position.y + pos.y
        )
        return ActionCommand(drive.actionBuilder(drive.localizer.pose).strafeTo(newPos).build())
    }

    fun strafeLinearHeading(pos: Vector2d, heading: Double): ActionCommand {
        val pose = drive.localizer.pose
        val newPos = Vector2d(
            pose.position.x + pos.x,
            pose.position.y + pos.y
        )
        val newHeading = pose.heading + heading
        return ActionCommand(drive.actionBuilder(drive.localizer.pose).strafeToLinearHeading(newPos, heading).build())
    }
}
