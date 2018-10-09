/*Done this before so this
* should be simple , calculate the pOpenCells
* math functions mean, standard dev, and statistic mean intervals
* and analyse the data structure based on the size of the grid and the number of calculations
* pOfCells should be modelled appropriately and represented as a time representation of T
 */
public class PercolationStats {
    private double[] pOpenCells;

    public PercolationStats(int N, int T) {
        // perform trials independent experiments on an n-by-n grid
        if (N < 1 || T < 1) {
            throw new IllegalArgumentException("N and T must be grater than 0.");
        }
        pOpenCells = new double[T];
        for (int t = 0; t < T; t++) {
            Percolation p = new Percolation(N); //Create a new instance for each test
            int count = 0;
            do {
                int i, j;
                do {
                    i = (int) (Math.random() * N) + 1;
                    j = (int) (Math.random() * N) + 1;
                } while (p.isOpen(i, j)); // generate random Number's
                p.open(i, j);
                count++;
            } while (!p.percolates()); // essentially test the model for percolation
            pOpenCells[t] = (double) count / (N * N); // the probability of open cells
        }
    }
    public double mean() {
        // sample mean of percolation threshold
        double tot = 0;
        for (int i = 0; i < pOpenCells.length; i++) {
            tot += pOpenCells[i];
        }
        return tot / pOpenCells.length;
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        double tot = 0;
        double mu = mean();
        for (int i = 0; i < pOpenCells.length; i++) {
            tot += pOpenCells[i] * pOpenCells[i];
        }
        return Math.sqrt((tot - pOpenCells.length * mu * mu) / (pOpenCells.length - 1));
    }

    public double confidenceLo() {
        // low  endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(pOpenCells.length);
    }

    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(pOpenCells.length);
    }

    public static void main(String[] args) {
        // test client (described below)
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(N, T);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
    }
}