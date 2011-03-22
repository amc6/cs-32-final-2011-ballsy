package graphical;

import processing.core.*;

public class Spring2D {
	float vx, vy; // The x- and y-axis velocities
	float x, y; // The x- and y-coordinates
	float gravity;
	float mass;
	float radius = 20;
	float stiffness = 0.2f;
	float damping = 0.7f;
	PApplet _parent; 

	public Spring2D(float xpos, float ypos, float m, float g, PApplet parent) {
		x = xpos;
		y = ypos;
		mass = m;
		gravity = g;
		_parent = parent;
	}

	public void update(float targetX, float targetY) {
		float forceX = (targetX - x) * stiffness;
		float ax = forceX / mass;
		vx = damping * (vx + ax);
		x += vx;
		float forceY = (targetY - y) * stiffness;
		forceY += gravity;
		float ay = forceY / mass;
		vy = damping * (vy + ay);
		y += vy;
	}

	public void display(float nx, float ny) {
		//_parent.noCursor();;
		_parent.noStroke();
		_parent.ellipse(x, y + 100, radius*2, radius*2);
		_parent.stroke(255);
		_parent.line(x, y + 100, nx, ny);
	}

}


