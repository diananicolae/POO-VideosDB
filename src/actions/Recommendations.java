package actions;

import common.Constants;
import entertainment.Video;
import user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Recommendations {
    private Recommendations() {
    }

    /**
     * Number of current season
     */
    public static String standardRecommendation(final String username) {
        User user = ProcessUtils.getUserInstance(username, ProcessData.users);
        if (user == null) {
            return "StandardRecommendation cannot be applied!";
        }

        for (Video video : ProcessData.videos) {
            if (!user.getHistory().containsKey(video.getTitle())) {
                return "StandardRecommendation result: " + video.getTitle();
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    /**
     * Number of current season
     */
    public static String bestUnseenRecommendation(final String username) {
        User user = ProcessUtils.getUserInstance(username, ProcessData.users);
        if (user == null) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }

        List<Video> videos = new ArrayList<>(ProcessData.videos);
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
     * Number of current season
     */
    public static String popularRecommendation(final String username) {
        User user = ProcessUtils.getUserInstance(username, ProcessData.users);
        if (user == null || !user.getSubscription().equals(Constants.PREMIUM)) {
            return "PopularRecommendation cannot be applied!";
        }
        List<String> genres = ProcessUtils.genrePopularity(ProcessData.videos);

        for (String genre : genres) {
            List<Video> selectedVideos = new ArrayList<>(ProcessData.videos);
            selectedVideos.removeIf(video -> !video.getGenres().contains(genre)
                    || user.getHistory().containsKey(video.getTitle()));
            if (!selectedVideos.isEmpty()) {
                return "PopularRecommendation result: " + selectedVideos.get(0).getTitle();
            }
        }
        return "PopularRecommendation cannot be applied!";
    }

    /**
     * Number of current season
     */
    public static String favoriteRecommendation(final String username) {
        User user = ProcessUtils.getUserInstance(username, ProcessData.users);
        if (user == null || !user.getSubscription().equals(Constants.PREMIUM)) {
            return "FavoriteRecommendation cannot be applied!";
        }

        List<Video> videos = new ArrayList<>(ProcessData.videos);
        videos.removeIf(video -> user.getHistory().containsKey(video.getTitle()));

        if (videos.isEmpty()) {
            return "FavoriteRecommendation cannot be applied!";
        }
        String title = ProcessUtils.getFavoriteVideos(videos, Constants.DATABASE,
                Constants.FIRST_VIDEO).get(0);
        return "FavoriteRecommendation result: " + title;
    }

    /**
     * Number of current season
     */
    public static String searchRecommendation(final String username, final String genre) {
        User user = ProcessUtils.getUserInstance(username, ProcessData.users);
        if (user == null || !user.getSubscription().equals(Constants.PREMIUM)) {
            return "SearchRecommendation cannot be applied!";
        }

        List<Video> videos = new ArrayList<>(ProcessData.videos);
        videos.removeIf(video -> !video.getGenres().contains(genre)
                || user.getHistory().containsKey(video.getTitle()));

        if (videos.isEmpty()) {
            return "SearchRecommendation cannot be applied!";
        }
        videos.sort((video1, video2) -> {
            if (video1.averageRating() == null || video2.averageRating() == null) {
                return 0;
            }
            if (video1.averageRating().equals(video2.averageRating())) {
                return video1.getTitle().compareTo(video2.getTitle());
            }
            return video2.averageRating().compareTo(video1.averageRating());
        });

        List<String> titles = new ArrayList<>();
        for (Video video : videos) {
            titles.add(video.getTitle());
        }
        Collections.sort(titles);
        return "SearchRecommendation result: " + titles;
    }
}
