import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;

public class SeamCarver {

    private class SearchNode{
        SeamCarver carver;
        int x;
        int y;
        double weight;

        public SearchNode(SeamCarver carver, int x, int y){
            this.carver = carver;
            this.x = x;
            this.y = y;
            this.weight = carver.energy(x,y);
        }

        Iterable<SearchNode> adj(Direction dir)
        {
            if(dir == Direction.Vertical)
                return getVerticalSearchNodes();
            else
                return getHorizontalSearchNodes();
        }

        private Iterable<SearchNode> getHorizontalSearchNodes() {
            ArrayList<SearchNode> list = new ArrayList<>(3);

            if(x >= carver.width() - 1)
                return list;

            if(y > 0)
                list.add(new SearchNode(carver, x + 1, y - 1));

            list.add(new SearchNode(carver, x + 1, y));

            if(y < carver.height() - 1)
                list.add(new SearchNode(carver, x + 1, y + 1));

            return list;
        }

        private Iterable<SearchNode> getVerticalSearchNodes() {
            ArrayList<SearchNode> list = new ArrayList<>(3);

            if(y >= carver.height() - 1)
                return list;

            if(x > 0)
                list.add(new SearchNode(carver, x - 1, y + 1));

            list.add(new SearchNode(carver, x, y + 1));

            if(x < carver.width() - 1)
                list.add(new SearchNode(carver, x + 1, y + 1));

            return list;
        }
    }

    private enum Direction{
        Horizontal,
        Vertical
    }

