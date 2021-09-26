package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;


@TeleOp(name = "TeleOpPrototype", group = "Prototypes")
public class TeleOpPrototypeNoRR extends LinearOpMode {
	// Timer
    private ElapsedTime runtime = new ElapsedTime();
    
	// Declare Motors
    private DcMotor lbDrive = null;
    private DcMotor rbDrive = null;
    private DcMotor lfDrive = null;
    private DcMotor rfDrive = null;
	
	// Declare Motor power values
	private	double lbDrivePower;
	private double rbDrivePower;
	private double lfDrivePower;
	private double rfDrivePower;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        lbDrive = hardwareMap.get(DcMotor.class, "leftRear");  // These hardware map names 
        rbDrive = hardwareMap.get(DcMotor.class, "rightRear"); // are compatable with RoadRunners 
        lfDrive = hardwareMap.get(DcMotor.class, "leftFront"); // Defaults
        rfDrive = hardwareMap.get(DcMotor.class, "rightFront");

		

        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        rbDrive.setDirection(DcMotor.Direction.REVERSE);
        rfDrive.setDirection(DcMotor.Direction.REVERSE);

		// This disigusting syntax just allows us to know the current position through road runner
		StandardTrackingWheelLocalizer myLocalizer = new StandardTrackingWheelLocalizer(hardwareMap);
        myLocalizer.setPoseEstimate(new Pose2d(10, 10, Math.toRadians(90)));

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
			myLocalizer.update();

            // Retrieve your pose
            Pose2d myPose = myLocalizer.getPoseEstimate();

            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double strafe = gamepad1.left_stick_x;

            lbDrivePower = (Range.clip((drive + turn) * strafe, -1.0, 1.0));
            rbDrivePower = (Range.clip((drive - turn) * strafe, -1.0, 1.0));
            lfDrivePower = (Range.clip((drive + turn) * -strafe, -1.0, 1.0));
            rfDrivePower = (Range.clip((drive - turn) * -strafe, -1.0, 1.0));

            lbDrive.setPower(lbDrivePower);
            rbDrive.setPower(rbDrivePower);
            lfDrive.setPower(lfDrivePower);
            rfDrive.setPower(rfDrivePower);


            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left back (%.2f), right back (%.2f), left front (%.2f), right front (%.2f)", lbDrivePower, rbDrivePower, lfDrivePower, rfDrivePower);

			telemetry.addData("x", myPose.getX());
            telemetry.addData("y", myPose.getY());
            telemetry.addData("heading", myPose.getHeading());

			telemetry.update();
        }
    }
}
