# OOP - Assignment 2 - Directed Weighted Graph Algorithms

### Abstract
In this Assignment, we have been tasked to implement several interfaces (in the api folder),
as well as to create a UI to present the graphs and the algorithms to the user (with 
functionality of buttons to showcase the algorithms).
<br><br>

## The Algorithms:
Below We will describe some of the more complex algorithms out of the ones we have 
implemented:
### Shortest Path Distance & Shortest Path:
These two algorithms were implemented by using the Dijkstras Algorithm, which, while having 
the same runtime of the Floyd–Warshall Algorithm, uses much less space in memory for its 
calculations. It returns a hashmap containing a two-valued double array for each node, 
which stores the shortest distance from the source node to the current node and the previous
on the path from the source node.<br>
With this information, we can easily deduce the desired results.

### Center
This algorithm had to find the node which minimizes the maximum distance of all nodes from it.
This we do by iterating over the nodes, and finding (using the algorithms above) the maximum 
distance. Out of these values, we choose the minimum, and return the node's ID.

### TSP:
We receive a list of nodes which we have to visit, and return the shortest path so that all
nodes are visited at least once. After much consideration, a Greedy algorithm was used to
implement this algorithm. For each node to visit, we check which of the other nodes is closest
in terms of distance, and go to it. The action is repeated until there are no nodes to visit.

## Performance
Below, we shall present the performance of the most time-consuming algorithms (in seconds).
For the sake of unity and simplicity, we shall run the same algorithms on the same input queries on 
these different graphs:

|                        | is Connected           | Shortest Path Distance | Shortest Path      | Center        | TSP           |
| ---------------------- | ---------------------- | ---------------------- | ------------------ | ------------- | ------------- |
| 1000 Nodes             | 0.245                  | 0.265                  | 0.270              | 17.717        | 0.734         |
| 10000 Nodes            | Shortest Path Distance | Paragraph              | Text               | Description   | 42.469        |
| 100000 Nodes           | Shortest Path Distance | Paragraph              | Text               | Description   | Description   |
| 1000000 Nodes          | Shortest Path Distance | Paragraph              | Text               | Description   | Description   |