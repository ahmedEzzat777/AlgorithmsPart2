import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BaseballElimination {
    private class TeamInfo {
        int Wins;
        int Loses;
        int Remaining;
    }

    private class Solution
    {
        int InputCapacity;
        FordFulkerson FF;
        boolean TriviallyEliminated = false;
        int TriviallyEliminatedIdx = -1;

        Solution(int inputCapacity,
                 FordFulkerson ff){
            this.InputCapacity = inputCapacity;
            this.FF = ff;
        }

        Solution(boolean triviallyEliminated,
                 int triviallyEliminatedIdx){
            this.TriviallyEliminated = triviallyEliminated;
            this.TriviallyEliminatedIdx = triviallyEliminatedIdx;
        }
    }

    private int m_n;
    private HashMap<String, Integer> m_teamsIndices;
    private String[] m_teamsByIndices;
    private TeamInfo[] m_teamsInfo;
    private int[][] m_games;

    public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
    {
        In file = new In(filename);

        m_n = Integer.parseInt(file.readLine());
        int idx = 0;
        m_teamsIndices = new HashMap<>();
        m_teamsByIndices = new String[m_n];
        m_games = new int[m_n][m_n];
        m_teamsInfo = new TeamInfo[m_n];

        while (file.hasNextLine())
        {
            String line = file.readLine().trim();
            String[] strings = line.split("\\s+");
            m_teamsIndices.put(strings[0], idx);
            m_teamsByIndices[idx] = strings[0];
            TeamInfo info = new TeamInfo();
            info.Wins = Integer.parseInt(strings[1]);
            info.Loses = Integer.parseInt(strings[2]);
            info.Remaining = Integer.parseInt(strings[3]);
            m_teamsInfo[idx] = info;

            for(int i = 0; i < m_n; i++)
                m_games[i][idx] = Integer.parseInt(strings[4+i]);

            idx++;
        }
    }

    public int numberOfTeams()                        // number of teams
    {
        return m_n;
    }

    public Iterable<String> teams()                                // all teams
    {
        return Arrays.asList(m_teamsByIndices);
    }

    public int wins(String team)                      // number of wins for given team
    {
        Integer teamIdx = m_teamsIndices.get(team);

        if(teamIdx == null)
            throw new IllegalArgumentException();

        return m_teamsInfo[teamIdx].Wins;
    }

    public int losses(String team)                    // number of losses for given team
    {
        Integer teamIdx = m_teamsIndices.get(team);

        if(teamIdx == null)
            throw new IllegalArgumentException();

        return m_teamsInfo[teamIdx].Loses;
    }

    public              int remaining(String team)                 // number of remaining games for given team
    {
        Integer teamIdx = m_teamsIndices.get(team);

        if(teamIdx == null)
            throw new IllegalArgumentException();

        return m_teamsInfo[teamIdx].Remaining;
    }

    public              int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        Integer team1Idx = m_teamsIndices.get(team1);
        Integer team2Idx = m_teamsIndices.get(team2);

        if(team1Idx == null || team2Idx == null)
            throw new IllegalArgumentException();

        return m_games[team1Idx][team2Idx];
    }

    public          boolean isEliminated(String team)              // is given team eliminated?
    {
        Solution solution = getIsEliminatedSolution(team);

        if(solution.TriviallyEliminated)
            return true;

        double maxFlow = solution.FF.value();
        return solution.InputCapacity != maxFlow;
    }

    public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        Solution solution = getIsEliminatedSolution(team);

        if(solution.TriviallyEliminated)
        {
            ArrayList<String> list = new ArrayList<>();
            list.add(m_teamsByIndices[solution.TriviallyEliminatedIdx]);
            return list;
        }

        FordFulkerson ff = solution.FF;
        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i < m_n; i++)
        {
            if(ff.inCut(i))
                list.add(m_teamsByIndices[i]);
        }

        return list.isEmpty()? null : list;
    }

    private Solution getIsEliminatedSolution(String team)
    {
        Integer teamIdx = m_teamsIndices.get(team);

        if(teamIdx == null)
            throw new IllegalArgumentException();

        int numberOfGames = (m_n*(m_n-1))/2; //nC2
        int numberOfGraphVertices = numberOfGames + m_n + 2; //games + n teams + 2
        int s = numberOfGraphVertices - 2;
        int t = numberOfGraphVertices - 1;
        int winsPossibleForIdx = m_teamsInfo[teamIdx].Wins + m_teamsInfo[teamIdx].Remaining;

        FlowNetwork network = new FlowNetwork(numberOfGraphVertices);
        int gameCounter = 0;
        int inputCapacity = 0;

        for (int i = 0; i < m_n; i++)
        {
            int possibleWinsForI = winsPossibleForIdx - m_teamsInfo[i].Wins;

            if(possibleWinsForI < 0)
                return new Solution(true, i);

            network.addEdge(new FlowEdge(i, t, i==teamIdx? 0 : possibleWinsForI));

            for (int j = i + 1; j < m_n; j++)
            {
                boolean inValidPath = i == teamIdx || j == teamIdx;

                network.addEdge(new FlowEdge(s, m_n + gameCounter, inValidPath ? 0 : m_games[i][j]));
                network.addEdge(new FlowEdge(m_n + gameCounter, i, inValidPath ? 0 : Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(m_n + gameCounter, j, inValidPath ? 0 : Double.POSITIVE_INFINITY));
                inputCapacity += i==teamIdx || j==teamIdx? 0 : m_games[i][j];
                gameCounter++;
            }
        }
        return new Solution(inputCapacity, new FordFulkerson(network, s, t));
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams12.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
