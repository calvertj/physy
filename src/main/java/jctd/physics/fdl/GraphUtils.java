package jctd.physics.fdl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import jctd.physics.Body;

public class GraphUtils {
	
	private static Random rand = new Random();
	
	public static Set<String> connectedComponent(Graph g, String n) { 
		TreeSet<String> nodes = new TreeSet<String>();
		LinkedList<String> pending = new LinkedList<String>();
		
		pending.addLast(n);
		while(!pending.isEmpty()) { 
			String e1 = pending.removeFirst();
			nodes.add(e1);
			for(String e2 : g.neighbors(e1)) {
				if(!nodes.contains(e2)) { 
					pending.addLast(e2);
				}
			}
		}
		
		return nodes;
	}
	
	public static ArrayList<Set<String>> connectedComponents(Graph g, Iterable<String> nodes) { 
		ArrayList<Set<String>> ccs = new ArrayList<Set<String>>();
		Set<String> total = new TreeSet<String>();
		for(String n : nodes) { 
			if(!total.contains(n)) { 
				Set<String> cc = connectedComponent(g, n);
				total.addAll(cc);
				ccs.add(cc);
			}
		}
		return ccs;
	}
	
	public static <T extends Body> void connectGraph(Graph g, Iterable<T> bodies) {
		
		Iterable<String> names = new Mapped<T,String>(bodies, new MethodCallable<T,String>("name"));
		ArrayList<Set<String>> ccs = connectedComponents(g, names);
		
		for(int i = 0; i < ccs.size()-1; i++) { 
			Set<String> cc1 = ccs.get(i), cc2 = ccs.get(i+1);
			String n1 = cc1.iterator().next(), n2 = cc2.iterator().next();
			g.addEdge(n1, n2);
		}
	}

	public static Graph randomGraph(Iterable<? extends Body> bodies, int edges) { 
		Graph g = new Graph();
		ArrayList<Body> bodylist = new ArrayList<Body>();
		for(Body b : bodies) { bodylist.add(b); }
		int bcount = bodylist.size();
		
		for(int i = 0; i < edges; i++) {
			String e1, e2;
			do { 
				e1 = bodylist.get(rand.nextInt(bcount)).name();
				e2 = bodylist.get(rand.nextInt(bcount)).name();
			} while(e1.equals(e2) || g.containsEdge(e1, e2));
			
			g.addEdge(e1, e2);
			System.out.println(String.format("%s -> %s", e1, e2));
		}
		return g;
	}
}

interface Callable<X,Y> { 
	public Y call(X value);
}

class FieldCallable<X,Y> implements Callable<X,Y> {
	
	private String fieldName;
	public FieldCallable(String fieldName) { 
		this.fieldName = fieldName;
	}

	public Y call(X value) {
		Class cls = value.getClass();
		try {
			Field f = cls.getField(fieldName);
			return (Y)f.get(value);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	} 
	
}

class MethodCallable<X,Y> implements Callable<X,Y> {
	
	private String fieldName;
	public MethodCallable(String fieldName) { 
		this.fieldName = fieldName;
	}

	public Y call(X value) {
		Class cls = value.getClass();
		try {
			Method m = cls.getMethod(fieldName);
			return (Y)m.invoke(value);
			
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	} 
	
}

class Mapped<X,Y> implements Iterable<Y> {
	
	private Callable<X,Y> caller;
	private Iterable<X> base;
	
	public Mapped(Iterable<X> b, Callable<X,Y> c) { 
		base = b;
		caller = c;
	}

	public Iterator<Y> iterator() {
		return new CallingIterator();
	} 
	
	private class CallingIterator implements Iterator<Y> { 
		
		private Iterator<X> itr;
		public CallingIterator() { itr = base.iterator(); }
		
		public boolean hasNext() { return itr.hasNext(); }
		public Y next() { return caller.call(itr.next()); }
		public void remove() { itr.remove(); }
		
	}
}