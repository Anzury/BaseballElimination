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
    private boolean[] visited; // Visited nodes
    private int G = 0;

    // False if maxFlow hasn't been solved for this instance. True 
    // otherwise.
    private boolean solved = false; 

    FlowNetwork(int t ,int n, int[][] Data){
        this.n = n;

        int matches = ((n-1)*(n-2)/2);
        // Loop variables
        int x = 0, y = 0, k = 0, l = 0;
        
        // Set size of adjacency list
        this.sink = matches + n;
        this.adj_list = new ArrayList<ArrayList<Edge>>(this.sink+1);
        for(k = 0; k < sink+1; k++)
            adj_list.add(k, new ArrayList<Edge>());

        for(y = 0; y < n-1; y++){
            for(x = y+1; x < n; x++){
                if(y!= t && x!=t){
                    l++;
                    // We have three types of edges :
                    // ---------------- FIRST TYPE OF EDGE ----------------
                    // The edges from the source s and the matches 
                    // (by convention, s = 0)
                    // (matches are sorted in order of minimum index : 1-2, 1-3, ... , 2-5,2-6, ...)
                    this.G+=Data[y][x+2];
                    adj_list.get(0).add(new Edge(l, Data[y][x+2], 0));
                    // ---------------- SECOND TYPE OF EDGE ----------------
                    // The edges between matches and team
                    adj_list.get(l).add(new Edge(y+matches, Integer.MAX_VALUE, 0));
                    adj_list.get(l).add(new Edge(x+matches, Integer.MAX_VALUE, 0));
                    k = x;
                }
            }
            // ---------------- THIRD TYPE OF EDGE ----------------
            // The edges between teams and the pit
            // (by convention, p = ((n-1)*(n-2)/2) + n + 2)
            adj_list.get(y+matches+1).add(new Edge(sink, Data[t][0]+ Data[t][1]-Data[y][0], 0));
        }
        // ---------------- THIRD TYPE OF EDGE ----------------
        // The edges between teams and the pit
        // (by convention, p = ((n-1)*(n-2)/2) + n + 2)
        for( x = 0 ; x < sink+1; x++){
            System.out.print(x);
            for(Edge e : adj_list.get(x)){
            System.out.print(" " + e.dest + ":"+e.cap);}
            System.out.println();}
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
            int N = sink+1;
            this.visited = new boolean[N];
            // minimum cut
            this.maxFlow = 0; // Maximum flow

            // Find max flow using Depth First Search (DFS)
            do {
                // Since we'll be calculating the minimum between the flow
                // value passed to DFS and the residual flow of edges, we
                // ensure that the initial flow value passed to DFS is large
                // enough (that way, initially, the actual residual flow will
                // be picked)
                flow = this.DFS(this.source, Integer.MAX_VALUE);
                this.maxFlow += flow;
            } while(flow != 0); // Until there is an augmenting path


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
        this.visited[node] = true; // We mark the node as visited
        
        for(Edge e : edges) {
           // If we found an augmenting path
            if(visited[e.dest] != true && e.cap-e.flow > 0) {
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

    public boolean flowExists(){
        if(this.solved) return this.maxFlow == this.G;
        else {
            this.getMaxFlow();
            return this.maxFlow == this.G;
        }
    }
}