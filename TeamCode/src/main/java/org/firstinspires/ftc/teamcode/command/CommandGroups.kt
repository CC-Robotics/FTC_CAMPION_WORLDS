package org.firstinspires.ftc.teamcode.command

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import org.firstinspires.ftc.teamcode.subsystem.Lift
import org.firstinspires.ftc.teamcode.subsystem.Pinion

object CommandGroups {
    val scoreSpecimen: Command
        get() = SequentialGroup(
            Lift.to(Lift.Position.TOP_RUNG.ticks + 200),
        )
}