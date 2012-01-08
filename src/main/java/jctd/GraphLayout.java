package jctd;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import jctd.physics.Area;
import jctd.physics.DynamicBody;
import jctd.physics.Location;
import jctd.physics.Simulation;
import jctd.physics.Vector;
import jctd.physics.forces.AddingFieldGenerator;
import jctd.physics.forces.BarnesHutFieldGenerator;
import jctd.physics.forces.ForceFieldGenerator;
import jctd.physics.forces.Gravity;
import jctd.physics.forces.HookeForce;
import jctd.physics.forces.PairwiseForce;
import jctd.physics.forces.StaticFieldGenerator;

public class GraphLayout extends JFrame {
	
	public static void main(String[] args) { 
		new GraphLayout(10, 20);
	}
	
	private Simulation sim;
	private GraphSimulationView view;

	public GraphLayout(int b, int numEdges) { 
		super("GraphLayout Simulation");
		
		ArrayList<DynamicBody> bodies = new ArrayList<DynamicBody>();
		Area a = new Area(-10.0, -10.0, 10.0, 10.0);
		Random rand = new Random();
		
		double randScale = 10.0, randCenter = -randScale/2.0;	
		
		for(int i = 0; i < b; i++) {
			//Location loc = a.randomLocation();
			double x = rand.nextGaussian(), y = rand.nextGaussian();
			
			Location loc = new Location(x, y);
			
			//double mass = rand.nextDouble() * 10.0 + 1.0;
			double mass = 1.0;
			
			Vector v = new Vector();
			//v = new Vector(rand.nextDouble(), rand.nextDouble());
			//v = v.scale(randScale).addTo(new Vector(randCenter, randCenter));

			bodies.add(new DynamicBody("b" + i, mass, loc, v));
		}
		
		Area bb = Simulation.boundingBox(bodies);
		
		Graph graph = GraphUtils.randomGraph(bodies, numEdges);
		GraphUtils.connectGraph(graph, bodies);
		
		PairwiseForce springs = new HookeForce(100.0, 1.0);
		
		ForceFieldGenerator ffg = new AddingFieldGenerator(
				new BarnesHutFieldGenerator(new Gravity(-100.0))
				,new StaticFieldGenerator(new GraphEdgeForceField(graph, springs, bodies))
		);
		
		sim = new Simulation(ffg, bodies);
		
		view = new GraphSimulationView(sim, graph);
		view.setView(bb.expand(10, 10));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = (Container)getContentPane();
		c.setLayout(new BorderLayout());
		c.add(view, BorderLayout.CENTER);
		
		view.setPreferredSize(new Dimension(400, 400));
		
		view.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				startSimulation();
			}

			public void mouseReleased(MouseEvent arg0) {
				stopSimulation();
			} 
			
		});
		
		setVisible(true);
		pack();
	}
	
	private boolean simulationRunning = false;
	
	public void startSimulation() { 
		simulationRunning = true;
		Runnable r = new Runnable() { 
			public void run() { 
				while(simulationRunning) { 
					step();
					
					try {
						Thread.sleep(1L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		Thread t = new Thread(r);
		t.start();
	}
	
	public void stopSimulation() { 
		simulationRunning = false;
	}
	
	public void step() { 
		sim.step();
		view.setView(sim.boundingBox());
		view.repaint();
	}
	
	public Action stepAction() { 
		return new AbstractAction("Step") { 
			public void actionPerformed(ActionEvent e) {
				step();
			}
		};
	}
}
