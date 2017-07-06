package com.example.isaiah.kenken;

/**
 * Created by Isaiah on 2017-07-05.
 */

public class LatinSquareGenerator {

    private int n;
    private int MIN_ITERATIONS;
    private int [][][] cube;
    private boolean proper = true;
    private OrderedTriple improperCell = null;


    public LatinSquareGenerator(int size) {
        this.n = size;
        cube = new int[n][n][n];
        this.MIN_ITERATIONS = 2 * size * size * size;
        this.initialize();
        this.printCube();
        this.shuffle();
        this.printCube();
    }

    private void initialize() {
        for(int num = 0; num < n; num++) {
            for(int row = 0; row < n; row++) {
                for(int col = 0; col < n; col++) {
                    if(row + col ==  num || row + col == num + n) {
                        cube[row][col][num] = 1;
                    }
                }
            }
        }
    }

    public void printCube() {
        for(int row = 0; row < n; row++) {
            for(int col = 0; col < n; col++) {
                for(int num = 0; num < n; num++) {
                    if(cube[row][col][num] == 1) {
                        System.out.print(num + 1 + "  ");
                        break;
                    }
                }
            }
            System.out.println(" ");
        }
        System.out.println(" ");
        System.out.println(" ");
    }

    private void move(OrderedTriple t, int x1, int y1, int z1) {
        cube[t.x][t.y][t.z]++;
        cube[t.x][y1][z1]++;
        cube[x1][t.y][z1]++;
        cube[x1][y1][t.z]++;

        cube[t.x][t.y][z1]--;
        cube[t.x][y1][t.z]--;
        cube[x1][t.y][t.z]--;
        cube[x1][y1][z1]--;
        if(cube[x1][y1][z1] == -1) {
            // Cube is improper
            proper = false;
            improperCell = new OrderedTriple(x1, y1, z1);
        } else {
            proper = true;
            improperCell = null;
        }
    }

    public void shuffle() {
        for (int i = 0; i < MIN_ITERATIONS || !this.proper; i++) {
            if(this.proper) {
                this.moveFromProper();
            } else {
                this.moveFromImproper();
            }
        }
    }

    private void moveFromProper() {
        // Select a random 0-cell
        OrderedTriple cell = null;
        while(cell == null) {
            int x = (int)(n*Math.random());
            int y = (int)(n*Math.random());
            int z = (int)(n*Math.random());
            if(cube[x][y][z] == 0) {
                cell = new OrderedTriple(x, y, z);
            }
        }

        // Find all corresponding ones for this cell
        int[] ones = new int [3];
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < n; j++) {
                int x = (int)(1.0/2*(i-1)*(i-2)*(j-cell.x) + cell.x);
                int y = i*(i-2)*(cell.y-j) + cell.y;
                int z = (int)(1.0/2*i*(i-1)*(j-cell.z) + cell.z);
                if (cube[x][y][z] == 1){
                    ones[i] = j;
                }
            }
        }
        move(cell, ones[0], ones[1], ones[2]);
    }

    private void moveFromImproper() {
        OrderedTriple cell = improperCell;
        int[][] possibleIndices= {{-1, -1},{-1, -1},{-1, -1}};
        // Find all corresponding ones for this cell
        int[] ones = new int [3];
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < n; j++) {
                int x = (int)(1.0/2*(i-1)*(i-2)*(j-cell.x) + cell.x);
                int y = i*(i-2)*(cell.y-j) + cell.y;
                int z = (int)(1.0/2*i*(i-1)*(j-cell.z) + cell.z);
                if (cube[x][y][z] == 1){
                    if(possibleIndices[i][0] == -1) {
                        possibleIndices[i][0] = j;
                    } else {
                        possibleIndices[i][1] = j;
                    }
                }
            }
        }
        for(int i = 0; i < 3; i++) {
            int index = (int)(Math.random() * 2);
            ones[i] = possibleIndices[i][index];

        }
        move(cell, ones[0], ones[1], ones[2]);
    }

    private class OrderedTriple {
        public int x, y , z;
        public OrderedTriple (int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String toString() {
            return "(" + this.x + ", " + this.y + ", " + this.z + ")";
        }
    }
}
