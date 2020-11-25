package actor;

import java.util.ArrayList;
import java.util.Map;

public final class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<String> filmography;
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography,
                 final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    /**
     * Return total number of awards
     */
    public int getAwardsNumber() {
        int totalAwards = 0;
        for (Integer noAwards : getAwards().values()) {
            totalAwards += noAwards;
        }
        return totalAwards;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }
}
