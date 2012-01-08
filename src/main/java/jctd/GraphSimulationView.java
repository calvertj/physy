package jctd;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import jctd.physics.Area;
import jctd.physics.DynamicBody;
import jctd.physics.DynamicHistory;
import jctd.physics.Location;
import jctd.physics.Simulation;
import jctd.physics.SquareArea;
import jctd.physics.Vector;

public class GraphSimulationView extends JPanel {
	
	private Simulation sim;
	
	private Graph graph;
	
	private Area baseArea;
	private double scale;
	private boolean showDynamics;

	public GraphSimulationView(Simulation s, Graph g) { 
		sim = s;
		graph = g;
		baseArea = new SquareArea(sim.boundingBox());
		scale = 1.0;
		showDynamics = false;
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
			drawEdges(g);
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

	private void drawEdges(Graphics2D g) { 
		for(DynamicBody b1 : sim.bodies()) { 
			Location b1loc = b1.location();
			int b1x = x(b1loc.x()), b1y = y(b1loc.y());

			for(Graph.Edge edge : graph.edges(b1.name())) {
				if(edge.e1.compareTo(edge.e2) < 0) {  
					//System.out.println(String.format("%s", edge.toString()));
					DynamicBody b2 = sim.getBody(edge.e2);

					Location b2loc = b2.location();
					int b2x = x(b2loc.x()), b2y = y(b2loc.y());

					g.setColor(Color.gray);
					g.drawLine(b1x, b1y, b2x, b2y);
				}
			}
		}
	}
	
	private void drawEdges(Graphics2D g, DynamicBody b1) {
		Location b1loc = b1.location();
		int b1x = x(b1loc.x()), b1y = y(b1loc.y());
		
		for(Graph.Edge edge : graph.edges(b1.name())) {
			DynamicBody b2 = sim.getBody(edge.e2);
			Location b2loc = b2.location();
			int b2x = x(b2loc.x()), b2y = y(b2loc.y());
			
			g.setColor(Color.gray);
			g.drawLine(b1x, b1y, b2x, b2y);
		}
	}
}

