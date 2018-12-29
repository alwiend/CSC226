/*
* 	Alwien Dippenaar
* 	V00849850
* 	CSC 226 Assignment 2 Programming part
* 	October 25, 2018
* */

/* MST.java
   CSC 226 - Fall 2018
   Problem Set 2 - Template for Minimum Spanning Tree algorithm
   
   The assignment is to implement the mst() method below, using Kruskal's algorithm
   equipped with the Weighted Quick-Union version of Union-Find. The mst() method computes
   a minimum spanning tree of the provided graph and returns the total weight
   of the tree. To receive full marks, the implementation must run in time O(m log m)
   on a graph with n vertices and m edges.
   
   This template includes some testing code to help verify the implementation.
   Input graphs can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
       java MST
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. graphs.txt), run the program with
       java MST graphs.txt
   
   The input format for both methods is the same. Input consists
   of a series of graphs in the following format:
   
       <number of vertices>
       <adjacency matrix row 1>
       ...
       <adjacency matrix row n>
   	
   For example, a path on 3 vertices where one edge has weight 1 and the other
   edge has weight 2 would be represented by the following
   
   3
   0 1 0
   1 0 2
   0 2 0
   	
   An input file can contain an unlimited number of graphs; each will be processed separately.
   
   NOTE: For the purpose of marking, we consider the runtime (time complexity)
         of your implementation to be based only on the work done starting from
	 the mst() method. That is, do not not be concerned with the fact that
	 the current main method reads in a file that encodes graphs via an
	 adjacency matrix (which takes time O(n^2) for a graph of n vertices).
   
   (originally from B. Bird - 03/11/2012)
   (revised by N. Mehta - 10/9/2018)
*/

import java.io.File;
import java.util.*;

/*
* 	Helper class to store edge weights and its vertices
* */
class edgesWeight implements Comparable<edgesWeight>{
	int u;
	int v;
	int weight;
	public edgesWeight ( int w, int p, int q) { // Constructor with vertices and weight of the edge
		v = p;
		u = q;
		weight = w;
	}
	public int getWeight(){ // Return weight of edge
		return this.weight;
	}
	public int getU(){ // Return one of the edge's vertices
		return this.u;
	}
	public int getV(){ // Return the other edge's vertex
		return this.v;
	}
	// Used to overrride the compareTo function to sort the priority queue
	@Override
	public int compareTo(edgesWeight otherWeight) {
		if (this.getWeight() > otherWeight.getWeight()) {
			return 1;
		}
		else if (this.getWeight() < otherWeight.getWeight()) {
			return -1;
		} else {
			return 0;
		}
	}
}

/*
* 	Class helper to perform quick weighted union
* 	Adapted from the textbook page 228
* */
class UF {
	private static int parent[];
	private static int size[];
	private static int count;
	UF(int n) { // Initialize
		count = n;
		parent = new int[n];
		size = new int[n];
		for(int i = 0; i < n; i++) {
			parent[i] = i;
			size[i] = 1;
		}
	}
	// Find the vertice n
	public static int find(int n) {
		while (n != parent[n])
			n = parent[n];
		return n;
	}
	//Check to see if the vertices are connected
	public static boolean connected(int v, int u) {
		return find(v) == find(u);
	}
	//Join the two vertices
	public static void union(int v, int u) {
		int rootV = find(v);
		int rootU = find(u);
		if (rootV == rootU) {
			return;
		}
		// make smaller root point to larger one
		if (size[rootV] < size[rootU]) {
			parent[rootV] = rootU;
			size[rootU] += size[rootV];
		} else {
			parent[rootU] = rootV;
			size[rootV] += size[rootU];
		}
		count--;
	}
}

public class MST {

    	/*
	* 	Function to calculate the MST using kruskals algorithm
	* 	@param int[][][] adj - the array that contains all the information about the graphs
	* 	@return totalWeight - the weight of the MST
	* */
	static int mst(int[][][] adj) {
		int n = adj.length;
		Queue<edgesWeight> minPQ = new PriorityQueue<edgesWeight>(); // Priority queue of edges
		UF uf = new UF(n); // weighted union find
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < adj[i].length; j++) {
				edgesWeight temp = new edgesWeight(adj[i][j][1], i, adj[i][j][0]); // Create a new edge object
				minPQ.add(temp); // Add edge to the priority queue
			}
		}
		int totalWeight = 0;
		while (!minPQ.isEmpty()) {
			edgesWeight smallWeight = minPQ.remove(); // Remove the minimum weighted edge
			if (!uf.connected(smallWeight.getV(), smallWeight.getU())){ // Check to see if the vertices are connected
				uf.union(smallWeight.getV(), smallWeight.getU()); // connect the vertices
				totalWeight += smallWeight.getWeight(); // add the minimum weight to the total weight
			}
		}
		return totalWeight;
	}
	
	public static void main(String[] args) {
	/* Code to test your implementation */
	/* You may modify this, but nothing in this function will be marked */

		int graphNum = 0;
		Scanner s;

		if (args.length > 0) {
	 	//If a file argument was provided on the command line, read from the file
	  		try {
				s = new Scanner(new File(args[0]));
	  		}
	  		catch(java.io.FileNotFoundException e) {
				System.out.printf("Unable to open %s\n",args[0]);
				return;
	  		}
	  		System.out.printf("Reading input values from %s.\n",args[0]);
		} else {
		//Otherwise, read from standard input
	 		s = new Scanner(System.in);
	 		System.out.printf("Reading input values from stdin.\n");
		}
		//Read graphs until EOF is encountered (or an error occurs)
		while(true) {
			graphNum++;
			if(!s.hasNextInt()) {
				break;
			}
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][][] adj = new int[n][][];
	 		int valuesRead = 0;
	          	for (int i = 0; i < n && s.hasNextInt(); i++) {
				LinkedList<int[]> edgeList = new LinkedList<int[]>();
				for (int j = 0; j < n && s.hasNextInt();
				 	int weight = s.nextInt();
					if(weight > 0) {
					edgeList.add(new int[]{j, weight});
				}
		                valuesRead++;
		        }
		        adj[i] = new int[edgeList.size()][2];
		        Iterator it = edgeList.iterator();
		        for(int k = 0; k < edgeList.size(); k++) {
				adj[i][k] = (int[]) it.next();
		        }
		}
		if (valuesRead < n * n) {
			System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
		        break;
		}

	      	// // output the adjacency list representation of the graph
		/*for(int i = 0; i < n; i++) {
	     		System.out.print(i + ": ");
	     		for(int j = 0; j < adj[i].length; j++) {
	     	    		System.out.print("(" + adj[i][j][0] + ", " + adj[i][j][1] + ") ");
	     		}
	     		System.out.print("\n");
	    	}
		*/
	      	int totalWeight = mst(adj);
	      	System.out.printf("Graph %d: Total weight of MST is %d\n",graphNum,totalWeight);

				
	      	}
    	} 
}
