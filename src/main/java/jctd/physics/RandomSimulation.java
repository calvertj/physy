package jctd.physics;

import javax.swing.*;

import jctd.physics.forces.AddingFieldGenerator;
import jctd.physics.forces.BarnesHutFieldGenerator;
import jctd.physics.forces.BoxField;
import jctd.physics.forces.ForceFieldGenerator;
import jctd.physics.forces.Gravity;
import jctd.physics.forces.StaticFieldGenerator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class RandomSimulation extends JFrame {
	
	public static void main(String[] args) { 
		new RandomSimulation(250);
	}
	
	private Simulation sim;
	private SimulationView view;

	public RandomSimulation(int b) { 
		super("Random Simulation");
		
		ArrayList<DynamicBody> bodies = new ArrayList<DynamicBody>();
		Area a = new Area(0.0, 0.0, 1000.0, 1000.0);
		Random rand = new Random();
		
		double randScale = 10.0, randCenter = -randScale/2.0;	
		
		for(int i = 0; i < b; i++) {
			//Location loc = a.randomLocation();
			double x = rand.nextGaussian() * 1000.0 + 500.0;
			double y = rand.nextGaussian() * 1000.0 + 500.0;
			
			Location loc = new Location(x, y);
			
			double mass = rand.nextDouble() * 10.0 + 1.0;
			
			Vector v = new Vector();
			//v = new Vector(rand.nextDouble(), rand.nextDouble());
			//v = v.scale(randScale).addTo(new Vector(randCenter, randCenter));

			bodies.add(new DynamicBody("b" + i, mass, loc, v));
		}
		
		Area bb = Simulation.boundingBox(bodies);
		
		ForceFieldGenerator ffg = new AddingFieldGenerator(
				//new StaticFieldGenerator(new Floor(bb.y2(), 10.0, 5.0))
				//new BarnesHutFieldGenerator(new Gravity(-0.1))
				new BarnesHutFieldGenerator(new Gravity(1.0))
				
				//,new StaticFieldGenerator(new BrownianField(0.1))
				//,new StaticFieldGenerator(new BoxField(bb, 1.0))
				//,new StaticFieldGenerator(new FrictionField(0.1))
		);
		
		sim = new Simulation(ffg, bodies);
		
		view = new SimulationView(sim);
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
