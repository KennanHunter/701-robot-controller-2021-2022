package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystem.Drive;
import org.firstinspires.ftc.teamcode.subsystem.Intake;
import org.firstinspires.ftc.teamcode.subsystem.OutTake;


@TeleOp(name = "Sub System Rewrite", group = "Linear Opmode")
public class SubsystemRewrite extends LinearOpMode {

	private ElapsedTime runtime = new ElapsedTime();

	@Override
	public void runOpMode() {
		Drive drive = new Drive(hardwareMap);
		Intake intake = new Intake(hardwareMap);
		OutTake outTake = new OutTake(hardwareMap);

		telemetry.addData("Status", "Initialized");
		telemetry.update();

		waitForStart();
		runtime.reset();

		while (opModeIsActive()) {
			double foward = gamepad1.left_stick_y;
			double turn = gamepad1.right_stick_x;
			double strafe = gamepad1.left_stick_x;

			drive.vectorDrive(strafe, foward, turn);

			// Outtake Box
			if (gamepad2.left_bumper) {
				outTake.Box.reset();
			} else if (gamepad2.right_bumper) {
				outTake.Box.dump();
			}

			// Outtake Winch
			if (Math.abs(gamepad2.left_stick_y) > 0.05) {
				outTake.Winch.setManualPower(-gamepad2.left_stick_y);
			} else {
				if (gamepad2.dpad_up) {
					outTake.Winch.up();
				} else if (gamepad2.dpad_down) {
					outTake.Winch.down();
				} else if (gamepad2.dpad_left || gamepad2.dpad_right) {
					outTake.Winch.middle();
				}
			}

			intake.setDropSpeed(gamepad2.right_stick_y);


			// Intake Speed
			intake.setIntakeSpeed(-gamepad2.left_trigger + gamepad2.right_trigger);

			// Show the elapsed game time
			telemetry.addData("Status", "Run Time: " + runtime.toString());
			telemetry.update();
		}
	}
}