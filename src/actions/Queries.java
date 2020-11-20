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
        List<String> actorNames = ProcessUtils.getListFromMap(averageMap, sortType, number);
        return "Query result: " + actorNames;
    }

    public static String actorAwards(final String sortType,
                                     final List<List<String>> filters) {
        List<Actor> actors = ProcessUtils.getFilteredActors(filters);
        actors.sort((actor1, actor2) -> {
            if (actor1.getAwardsNumber() == actor2.getAwardsNumber()) {
                return actor1.getName().compareTo(actor2.getName());
            }
            return actor1.getAwardsNumber() - actor2.getAwardsNumber();
        });
        return ProcessUtils.getActorsList(sortType, actors);
    }


    public static String actorDescription(final String sortType,
                                          final List<List<String>> filters) {
        List<Actor> actors = ProcessUtils.getFilteredActors(filters);
        actors.sort(Comparator.comparing(Actor::getName));
        return ProcessUtils.getActorsList(sortType, actors);
    }


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

    public static String favoriteVideos(final int number, final String sortType,
                                        final List<List<String>> filters,
                                        final String objectType) {
        List<Video> videos = ProcessUtils.getFilteredVideos(filters, objectType);
        Map<String, Double> favoriteMap = new HashMap<>();

        for (Video video : videos) {
            double favoriteCount = 0;
            for (User user : ProcessData.users) {
                if (user.getFavoriteMovies().contains(video.getTitle())) {
                    favoriteCount++;
                }
            }
            if (favoriteCount != 0) {
                favoriteMap.put(video.getTitle(), favoriteCount);
            }
        }

        if (favoriteMap.isEmpty()) {
            return "Query result: []";
        }
        List<String> videoTitles = ProcessUtils.getListFromMap(favoriteMap, sortType, number);
        return "Query result: " + videoTitles;
    }

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

    public static String mostViewedVideos(final int number, final String sortType,
                                          final List<List<String>> filters,
                                          final String objectType) {
        List<Video> videos = ProcessUtils.getFilteredVideos(filters, objectType);
        Map<String, Double> viewsMap = new HashMap<>();

        for (Video video : videos) {
            double noViews = 0;
            for (User user : ProcessData.users) {
                if (user.getHistory().containsKey(video.getTitle())) {
                    noViews++;
                }
            }
            if (noViews > 0) {
                viewsMap.put(video.getTitle(), noViews);
            }
        }

        if (viewsMap.isEmpty()) {
            return "Query result: []";
        }
        List<String> videoTitles = ProcessUtils.getListFromMap(viewsMap, sortType, number);
        return "Query result: " + videoTitles;

    }

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
