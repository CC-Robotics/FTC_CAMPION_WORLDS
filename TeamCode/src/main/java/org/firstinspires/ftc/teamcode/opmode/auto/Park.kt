package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
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


@Autonomous(name = "Park", group = "Regular Series")
class Park : NextFTCOpMode() {
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
        actions.strafeTo(Vector2d(61.38, -62.69))()
    }
}