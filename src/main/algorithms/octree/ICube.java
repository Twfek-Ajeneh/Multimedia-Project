package main.algorithms.octree;

public interface ICube {
    void classification();
    void reduction();
    void assignment();
    int[] colorMap();
    int[][] pixels();
}
