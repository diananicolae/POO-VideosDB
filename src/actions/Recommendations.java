package actions;

import common.Constants;
import entertainment.Video;
import fileio.ActionInputData;
import user.User;

import java.util.ArrayList;
import java.util.List;

public final class Recommendations {
    private final Database database;

    public Recommendations(final Database database) {
        this.database = database;
    }

    /**
     * Returns first unwatched video from database
     */
    public String standardRecommendation(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        List<Video> videos = new ArrayList<>(database.videosDB());

        if (user == null) {
            return "StandardRecommendation cannot be applied!";
        }
        videos.removeIf(video -> user.getHistory().containsKey(video.getTitle()));
        if (videos.isEmpty()) {
            return "StandardRecommendation cannot be applied!";
        }

        return "StandardRecommendation result: " + videos.get(0).getTitle();
    }

    /**
     * Returns first unwatched video from database
     * with the highest rating
     */
    public String bestUnseenRecommendation(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        List<Video> videos = new ArrayList<>(database.videosDB());

        if (user == null) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }
        videos.removeIf(video -> user.getHistory().containsKey(video.getTitle()));
        if (videos.isEmpty()) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }
        videos.sort((video1, video2) -> {
            if (video1.averageRating() == null) {
                return 1;
            }
            if (video2.averageRating() == null) {
                return -1;
            }
            return video2.averageRating().compareTo(video1.averageRating());
        });
        return "BestRatedUnseenRecommendation result: " + videos.get(0).getTitle();
    }

    /**
     * Returns first unwatched video from database
     * from the most popular genre
     */
    public String popularRecommendation(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        List<String> genres = ProcessUtils.genrePopularity(database.videosDB(), database);

        if (user == null || !user.getSubscription().equals(Constants.PREMIUM)) {
            return "PopularRecommendation cannot be applied!";
        }

        for (String genre : genres) {
            List<Video> videos = new ArrayList<>(database.videosDB());
            videos.removeIf(video -> !video.getGenres().contains(genre)
                    || user.getHistory().containsKey(video.getTitle()));
            if (!videos.isEmpty()) {
                return "PopularRecommendation result: " + videos.get(0).getTitle();
            }
        }
        return "PopularRecommendation cannot be applied!";
    }

    /**
     * Returns first unwatched video from database
     * most frequent in users' favorite videos
     */
    public String favoriteRecommendation(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        List<Video> videos = new ArrayList<>(database.videosDB());

        if (user == null || !user.getSubscription().equals(Constants.PREMIUM)) {
            return "FavoriteRecommendation cannot be applied!";
        }

        videos.removeIf(video -> user.getHistory().containsKey(video.getTitle()));
        if (videos.isEmpty()) {
            return "FavoriteRecommendation cannot be applied!";
        }
        String title = ProcessUtils.getFavoriteVideos(videos, Constants.DATABASE,
                Constants.FIRST_VIDEO, database).get(0);
        return "FavoriteRecommendation result: " + title;
    }

    /**
     * Returns first unwatched video sorted by rating
     * filtered at input
     */
    public String searchRecommendation(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        List<Video> videos = new ArrayList<>(database.videosDB());
        List<String> titles = new ArrayList<>();

        if (user == null || !user.getSubscription().equals(Constants.PREMIUM)) {
            return "SearchRecommendation cannot be applied!";
        }

        videos.removeIf(video -> !video.getGenres().contains(action.getGenre())
                || user.getHistory().containsKey(video.getTitle()));

        if (videos.isEmpty()) {
            return "SearchRecommendation cannot be applied!";
        }
        videos.sort((video1, video2) -> {
            if (video1.averageRating() == null || video2.averageRating() == null
                    || video1.averageRating().equals(video2.averageRating())) {
                return video1.getTitle().compareTo(video2.getTitle());
            }
            return video1.averageRating().compareTo(video2.averageRating());
        });

        for (Video video : videos) {
            titles.add(video.getTitle());
        }
        return "SearchRecommendation result: " + titles;
    }
}
