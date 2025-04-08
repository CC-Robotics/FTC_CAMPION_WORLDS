package org.firstinspires.ftc.teamcode.opmode.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.subsystem.Lift
import org.firstinspires.ftc.teamcode.subsystem.Lifts

@TeleOp(name = "Lift Control", group = "Testing")
class LiftControl : NextFTCOpMode() {
    override val components = Components()
        .useSubsystems(Lift)
        .useGamepads()
        .useBulkReading()

    override fun onStartButtonPressed() {
        Lift.attach(DefaultKeymap())
    }

    override fun onUpdate() {
        telemetry.addData("Target Position", Lift.controlSystem.goal.position)
        telemetry.addData("Current Position", Lift.motor.currentPosition)
        telemetry.update()
    }
}
