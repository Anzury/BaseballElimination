/**
 * -------------------------------------------
 *              FlowNetwork.java
 * -------------------------------------------
 *
 * Year : 2022
 * Course : Graphs and networks
 * Authors : Juanfer MERCIER, Adrien PICHON
 **/


import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

// Class to store edges of the flow network
class Edge {
    // Edge destination, capacity and flow
    int dest, cap, flow;
    Edge(int dest, int cap, int flow){
        this.dest = dest;
        this.cap = cap;
        this.flow = flow;
    }
}

// Class to represent a flow network adapted to the baseball problem
class FlowNetwork {
    // Adjacency list
    ArrayList<ArrayList<Edge>> adj_list; 
    private int n;

    private int source = 0, sink = -1; // Source and sink
    private int maxFlow = -1; // Maximum flow value
    private int[] visited; // Visited nodes
    private int t = 1; // Iteration number at which a vertex is visited
    private boolean[] minCut; // Minimum capacity cut

    // False if maxFlow/minCut hasn't been solved for this instance. True 
    // otherwise.
    private boolean solved = false; 

    FlowNetwork(int t ,int n, int[][] Data){
        this.n = n;
        
        // Loop variables
        int x = 0, y = 0, k = 0, idx = 0;

        // Set size of adjacency list
        this.sink = (n*(n-1)/2) + n + 1;
        this.adj_list = new ArrayList<ArrayList<Edge>>(this.sink+1);
        for(k = 0; k < sink+1; k++)
            adj_list.add(k, new ArrayList<Edge>());

        // We have three types of edges :
        // ---------------- FIRST TYPE OF EDGE ----------------
        // The edges from the source s and the matches 
        // (by convention, s = 0)
        // (matches are sorted in order of minimum index : 1-2, 1-3, ... , 2-5,2-6, ...)
        for(y = 1; y <= n-1; y++){
            for(x = y; x <= n-1; x++)
                adj_list.get(0).add(new Edge(y+1, Data[y][x+2], 0));
        }
        // ---------------- SECOND TYPE OF EDGE ----------------
        // The edges between matches and team

        // Penalties
        for(y = 1; y < n*(n-1)/2; y++){
            
            adj_list.get(y).add(new Edge(t, k, idx));
            adj_list.get(y).add(new Edge(t, k, idx));
        }


        // ---------------- THIRD TYPE OF EDGE ----------------
        // The edges between teams and the pit
        // (by convention, p = (n*(n-1)/2) + n + 2)
        for(y = 0; y < n; y++)
            for(x = 0; x < m; x++)
                adj_list.get(y*m+x+1).add(new Edge(this.sink, B[y][x], 0));
    }

    /* Returns adjacency list */
    public ArrayList<ArrayList<Edge>> getAdjacencyList() {
        return this.adj_list;
    }

    /* Returns maximum flow */
    public int getMaxFlow() {
        /* If the instance is already solved no need to solve it again */
        if(this.solved) return this.maxFlow;
        else {
            int flow = -1; // Flow returned by the DFS procedure
            int N = this.n*this.m;
            this.visited = new int[N+2];
            // CAUTION : we do not need the source and sink to compute the
            // minimum cut
            this.minCut = new boolean[N]; // default value : false
            this.maxFlow = 0; // Maximum flow

            // Find max flow using Depth First Search (DFS)
            do {
                // Since we'll be calculating the minimum between the flow
                // value passed to DFS and the residual flow of edges, we
                // ensure that the initial flow value passed to DFS is large
                // enough (that way, initially, the actual residual flow will
                // be picked)
                flow = this.DFS(this.source, Integer.MAX_VALUE);
                this.t++;
                this.maxFlow += flow;
            } while(flow != 0); // Until there is an augmenting path

            // Find min cut (we use the "timestamp" t to determine which
            // vertices are in set A and which one are not)
            // Idea based on :
            // https://stackoverflow.com/questions/4482986/how-can-i-find-the-minimum-cut-on-a-graph-using-a-maximum-flow-algorithm
            for(int i = 1; i <= N; i++)
                if(visited[i] == this.t-1)
                    this.minCut[i-1] = true;

            // We mark the instance as solved so we don't have to repeat 
            // the process and we return the maximum flow value
            this.solved = true; 
            return this.maxFlow;
        }
    }

    /* Depth First Search procedure adapted to the baseball problem 
     * 
     * node : the node from which we start the procedure
     * flow : the current flow passing through that node
     */
    private int DFS(int node, int flow) {
        // When we reach the sink, return augmented path flow
        if(node == this.sink) return flow;
        int subflow = -1;

        // Get list of edges of the node
        List<Edge> edges = this.adj_list.get(node);
        this.visited[node] = this.t; // We mark the node as visited
        
        for(Edge e : edges) {
           // If we found an augmenting path
            if(visited[e.dest] != this.t && e.cap-e.flow > 0) {
                // Update flow and run DFS from the node
                flow = Math.min(e.cap-e.flow, flow);
                subflow = DFS(e.dest, flow);

                // Update edges flow if there is an augmenting path
                if(subflow > 0){
                    e.flow += subflow;

                    for(Edge se : this.adj_list.get(e.dest))
                        if(se.dest == node)
                            se.flow -= subflow;

                    return subflow;
                }
            }
        }

        // No augmenting path : we return 0
        return 0;
    }

    public boolean[][] getMinCut() {
        // We get the minimum capacity cut when calculating the maximum 
        // flow of the network. So if the instance is already solved, we 
        // can get the minimum capacity cut
        if(!this.solved)
            this.getMaxFlow();

        // The boolean array representing the cut is unidimensional and 
        // we would like to have a 2D array to ease the process of printing 
        // the binarized image
        boolean[][] cut = new boolean[this.n][this.m];

        int x = 0;
        for(int y = 0; y < this.n; y++)
            for(x = 0; x < this.m; x++)
                cut[y][x] = this.minCut[y*this.m+x];

        return cut;
    }

}