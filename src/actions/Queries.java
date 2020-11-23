package actions;

import actor.Actor;
import common.Constants;
import entertainment.Video;
import user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Queries {
    private Queries() {
    }

    /**
     * Number of current season
     */
    public static String actorAverage(final int number, final String sortType) {
        Map<String, Double> averageMap = new HashMap<>();

        for (Actor actor : ProcessData.actors) {
            double actorAverage = 0.0;
            int ratedFilms = 0;

            for (String film : actor.getFilmography()) {
                if (ProcessUtils.getVideoInstance(film, ProcessData.videos) == null
                        || ProcessUtils.getVideoInstance(film, ProcessData.videos).
                        averageRating() == null) {
                    continue;
                }
                actorAverage += ProcessUtils.getVideoInstance(film,
                        ProcessData.videos).averageRating();
                ratedFilms++;
            }
            if (ratedFilms == 0) {
                continue;
            }
            actorAverage /= ratedFilms;
            averageMap.put(actor.getName(), actorAverage);
        }

        if (averageMap.isEmpty()) {
            return "Query result: []";
        }
        List<String> actorNames = ProcessUtils.getListFromMap(averageMap,
                sortType, number);
        return "Query result: " + actorNames;
    }

    /**
     * Number of current season
     */
    public static String actorAwards(final String sortType,
                                     final List<List<String>> filters) {
        List<Actor> actors = ProcessUtils.getFilteredActors(filters);
        actors.sort((actor1, actor2) -> {
            switch (sortType) {
                case Constants.ASC -> {
                    if (actor1.getAwardsNumber() == actor2.getAwardsNumber()) {
                        return actor1.getName().compareTo(actor2.getName());
                    }
                    return actor1.getAwardsNumber() - actor2.getAwardsNumber();
                }
                case Constants.DESC -> {
                    if (actor1.getAwardsNumber() == actor2.getAwardsNumber()) {
                        return actor2.getName().compareTo(actor1.getName());
                    }
                    return actor2.getAwardsNumber() - actor1.getAwardsNumber();
                }
                default -> {
                    return 0;
                }
            }
        });
        return ProcessUtils.getActorsList(actors);
    }

    /**
     * Number of current season
     */
    public static String actorDescription(final String sortType,
                                          final List<List<String>> filters) {
        List<Actor> actors = ProcessUtils.getFilteredActors(filters);
        actors.sort(Comparator.comparing(Actor::getName));
        if (sortType.equals(Constants.DESC)) {
            Collections.reverse(actors);
        }
        return ProcessUtils.getActorsList(actors);
    }

    /**
     * Number of current season
     */
    public static String videoRatings(final int number, final String sortType,
                                      final List<List<String>> filters, final String objectType) {
        List<Video> videos = ProcessUtils.getFilteredVideos(filters, objectType);
        videos.removeIf(video -> video.averageRating() == null);
        videos.sort(Comparator.comparingDouble(Video::averageRating));

        if (videos.isEmpty()) {
            return "Query result: []";
        }
        if (sortType.equals(Constants.DESC)) {
            Collections.reverse(videos);
        }

        List<String> videoTitles = new ArrayList<>();
        for (Video video : videos) {
            videoTitles.add(video.getTitle());
        }
        if (number < videoTitles.size()) {
            videoTitles = videoTitles.subList(0, number);
        }
        return "Query result: " + videoTitles;
    }

    /**
     * Number of current season
     */
    public static String favoriteVideos(final int number, final String sortType,
                                        final List<List<String>> filters,
                                        final String objectType) {
        List<Video> videos = ProcessUtils.getFilteredVideos(filters, objectType);
        List<String> videoTitles = ProcessUtils.getFavoriteVideos(videos, sortType, number);
        return "Query result: " + videoTitles;
    }

    /**
     * Number of current season
     */
    public static String longestVideos(final int number, final String sortType,
                                       final List<List<String>> filters,
                                       final String objectType) {
        List<Video> videos = ProcessUtils.getFilteredVideos(filters, objectType);
        Map<String, Double> durationMap = new HashMap<>();

        for (Video video : videos) {
            durationMap.put(video.getTitle(), (double) video.getDuration());
        }

        if (durationMap.isEmpty()) {
            return "Query result: []";
        }
        List<String> videoTitles = ProcessUtils.getListFromMap(durationMap, sortType, number);
        return "Query result: " + videoTitles;

    }

    /**
     * Number of current season
     */
    public static String mostViewedVideos(final int number, final String sortType,
                                          final List<List<String>> filters,
                                          final String objectType) {
        List<Video> videos = ProcessUtils.getFilteredVideos(filters, objectType);
        Map<String, Double> viewsMap = ProcessUtils.getViewsMap(videos);

        if (viewsMap.isEmpty()) {
            return "Query result: []";
        }
        List<String> videoTitles = ProcessUtils.getListFromMap(viewsMap, sortType, number);
        return "Query result: " + videoTitles;

    }

    /**
     * Number of current season
     */
    public static String mostActiveUsers(final int number, final String sortType) {
        Map<String, Double> usersMap = new HashMap<>();

        for (User user : ProcessData.users) {
            if (!user.getRatedMovies().isEmpty()) {
                usersMap.put(user.getUsername(), (double) user.getRatedMovies().size());
            }
        }

        if (usersMap.isEmpty()) {
            return "Query result: []";
        }
        List<String> usernames = ProcessUtils.getListFromMap(usersMap, sortType, number);
        return "Query result: " + usernames;
    }
}
