package physics;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.EdgeChainDef;
import org.jbox2d.common.Vec2;

public class PhysicsVertexSurfaceDef extends PhysicsDef {
	
	protected EdgeChainDef _edgeChain;
	protected ArrayList<Vec2> _points;
	
	public PhysicsVertexSurfaceDef(PhysicsWorld world, float x, float y, ArrayList<Vec2> worldPoints, float d, float f, float b) {
		super(world, false);
		
		_edgeChain = new EdgeChainDef();
		_points = new ArrayList<Vec2>();
		
		for (int i = 0; i < worldPoints.size(); i++){
			Vec2 vec = worldPoints.get(i);
			_points.add(vec);
			_edgeChain.addVertex(vec);
		}
		
		// Need to also do from other side to avoid bug of falling through the surface when
		// approaching from one side
		for (int i = worldPoints.size() - 1 ; i >= 0; i--){
			Vec2 vec = worldPoints.get(i);
			vec.x -= 0.5f;
			vec.y -= 0.5f;
			_points.add(vec);
			_edgeChain.addVertex(vec);
		}

		_edgeChain.setIsLoop(false);
		this.createBody(_edgeChain, d, f, b, x, y);
	
	}

	public List<Vec2> getWorldPoints(){
		return _points;
	}
	
}