    private Picture m_picture;
    private int[][] m_edgeTo;
    private double[][] m_distanceTo;
    private double[][] m_energyCache;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture){
        if(picture == null)
            throw new IllegalArgumentException();
        m_picture = new Picture(picture);
        initializeEnergyCache();
    }

    private void initializeEnergyCache() {
        m_energyCache = new double[width()][height()];

        for(int x = 0 ; x < width(); x++)
            for(int y = 0; y < height(); y++)
                m_energyCache[x][y] = Double.POSITIVE_INFINITY;
    }

    // current picture
    public Picture picture(){
        return new Picture(m_picture);
    }

    // width of current picture
    public int width(){
        return m_picture.width();
    }

    // height of current picture
    public int height(){
        return m_picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y){
        validatePixel(x,y);

        if(x == 0 || x == width() -1
            || y == 0 || y == height() -1)
            return 1000; //higher than any other pixel

        if(m_energyCache[x][y] != Double.POSITIVE_INFINITY)
            return m_energyCache[x][y];

        Color p_left = m_picture.get(x - 1, y);
        Color p_right = m_picture.get(x + 1, y);
        Color p_top = m_picture.get(x, y - 1);
        Color p_bottom = m_picture.get(x, y + 1);

        double gradXSquared =
                square((p_left.getRed() - p_right.getRed()))+
                square((p_left.getBlue() - p_right.getBlue()))+
                square((p_left.getGreen() - p_right.getGreen()));

        double gradYSquared =
                square((p_top.getRed() - p_bottom.getRed()))+
                square((p_top.getBlue() - p_bottom.getBlue()))+
                square((p_top.getGreen() - p_bottom.getGreen()));

        double energy = Math.sqrt(gradXSquared + gradYSquared);
        m_energyCache[x][y] = energy;
        return energy;
    }

    private void validatePixel(int x, int y) {
        if(x < 0 || x >= width() ||
            y < 0 || y >= height())
            throw new IllegalArgumentException();
    }

    private double square(double x){
        return x*x;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam(){
        m_edgeTo = new int[width()][height()];
        m_distanceTo = new double[width()][height()];

        for(int x = 0 ; x < width(); x++)
        {
            for(int y = 0; y < height(); y++)
            {
                if(x == 0)
                    m_distanceTo[x][y] = energy(x, y);
                else
                    m_distanceTo[x][y] = Double.POSITIVE_INFINITY;

                m_edgeTo[x][y] = -1;
            }
        }

        for(int x = 0 ; x < width() - 1; x++)
        {
            for(int y = 0; y < height(); y++)
            {
                SearchNode from = new SearchNode(this, x, y);

                for (SearchNode to : from.adj(Direction.Horizontal))
                    relax(Direction.Horizontal, from, to);
            }
        }

        int[] seam = getSeamHorizontal();
        m_edgeTo = null;
        m_distanceTo = null;
        return seam;
    }

    private int[] getSeamHorizontal() {
        //get max in last row
        int idx = -1;
        double min = Double.MAX_VALUE;

        for(int i = 0; i < height(); i++)
        {
            if(m_distanceTo[width()-1][i] < min)
            {
                idx = i;
                min = m_distanceTo[width()-1][i];
            }
        }

        if(idx == -1)
            return null;

        //trace back from idx to get the seam
        int seam[] = new int[width()];

        for(int i = width() - 1; i >= 0; i--)
        {
            seam[i] = idx;
            idx = m_edgeTo[i][idx];
        }

        return seam;
    }

    private int[] getSeamVertical() {
        //get max in last row
        int idx = -1;
        double min = Double.MAX_VALUE;

        for(int i = 0; i < width(); i++)
        {
            if(m_distanceTo[i][height() - 1] < min)
            {
                idx = i;
                min = m_distanceTo[i][height() - 1];
            }
        }

        if(idx == -1)
            return null;

        //trace back from idx to get the seam
        int seam[] = new int[height()];

        for(int i = height() - 1; i >= 0; i--)
        {
            seam[i] = idx;
            idx = m_edgeTo[idx][i];
        }

        return seam;
    }

    private void relax(Direction dir, SearchNode from, SearchNode to) {
        if(m_distanceTo[to.x][to.y] > m_distanceTo[from.x][from.y] + to.weight)
        {
            m_distanceTo[to.x][to.y] = m_distanceTo[from.x][from.y] + to.weight;
            m_edgeTo[to.x][to.y] = dir == Direction.Horizontal ? from.y : from.x;
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam(){
        m_edgeTo = new int[width()][height()];
        m_distanceTo = new double[width()][height()];

        for(int x = 0 ; x < width(); x++)
        {
            for(int y = 0; y < height(); y++)
            {
                if(y == 0)
                    m_distanceTo[x][y] = energy(x, y);
                else
                    m_distanceTo[x][y] = Double.POSITIVE_INFINITY;

                m_edgeTo[x][y] = -1;
            }
        }

        for(int y = 0 ; y < height() - 1; y++)
        {
            for(int x = 0; x < width(); x++)
            {
                SearchNode from = new SearchNode(this, x, y);

                for (SearchNode to : from.adj(Direction.Vertical))
                    relax(Direction.Vertical, from, to);
            }
        }

        int [] seam = getSeamVertical();
        m_distanceTo = null;
        m_edgeTo = null;
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam){
        if(seam == null || height() <= 1)
            throw new IllegalArgumentException();

        validateSeam(Direction.Horizontal, seam);

        Picture picture = new Picture(width(), height() - 1);

        for(int x = 0; x < width(); x++)
        {
            int i = 0;
            for(int y = 0; y < height(); y++)
            {
                if(y != seam[x])
                    picture.setRGB(x, i++, m_picture.getRGB(x,y));
            }
        }
        m_picture = picture;
        initializeEnergyCache();
    }

    private void validateSeam(Direction dir, int[] seam) {
        if(seam.length != (dir == Direction.Horizontal ? width() : height()))
            throw new IllegalArgumentException();

        int max = dir == Direction.Horizontal ? height() : width();
        int prev = seam[0];
        for(int i : seam)
        {
            if(i < 0 || i >= max || (Math.abs(i-prev) > 1))
                throw new IllegalArgumentException();
            prev = i;
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
        if(seam == null || width() <= 1)
            throw new IllegalArgumentException();

        validateSeam(Direction.Vertical, seam);

        Picture picture = new Picture(width() - 1, height());

        for(int y = 0; y < height(); y++)
        {
            int i = 0;
            for(int x = 0; x < width(); x++)
            {
                if(x != seam[y])
                    picture.setRGB(i++, y, m_picture.getRGB(x,y));
            }
        }
        m_picture = picture;
        initializeEnergyCache();
    }

    //  unit testing (optional)
    public static void main(String[] args){
        Picture p = new Picture("chameleon.png");
        SeamCarver s = new SeamCarver(p);
        int[] x = s.findHorizontalSeam();
        int[] y = s.findVerticalSeam();
        s.removeHorizontalSeam(x);
        y = s.findVerticalSeam();
        s.removeVerticalSeam(y);
    }

}