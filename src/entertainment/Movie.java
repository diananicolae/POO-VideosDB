package entertainment;

import java.util.ArrayList;
import java.util.List;

public final class Movie extends Video {
    private final int duration;
    private List<Double> ratings;

    public Movie(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
        this.ratings = new ArrayList<>();
    }

    /**
     * Return movie rating
     */
    public Double averageRating() {
        if (ratings.isEmpty()) {
            return null;
        }

        double rating = 0.0;
        for (double movieRating : ratings) {
            rating += movieRating;
        }
        rating /= ratings.size();
        return rating;
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }
}
