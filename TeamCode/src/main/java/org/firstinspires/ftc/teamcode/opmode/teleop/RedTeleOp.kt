import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.OpModeData
import org.firstinspires.ftc.teamcode.opmode.BaseTeleOp

@TeleOp(name = "Red | Tele - Ri/Xa | Main", group = "2024-25")
class RedTeleOp : BaseTeleOp() {
    override fun onInit() {
        OpModeData.alliance = OpModeData.Alliance.RED
        super.onInit()
    }
}