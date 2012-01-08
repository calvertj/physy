package jctd.physics.fdl;

import java.util.*;

import jctd.physics.EmptyIterator;

public class Graph implements Iterable<Graph.Edge> {

	private Map<String,Set<String>> edges;
	
	public Graph() { 
		edges = new TreeMap<String,Set<String>>();
	}
	
	public void addEdge(String e1, String e2) { 
		if(!edges.containsKey(e1)) { edges.put(e1, new TreeSet<String>()); }
		if(!edges.containsKey(e2)) { edges.put(e2, new TreeSet<String>()); }
		edges.get(e1).add(e2);
		edges.get(e2).add(e1);
	}
	
	public boolean containsEdge(String e1, String e2) { 
		return edges.containsKey(e1) && edges.get(e1).contains(e2);
	}
	
	public int numEdges(String n) { 
		return edges.containsKey(n) ? edges.get(n).size() : 0;
	}
	
	public static class Edge { 
		
		public String e1, e2;
		
		public Edge(String e1, String e2) {
			this.e1 = e1; this.e2 = e2; 
		}
		
		public String toString() { 
			return String.format("%s -- %s", e1, e2);
		}
		
		public int hashCode() { 
			int code = 17;
			code += e1.hashCode(); code *= 37;
			code += e2.hashCode(); code *= 37;
			return code;
		}
		
		public boolean equals(Object o) { 
			if(!(o instanceof Edge)) return false; 
			Edge e = (Edge)o;
			return e.e1.equals(e1) && e.e2.equals(e2);
		}
	}

	public Iterator<Edge> iterator() {
		return new EdgeIterator();
	}
	
	public Iterable<Edge> edges(final String e1) { 
		return new Iterable<Edge>() {
			public Iterator<Edge> iterator() {
				return new StringToEdgeIterator(e1,
						edges.containsKey(e1) 
						? edges.get(e1).iterator() 
						: new EmptyIterator<String>());
			} 
		};
	}
	
	public Iterable<String> neighbors(final String e1) { 
		final Iterator<Edge> edges = edges(e1).iterator();
		return new Iterable<String>() {
			public Iterator<String> iterator() { 
				return new Iterator<String>() { 
					public boolean hasNext() { return edges.hasNext(); }
					public String next() { return edges.next().e2; }
					public void remove() { throw new UnsupportedOperationException(); }
				}; 
			}
		};
	}
	
	private class StringToEdgeIterator implements Iterator<Edge> {
		private String e1; 
		private Iterator<String> e2s;
		
		public StringToEdgeIterator(String e1, Iterator<String> e2s) { 
			this.e1 = e1; 
			this.e2s = e2s;
		}
		
		public boolean hasNext() {
			return e2s.hasNext();
		}

		public Edge next() {
			return new Edge(e1, e2s.next());
		}

		public void remove() {
			throw new UnsupportedOperationException();
		} 
	}
	
	private class EdgeIterator implements Iterator<Edge> {
		
		private Iterator<String> e1, e2;
		
		private String currentStart;
		private Edge nextEdge;
		
		public EdgeIterator() { 
			e1 = edges.keySet().iterator();
			e2 = null;
			nextEdge = null;
			currentStart = null;
			findNextEdge();
		}
		
		private void findNextEdge() {
			nextEdge = null;
			String[] p = findNextPair();
			while(p != null && p[0].compareTo(p[1]) > 0) { 
				p = findNextPair();
			}
			nextEdge = p == null ? null : new Edge(p[0], p[1]);
		}
		
		private String[] findNextPair() {

			while(e2==null || !e2.hasNext()) { 
				e2 = null;
				if(e1 != null && e1.hasNext()) { 
					currentStart = e1.next();
					e2 = edges.get(currentStart).iterator();
				} else { 
					break;
				}
			}

			if(e2 == null) {  // we're completely done. 
				return null;
			}
			
			return new String[] { currentStart, e2.next() };
		}

		public boolean hasNext() {
			return nextEdge != null;
		}

		public Edge next() {
			Edge e = nextEdge;
			findNextEdge();
			return e;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		} 
	}
}
