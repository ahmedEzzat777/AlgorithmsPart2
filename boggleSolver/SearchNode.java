import java.util.ArrayList;

public class SearchNode {
    int i;
    int j;
    int rows;
    int cols;

    public SearchNode(int rows, int cols, int i, int j){
        this.rows = rows;
        this.cols = cols;
        this.i = i;
        this.j = j;
    }

    public Iterable<SearchNode> adj()
    {
        ArrayList<SearchNode> list = new ArrayList<>(8);

        //top
        if(i != 0)
            list.add(new SearchNode(rows, cols, i - 1, j));

        //bottom
        if(i != rows - 1)
            list.add(new SearchNode(rows, cols, i + 1, j));

        //left
        if(j != 0)
            list.add(new SearchNode(rows, cols, i, j - 1));

        //right
        if(j != cols - 1)
            list.add(new SearchNode(rows, cols, i, j + 1));

        //top left
        if(i != 0 && j != 0)
            list.add(new SearchNode(rows, cols, i - 1, j - 1));

        //top right
        if(i != 0 && j != cols - 1)
            list.add(new SearchNode(rows, cols, i - 1, j + 1));

        //bottom left
        if(i != rows - 1 && j != 0)
            list.add(new SearchNode(rows, cols, i + 1, j - 1));

        //bottom right
        if(i != rows - 1 && j != cols - 1)
            list.add(new SearchNode(rows, cols, i + 1, j + 1));

        return list;
    }
}
