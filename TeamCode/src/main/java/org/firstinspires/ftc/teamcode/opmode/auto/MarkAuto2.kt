package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import org.firstinspires.ftc.teamcode.MecanumDrive
import org.firstinspires.ftc.teamcode.command.ActionCommand
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Effector
import org.firstinspires.ftc.teamcode.subsystem.Lift
import org.firstinspires.ftc.teamcode.subsystem.Pinion
import org.firstinspires.ftc.teamcode.util.ActionCommandFactory


@Autonomous(name = "Auto MkII", group = "Mark Series")
class MarkAuto2 : NextFTCOpMode() {
    override val components = Components()
        .useSubsystems(Drivetrain, Effector, Lift, Pinion)
        .useBulkReading()

    private val initialPose = Pose2d(11.68, -67.94, Math.toRadians(90.00))
    lateinit var drive: MecanumDrive
    private lateinit var actions: ActionCommandFactory

    override fun onInit() {
        drive = MecanumDrive(hardwareMap, initialPose)
        actions = ActionCommandFactory(drive)
    }

    override fun onStartButtonPressed() {
//        actionCommandFactory.createTrajectory {
//            splineTo(Vector2d(60.02, -61.64), Math.toRadians(0.24))
//        }()
        SequentialGroup(
            ActionCommand(drive.actionBuilder(drive.localizer.pose)
                .strafeTo(Vector2d(48.0, -64.0))
                .strafeTo(Vector2d(48.0, -8.0)) // Go around
                .turnTo(Math.toRadians(0.0))
                .strafeTo(Vector2d(56.0, -9.0)) // Align in front of specimen
                .strafeTo(Vector2d(56.0, -62.69)) // Push it down
                .strafeTo(Vector2d(53.0, -8.0)) // Go back up
                .strafeTo(Vector2d(62.0, -8.0)) // Move to next one
                .strafeTo(Vector2d(62.0, -62.69)) // Push it down
                .strafeTo(Vector2d(59.52, -8.0)) // Go back up
                .strafeTo(Vector2d(72.0, -8.0)) // Move to next one
                .strafeTo(Vector2d(72.0, -62.69)) // Push it down
                .build()),
            // Lift.to(Lift.Position.TOP_RUNG.ticks + 200)
        )()
    }
}