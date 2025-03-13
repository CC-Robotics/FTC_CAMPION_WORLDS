import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.OpModeData
import org.firstinspires.ftc.teamcode.opmode.BaseTeleOp

@TeleOp(name = "Blue | Tele - Ri/Xa | Main", group = "2024-25")
class BlueTeleOp : BaseTeleOp() {
    override fun onInit() {
        OpModeData.alliance = OpModeData.Alliance.BLUE
        super.onInit()
    }
}