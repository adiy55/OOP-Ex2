# OOP-Ex2

## Preface:

In this assignment we were tasked to implement several interfaces (in the api directory), and to create a GUI (graphical
user interface) that presents the graphs and algorithms to the user.

## 1. Algorithms:

Explanations of the complex algorithms we implemented:

**Shortest Path Distance & Shortest Path:**

* These two algorithms use ***Dijkstra's Algorithm***. Although this algorithm has the same runtime as the
  Floyd–Warshall Algorithm, it has a much better space complexity.

* The function returns a hashmap containing a two-valued double array for each node. The array stores the shortest
  distance from the source node to the current node, and the previous path from the source node.

With this information we can easily deduce the desired results.

**Center:**

* This algorithm finds the node which minimizes the maximum distance of all nodes from it.
* The function iterates the nodes and (using ***Dijkstra's Algorithm*** mentioned above) finds the maximum distance. Out
  of these values the minimum is chosen. The function returns the node's ID.

***Travelling Salesman Problem (TSP):***

* The function receives a list of nodes (cities) to visit, and returns the shortest path so that all nodes are visited
  at least once.
* After much consideration, a greedy algorithm was used to implement this. For each node visited we check which of the
  other nodes is closest in distance, and go to it. The action is repeated until there are no nodes to visit.

## 2. Performance:

In the table below you see the performance of the most time-consuming algorithms.

**Note:** The time is measured in seconds. When time exceeds a minute the fraction is excluded since it has little to no
importance.

For the sake of unity and simplicity, we ran the algorithms on the same input queries on these different graphs.

|                        | is Connected           | Shortest Path Distance | Shortest Path      | Center         | TSP (for 6 cities) |
| ---------------------- | ---------------------- | ---------------------- | ------------------ | -------------- | ------------------ |
| 1,000 Nodes            | 0.215                  | 0.211                  | 0.222              | 2.09           | 0.344              |
| 10,000 Nodes           | 0.914                  | 0.996                  | 0.987              | 27 min, 34 sec | 4.747              |
| 100,000 Nodes          | 16.421                 | 68                     | 71                 | Timeout        | 21 min, 31 sec     |

## 3. GUI:

The GUI was made with JavaFX.

It allows the user to save and load graphs (JSON file format), run algorithms and edit the graph.

You can run the GUI from the command line using the ``Ex2.jar`` file in this repository. You can find sample graphs in
the ``data`` directory.

### Use the following command:

```
java -jar Ex2.jar data/G1.json 
```

## 4. Dependencies:

* ``Java`` 16
* ``Gson`` com.google.code.gson: 2.8.9
* ``json`` org.json: 20210307
* ``JavaFX`` org.openjfx: 17.0.2-ea+1

Dependency for test classes:

* ``JUnit``: org.junit.jupiter: 5.8.2

You can download these libraries using the ``pom.xml`` file.

## 5. UML

![Click to enlarge](https://raw.githubusercontent.com/adiy55/OOP-Ex2/5eb65487fc80316a4f634d7cd32482b12ecd2c1c/data/ClassDiagram.svg?token=AK4TG7M7WQLUIIFDGPO7WQTBWU7DQ)
