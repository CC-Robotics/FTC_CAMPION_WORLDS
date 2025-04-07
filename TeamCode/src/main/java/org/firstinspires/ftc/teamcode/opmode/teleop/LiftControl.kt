package org.firstinspires.ftc.teamcode.opmode.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.subsystem.Lifts

@TeleOp(name = "ArmControl", group = "Test Modes")
class LiftControl : NextFTCOpMode() {
    override val components = Components()
        .useSubsystems(Lifts)
        .useGamepads()
        .useBulkReading()

    override fun onStartButtonPressed() {
        Lifts.attach(DefaultKeymap())
    }

    override fun onUpdate() {
        telemetry.addData("Target Position", Lifts.controlSystem.goal.position)
        telemetry.addData("Current Position", Lifts.motors.currentPosition)
        telemetry.update()
    }
}
