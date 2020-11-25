package entertainment;

import java.util.ArrayList;

public abstract class Video {
    private String title;
    private int year;
    private ArrayList<String> cast;
    private ArrayList<String> genres;

    public Video(final String title, final int year,
                 final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
    }

    public Video() {
    }

    /**
     * Return video rating
     */
    public abstract Double averageRating();

    /**
     * Return video duration
     */
    public abstract int getDuration();

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }
}
