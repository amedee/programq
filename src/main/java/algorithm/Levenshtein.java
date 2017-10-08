package algorithm;

/**
 * Created by joris on 10/8/17.
 */
public class Levenshtein {


    public static double relativeDistance(String a, String b)
    {
        double d = distance(a, b);
        d /= java.lang.Math.max(a.length(), b.length());
        return d;
    }

    public static int distance(String a, String b)
    {
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

}
