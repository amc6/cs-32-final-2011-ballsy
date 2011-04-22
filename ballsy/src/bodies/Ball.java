package bodies;

import static bodies.BodyConstants.DEFAULT_BALL_RADIUS;

import org.dom4j.Element;

public class Ball extends AbstractBody {
		
	public Ball(float centerX, float centerY, float radius){
		
		this.setPhysicsAndGraphics(new physics.PhysicsBall(centerX, centerY, radius),
				new graphics.GraphicsBall());
		
		
	}

	@Override
	public Element writeXML() {
		return super.writeXML("ball");
	}

}
