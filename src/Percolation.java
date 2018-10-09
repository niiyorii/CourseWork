
public class Percolation {
    private boolean[] cells; //if open is 1 , block 0
    private int N; //create N-by-N grid
    private WeightedQuickUnionUF uf;
    private int top;
    private int bottom;

    public Percolation(int N)  {             // create N-by-N grid, with all sites blocked
        if (N <= 0) {
            throw new IllegalArgumentException("N must be bigger than 0");
        }
        this.N = N;
        uf = new WeightedQuickUnionUF(N*N + 2);
        cells = new boolean[N*N+2];   // 0 top_visual N*N+1 bottom_visual
        top = 0;
        bottom = N*N +1;
        for (int i = 1; i <= N*N; i++) {
            cells[i] = false;   //initial all sites block
        }
    }

    public void open(int i, int j)  {        // open site (row i, column j) if it is not open already
        isInRange(i, j);
        int index = toIndex(i, j);
        cells[index] = true;

        if (i == 1) {
            uf.union(index, top);
        }
        if (!percolates()) {
            if (i == N) {
                uf.union(index, bottom);
            }
        }
        if (i < N && cells[index+N]) {
            uf.union(index, index+N);
        }
        if (i > 1 && cells[index-N]) {
            uf.union(index, index-N);
        }
        if (j < N && cells[index+1]) {
            uf.union(index, index+1);
        }
        if (j > 1 && cells[index-1]) {
            uf.union(index, index-1);
        }

    }

    private int toIndex(int i, int j) {
        isInRange(i, j);
        return j + (i-1) * N;
    }

    private void isInRange(int i, int j) {
        if (!(i >= 1 && i <= N && j >= 1 && j <= N)) {
            throw new IndexOutOfBoundsException("Index is not betwwen 1 and N");
        }
    }

    public boolean isOpen(int i, int j) {     // is site (row i, column j) open?
        isInRange(i, j);
        return cells[toIndex(i, j)];
    }

    /*A full site is an open site that can be connected to an open site in the top row
     * via a chain of neighboring (left, right, up, down) open sites.
     */
    public boolean isFull(int i, int j) {    // is site (row i, column j) full?
        isInRange(i, j);
        return uf.connected(top, toIndex(i, j));
    }

    /* Introduce 2 virtual sites (and connections to top and bottom).
     * Percolates iff virtual top site is connected to virtual bottom site.
     */
    public boolean percolates()  {           // does the system percolate?
        return uf.connected(top, bottom);
    }
    /*
    public void printMatrix() {
        int size = cells.length;
        for (int i = 0; i < size; i++) { //open(i,j);
            if (cells[i] == true)
                System.out.print("[X] ");
            else
                System.out.print("[ ] ");
            if (i % N == 0)
                System.out.println("\n");
        }
    }
    */

    public static void main(String[] args) {
        // test client (optional)
        int N = Integer.parseInt(args[0]);
        Percolation p = new Percolation(N); //Create a new instance for each test
        int count = 0;
        do {
            int i, j;
            do {
                i = (int) (Math.random()* N) + 1;
                j = (int) (Math.random() * N) + 1;
            } while (p.isOpen(i, j)); // generate random Number's
            p.open(i, j);
            count++;
            System.out.println("Does it Percolate? "+ count+ ": " + p.percolates());
        } while (!p.percolates()); // essentially test the model for percolation
        //p.printMatrix();



    }
}

    //Dev notes
    /*
    * 28/9/2018
    *  Created draft pseudocode from sketchpad notes
    *
    * 31/9/2019
    *  Revisited union-find and connected() - expanded propgation logic under open()
    *
    * 2/10/2018
    * Massive dose of coder's block -  the union find logic is fine, the criteria fits the requirements
    * There is a major/minor issue with the grid percolating, with Coder's block it
    * can be anything, at the moment I can't see the issue....
    * It passes all checks in terms of logical semantics - it's probably without a
    * doubt a massive case of bad code that just needs rubber ducking...
    * 3/10/2018
    * Finally have the 2D array percolating, minor issue with the implementation.
    * isFull is never called and percolates() always returns true,
    * made some advanced changes to the test client by adding 2 while loops
    * one to open the cells individually
    * another to test the model percolation
    * 9/10/2018
    * After a good few hours of rubber ducking the code, a Test client percolates it for all cases.
    * I had made a range check error for where i=1, I had implemented a cell variable as cells[i-1][j-1]
    * this was also the reason why it was percolating erroneously - it was connecting all sites as cell[0][0]
    * and creating injective sets for the top site and virtual bottom site intead of surjective
    * virtual sites have now been scrapped for a single array implementation and top/bottom index ints
    * for find(p) and union(s,n)
    * I also made a critical error in considering
    * Math.Random may uniformly unblock cells and
    * therefore at some integer N prevent percolation.
    *
    * with the model as-is, I can consider it a complete solution.
    *
     */