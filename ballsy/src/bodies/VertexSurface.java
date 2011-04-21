package bodies;

import static bodies.BodyConstants.DEFAULT_BODY_BOUNCINESS;
import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import static bodies.BodyConstants.DEFAULT_BODY_DENSITY;
import static bodies.BodyConstants.DEFAULT_BODY_FRICTION;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import ballsy.AbstractLevel;

public class VertexSurface extends AbstractBody {
		
	public VertexSurface(AbstractLevel level, PhysicsWorld world, ArrayList<Vec2> worldPoints){
		super(level, world, new physics.PhysicsVertexSurfaceDef(world, 0, 0, worldPoints, DEFAULT_BODY_DENSITY, DEFAULT_BODY_FRICTION, DEFAULT_BODY_BOUNCINESS), new graphical.VertexSurfaceDef(DEFAULT_BODY_COLOR));
	}
}
