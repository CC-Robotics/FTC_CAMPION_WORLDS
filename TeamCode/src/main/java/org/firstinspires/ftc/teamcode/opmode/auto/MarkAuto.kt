package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import org.firstinspires.ftc.teamcode.MecanumDrive
import org.firstinspires.ftc.teamcode.subsystem.Arm

@Autonomous(name = "Mark Auto")
class MarkAuto : NextFTCOpMode() {
    override val components = Components()
        .useSubsystems(Arm)
        .useBulkReading()

    val initialPose = Pose2d(Vector2d(0.0, 0.0), 0.0)
    lateinit var drive: MecanumDrive
    lateinit var trajectoryAction: Action

    private fun buildTrajectory(): TrajectoryActionBuilder {
        return drive.actionBuilder(initialPose)
            .strafeTo(Vector2d(2.0, 0.0))
            .strafeTo(Vector2d(2.0, -20.0))
            .strafeTo(Vector2d(20.0, -20.0))
    }

    override fun onInit() {
        drive = MecanumDrive(hardwareMap, initialPose)
        trajectoryAction = buildTrajectory().build()
    }

    override fun onStartButtonPressed() {
        runBlocking(trajectoryAction)
    }
}