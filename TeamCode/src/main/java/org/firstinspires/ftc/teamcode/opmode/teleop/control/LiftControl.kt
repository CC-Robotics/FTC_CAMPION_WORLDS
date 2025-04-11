package org.firstinspires.ftc.teamcode.opmode.teleop.control

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.subsystem.Lift
import org.firstinspires.ftc.teamcode.subsystem.LiftRaw

@TeleOp(name = "Lift Control", group = "Testing")
class LiftControl : NextFTCOpMode() {
    override val components = Components()
        .useSubsystems(LiftRaw)
        .useGamepads()
        .useBulkReading()

    override fun onStartButtonPressed() {
        LiftRaw.attach(DefaultKeymap())
    }

    override fun onUpdate() {
        telemetry.update()
    }
}
