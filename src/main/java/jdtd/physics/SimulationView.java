package jdtd.physics;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class SimulationView extends JPanel {
	
	private Simulation sim;
	
	private Area baseArea;
	private double scale;
	private boolean showDynamics;

	public SimulationView(Simulation s) { 
		sim = s;
		baseArea = new SquareArea(sim.boundingBox());
		scale = 1.0;
		showDynamics = true;
	}
	
	private void calculateScale() { 
		double w = baseArea.width();
		int pw = Math.max(getWidth(), getHeight());
		scale = (double)(pw) / w;
	}
	
	public void setView(Area a) {
		double w = Math.max(a.width(), a.height());
		baseArea = new Area(a.x1(), a.y1(), a.x1()+w, a.y1()+w);
		calculateScale();
	}
	
	protected void paintComponent(Graphics gg) { 
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D)gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int w = getWidth(), h = getHeight();
		if(w <= 0 || h <= 0) { return; }
		
		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);

		calculateScale();

		synchronized(sim) { 
			for(DynamicBody body : sim.bodies()) { 
				drawBody(g, body);
			}
		}
	}
	
	private int x(double x) { 
		return (int)Math.round(scale * (x - baseArea.x1()));
	}
	
	private int y(double y) { 
		return (int)Math.round(scale * (y - baseArea.y1()));
	}
	
	private static Color bodyColor = new Color(255, 0, 0, 125);
	
	private void drawBody(Graphics2D g, DynamicBody b) { 
		Location loc = b.location();
		
		int r = b.radius();
		int diam = r * 2;
		
		int px = x(loc.x());
		int py = y(loc.y());

		g.setColor(bodyColor);
		g.fillOval(px-r, py-r, diam, diam);

		if(showDynamics) { 
			DynamicHistory dyn = sim.getDynamicHistory(b);
			Vector v = b.velocity();
			
			int vx = x(loc.x() + v.x());
			int vy = y(loc.y() + v.y());

			g.setColor(Color.black);
			g.drawLine(px, py, vx, vy);

			int ppx = px, ppy = py;
			g.setColor(Color.blue);
			for(Location ploc : dyn.pastLocations) {
				px = x(ploc.x());
				py = y(ploc.y());
				g.drawLine(ppx, ppy, px, py);
				ppx=px; ppy=py;
			}
		}
	}
}

