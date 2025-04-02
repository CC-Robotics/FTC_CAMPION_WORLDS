package org.firstinspires.ftc.teamcode.opmode.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.subsystem.Arm

@TeleOp(name = "ArmControl", group = "Test Modes")
class ArmControl : NextFTCOpMode() {
    override val components = Components()
        .useSubsystems(Arm)
        .useGamepads()

    override fun onStartButtonPressed() {
        class ArmKeymap : DefaultKeymap() {
            override val arm = GamepadManager.gamepad2.leftStick
        }
        Arm.attach(ArmKeymap())
    }

    override fun onUpdate() {
        telemetry.addData("Target Position", Arm.targetPosition)
        telemetry.addData("Current Position", Arm.armMotors.currentPosition)
        telemetry.update()
    }
}
