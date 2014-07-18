package pap1213.assignment.nbody;
import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Body implements Callable<BodyInfo> {

	private static final double G = 6.674*Math.pow(10,-11);
	private static final double dt = 20*0.001;
	//private double EPS = Math.pow(10, 8);      // softening parameter (just to avoid infinities)

	private ArrayList<Body> bodies;
	public BodyInfo me;
	private BodyInfo future_me;
	
	public Body(int p, ArrayList<Body> bodies, P2d pos, V2d vel, double mass, Color color) {	
		me = new BodyInfo(pos, vel, 0, 0,mass,p,color);
		future_me = new BodyInfo(pos, vel, 0, 0,mass,p);
		this.bodies = bodies;
	}

	public void update(BodyInfo new_me) {
		
		me.fx = new_me.fx;
		me.fy = new_me.fy;
		me.position.x = new_me.position.x;
		me.position.y = new_me.position.y;
		me.velocity.x = new_me.velocity.x;
		me.velocity.y = new_me.velocity.y;
	  }
	
    public P2d getPos(){
    	return me.position;
    }
    
	public BodyInfo call() throws Exception {

		double future_fx = 0;
		double future_fy = 0;

		for (int j = 0; j< bodies.size(); j++)
    	{	
    		if (bodies.get(j).me.index != me.index)
    		{
    			double dx = (bodies.get(j).me.position.x-this.me.position.x);
    			double dy = (bodies.get(j).me.position.y-this.me.position.y);
    			double dist = Math.sqrt(dx*dx + dy*dy);
    			double f = (G*bodies.get(j).me.mass * this.me.mass)/(dist*dist);

    			future_fx = future_fx + ((f * dx)/dist);
    			future_fy = future_fy + ((f * dy)/dist);
    		}
    	}
		
		future_me.fx = future_fx;
		future_me.fy = future_fy;
		future_me.index = me.index;
		future_me.mass = me.mass;
		future_me.velocity.x = me.velocity.x + (dt * future_fx) / me.mass;
		future_me.velocity.y = me.velocity.y + (dt * future_fy) / me.mass;
		future_me.position.x = me.position.x + (dt * future_me.velocity.x);
		future_me.position.y = me.position.y + (dt * future_me.velocity.y);
		
		return future_me;
	}
}
