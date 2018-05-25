import java.util.*;
import java.io.*;
import java.lang.Math.*;

public class TruffleFinder {

  public static void main(String[] args) throws FileNotFoundException, InterruptedException {

    //announce purpose of program
    sleepDot();
    System.out.println("This program is designed to produce an optimal path from the top left corner to the bottom right corner of a square grid with weigthed vertices and edges, given that a player only moves right/down and cannot retrace their steps. In this case, the grid represents a field full of truffles, and we need to pick the maximum number of truffles possible while following the guidelines stated above.");

    // reading file info into array
    Scanner console = new Scanner(System.in);
    sleepDot();
    System.out.println("Please enter your file name: ");
    Node[][] path = calcLongestPath(loadFile(console.nextLine()));

    // calculating the maximum number of truffles that can be picked on this path
    int maxTruffles = path[path.length - 1][path.length - 1].longestPath;

    System.out.println();
    System.out.println("The maximum amount of truffles that can be picked from this square is " + maxTruffles + ".");
    System.out.println();

    // printing coordinates of the optimal path
    System.out.println();
    System.out.println("To find this optimal path follow these coordinates: ");
    System.out.println();

    printLongestPath(path);

    System.out.println();
    System.out.println("***************************************");
    System.out.println("For an i x j grid the coordinates read: ");
    System.out.println();
    System.out.println("Top Left = (0,0)");
    System.out.println("Top Right = (0,j)");
    System.out.println("Bottom Left = (i,0)");
    System.out.println("Bottom Right = (i,j)");
    System.out.println("***************************************");

  }

  public static void sleepDot() throws InterruptedException {
    Thread.sleep(750);
    System.out.print(".");
    System.out.print(" ");
    System.out.print(" ");
    Thread.sleep(750);
    System.out.print(".");
    System.out.print(" ");
    System.out.print(" ");
    Thread.sleep(750);
    System.out.print(".");
    System.out.print(" ");
    System.out.println(" ");
    Thread.sleep(750);
  }

  // node class:
  // each node contains an index, edgeRight, edgeDown and longestPath
  public static class Node {

    int x;
    int y;
    int edgeDown;
    int edgeRight;
    // longestPath is equal to the max amount of truffles that can be found
    // by going from the upper right corner to this node
    int longestPath = 0;

    public Node(int x, int y, int edgeRight, int edgeDown){
      this.x = x;
      this.y = y;
      this.edgeDown = edgeDown;
      this.edgeRight = edgeRight;
    }

    public void setLongestPath(int k) {
      longestPath = k;
    }

  }

  // this method reads the file into an array of node objects
  public static Node[][] loadFile (String filename) throws FileNotFoundException {


    // this scanner figures out how large the square is, i.e. how many nodes exist in the graph
    Scanner count = new Scanner(new File(filename));
    double numberOfNodes = 0.0;

    while (count.hasNext()) {
      String skip = count.next();
      numberOfNodes = numberOfNodes + 1.0;
    }
    int arraySize = (int)Math.sqrt(numberOfNodes/2);

    // this scanner converts the text file into node objects
    Scanner input = new Scanner(new File(filename));

    // array where the nodes are stored
    Node[][] path = new Node[arraySize][arraySize];

    // loading nodes into the array
    for (int i = 0; i < arraySize; i++) {
      for (int j = 0; j < arraySize; j++) {
        int down = Character.getNumericValue(input.next().charAt(1));
        int right = Character.getNumericValue(input.next().charAt(0));
        path[i][j] = new Node(i, j, down, right);
      }
    }

    return path;
  }

  // this method updates the longestPath field for each node in path[][]
  public static Node[][] calcLongestPath (Node[][] path) {
    for (int i = 0; i < path.length; i++) {
      for (int j = 0; j < path.length; j++) {
        // node in upper left corner
        if (i == 0 && j == 0) {
          path[i][j].setLongestPath(0);
        }
        // node on top boundary
        else if (i == 0 && j > 0) {
          int longestPath = path[i][j-1].longestPath + path[i][j-1].edgeRight;
          path[i][j].setLongestPath(longestPath);
        }
        // node on left boundary
        else if (j == 0 && i > 0) {
          int longestPath = path[i-1][j].longestPath + path[i-1][j].edgeDown;
          path[i][j].setLongestPath(longestPath);
        }
        // any other node
        else {
          int longestPath = Math.max(path[i-1][j].longestPath + path[i-1][j].edgeDown,
                                     path[i][j-1].longestPath + path[i][j-1].edgeRight);
          path[i][j].setLongestPath(longestPath);
        }
      }
    }

    return path;
  }

  // this method prints out the coordinates traversed to achieve the optimal path
  public static void printLongestPath (Node[][] path) {
    // create stack to push nodes that have been visited
    Stack<Node> optimalPath = new Stack<Node>();

    int i = path.length - 1;
    int j = path.length - 1;

    // starts by pushing the node at the end of the path (bottom right corner)
    optimalPath.push(path[i][j]);

    // from the bottom right corner, checks which of its neighbors (above and to the left) has
    // a greater longestPath value and pushes that node to the stack
    while (i > 0 || j > 0) {
      // top boundary nodes, in this case the only possible path remaining is all the way to the left
      if (i == 0) {
        j--;
        optimalPath.push(path[i][j]);
      }
      // left boundary nodes, in this case the only possible path remaining is all the way to the top
      else if (j == 0) {
        i--;
        optimalPath.push(path[i][j]);
      }
      // case where the neighboring node above has larger longestPath value
      else if (path[i-1][j].longestPath > path[i][j-1].longestPath) {
        i--;
        optimalPath.push(path[i][j]);
      }
      // case where the neighboring node to the left has the larger longestPath value
      else {
        j--;
        optimalPath.push(path[i][j]);
      }
    }

    // prints the indices from each node pushed to the stack
    while (!optimalPath.isEmpty()) {
      Node currNode = optimalPath.pop();
      System.out.println("(" + currNode.x + ", " + currNode.y + ")");
    }
  }

}
