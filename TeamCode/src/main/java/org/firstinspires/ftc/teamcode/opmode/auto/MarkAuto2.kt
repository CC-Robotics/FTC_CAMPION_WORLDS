package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import org.firstinspires.ftc.teamcode.MecanumDrive
import org.firstinspires.ftc.teamcode.archive.subsystem.Arm
import org.firstinspires.ftc.teamcode.util.ActionCommandFactory

@Autonomous(name = "Auto MkII", group = "Mark Series")
class MarkAuto2 : NextFTCOpMode() {
    override val components = Components()
        .useSubsystems(Arm)
        .useBulkReading()

    private val initialPose = Pose2d(Vector2d(0.0, 0.0), 0.0)
    lateinit var drive: MecanumDrive
    private lateinit var actionCommandFactory: ActionCommandFactory

    override fun onInit() {
        drive = MecanumDrive(hardwareMap, initialPose)
        actionCommandFactory = ActionCommandFactory(drive)
    }

    override fun onStartButtonPressed() {
        actionCommandFactory.create {
            strafeTo(Vector2d(2.0, 0.0))
            strafeTo(Vector2d(2.0, -20.0))
            strafeTo(Vector2d(20.0, -20.0))
        }()
    }
}