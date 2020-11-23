package entertainment;

import java.util.ArrayList;

public final class Serial extends Video {
    private int numberOfSeasons;
    private ArrayList<Season> seasons;

    public Serial(final String title, final ArrayList<String> cast,
                  final ArrayList<String> genres,
                  final int numberOfSeasons, final ArrayList<Season> seasons,
                  final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public Serial() {
    }

    /**
     * Number of current season
     */
    public Double averageRating() {
        Double rating = 0.0;
        int unratedSeasons = 0;

        for (Season season : seasons) {
            if (season.getRatings().isEmpty()) {
                unratedSeasons++;
                continue;
            }
            for (Double seasonRating : season.getRatings()) {
                rating += seasonRating / season.getRatings().size();
            }
        }
        if (unratedSeasons == numberOfSeasons) {
            return null;
        }
        rating /= numberOfSeasons;
        return rating;
    }

    /**
     * Number of current season
     */
    public int getDuration() {
        int duration = 0;
        for (Season season : seasons) {
            duration += season.getDuration();
        }
        return duration;
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    @Override
    public String toString() {
        return "Serial{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }
}
