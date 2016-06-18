import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Madss on 11-06-2016.
 */
public class Elo {
    private static HashMap<String, Double> ratings = new HashMap<>();
    private static final double K = 32;
    private static final double initialRating = 1000;

    //The address of a file containing all previous results should be given as argument when running main.
    public static void main(String[] args) {
        int count = 1;
        if (args.length == 0) {
            System.out.println("Please give a file as input");

        } else {
            ArrayList<String> matches = readFile(args[0]);

            for (String s : matches) {
                computeNewElo(s);
            }

            for(Map.Entry<String,Double> entry : sortHasMap(ratings).entrySet()) {
                String key = entry.getKey();
                int value =  entry.getValue().intValue();

                System.out.println(count + ": " + key + " => " + value);
                count++;
            }
        }
    }

    public static void computeNewElo(String match) {
        double S2;
        String[] matchSplit = match.split(" ");
        String person1 = matchSplit[0];
        String person2 = matchSplit[1];
        double matchResult = Double.valueOf(matchSplit[2]);

        if (!ratings.containsKey(person1)) {
            ratings.put(person1, initialRating);
        }
        if (!ratings.containsKey(person2)) {
            ratings.put(person2, initialRating);
        }

        double ratingOfPerson1 = ratings.get(person1);
        double ratingOfPerson2 = ratings.get(person2);

        //Transformed rating fo each player
        double R1 = Math.pow(10, (ratingOfPerson1 / 400));
        double R2 = Math.pow(10, (ratingOfPerson2 / 400));

        //Expected score for each player
        double E1 = R1 / (R1 + R2);
        double E2 = R2 / (R1 + R2);

        //Match result
        double S1 = matchResult;
        if (matchResult == 0) {
             S2 = 1;
        } else {
             S2 = 0;
        }

        //New rating
        double newRatingOfPerson1 = ratingOfPerson1 + K * (S1 - E1);
        double newRatingOfPerson2 = ratingOfPerson2 + K * (S2 - E2);

        ratings.put(person1, newRatingOfPerson1);
        ratings.put(person2, newRatingOfPerson2);
    }

    public static ArrayList<String> readFile(String address) {
        ArrayList<String> result = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(address))) {
            String line = br.readLine();

            while (line != null) {
                result.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static LinkedHashMap<String, Double> sortHasMap(HashMap<String, Double> map) {
        LinkedHashMap<String, Double> resultMap = new LinkedHashMap<>();

        while (!map.isEmpty()) {
            double largestValue = 0;
            String largestKey = "";

            for(Map.Entry<String,Double> entry : map.entrySet()) {
                if (entry.getValue() > largestValue) {
                    largestValue = entry.getValue();
                    largestKey = entry.getKey();
                }
            }
            resultMap.put(largestKey, largestValue);
            map.remove(largestKey);
        }
        return resultMap;
    }
}
