package actions;

import actor.Actor;
import common.Constants;
import entertainment.Video;
import fileio.ActionInputData;
import user.User;
import utils.ProcessUtils;

import java.util.*;

public final class Queries {
    private final Database database;

    public Queries(final Database database) {
        this.database = database;
    }

    /**
     * Returns list of actors sorted by the average rating
     * of their filmography
     */
    public String actorAverage(final ActionInputData action) {
        Map<String, Double> averageMap = new HashMap<>();

        /* Determine the average of every actor in the database */
        for (Actor actor : database.actorsDB()) {
            double actorAverage = 0.0;
            int ratedFilms = 0;

            /* For every video in the actor's filmography
            * modify actor's average rating*/
            for (String film : actor.getFilmography()) {
                Video video = ProcessUtils.getVideoInstance(film, database.videosDB());
                if (video == null || video.averageRating() == null) {
                    continue;
                }
                actorAverage += video.averageRating();
                ratedFilms++;
            }
            /* Skip the actor if no video from the filmography is rated */
            if (ratedFilms == 0) {
                continue;
            }
            actorAverage /= ratedFilms;
            averageMap.put(actor.getName(), actorAverage);
        }

        List<String> actorNames = ProcessUtils.getListFromMap(averageMap,
                action.getSortType(), action.getNumber());
        return "Query result: " + actorNames;
    }

    /**
     * Returns list of actors filtered by wanted awards and
     * sorted by the total number of awards
     */
    public String actorAwards(final ActionInputData action) {
        List<Actor> actors = ProcessUtils.getFilteredActors(action.getFilters(),
                database);

        actors.sort((actor1, actor2) -> {
            switch (action.getSortType()) {
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
     * Returns list of actors filtered by wanted words and
     * sorted alphabetically
     */
    public String actorDescription(final ActionInputData action) {
        List<Actor> actors = ProcessUtils.getFilteredActors(action.getFilters(),
                database);
        actors.sort(Comparator.comparing(Actor::getName));
        if (action.getSortType().equals(Constants.DESC)) {
            Collections.reverse(actors);
        }
        return ProcessUtils.getActorsList(actors);
    }

    /**
     * Returns titles list of videos sorted by rating
     */
    public String videoRatings(final ActionInputData action) {
        List<Video> videos = ProcessUtils.getFilteredVideos(action.getFilters(),
                action.getObjectType(), database);
        videos.removeIf(video -> video.averageRating() == null);
        Map<String, Double> ratingMap = new HashMap<>();

        for (Video video : videos) {
            ratingMap.put(video.getTitle(), video.averageRating());
        }
        List<String> videoTitles = ProcessUtils.getListFromMap(ratingMap,
                action.getSortType(), action.getNumber());
        return "Query result: " + videoTitles;

    }

    /**
     * Returns titles list of videos sorted by the number of appearances
     * in users' favorite videos
     */
    public String favoriteVideos(final ActionInputData action) {
        List<Video> videos = ProcessUtils.getFilteredVideos(action.getFilters(),
                action.getObjectType(), database);
        List<String> videoTitles = ProcessUtils.getFavoriteVideos(videos,
                action.getSortType(), action.getNumber(), database);
        return "Query result: " + videoTitles;
    }

    /**
     * Returns titles list of videos sorted by duration
     */
    public String longestVideos(final ActionInputData action) {
        List<Video> videos = ProcessUtils.getFilteredVideos(action.getFilters(),
                action.getObjectType(), database);
        Map<String, Double> durationMap = new HashMap<>();

        for (Video video : videos) {
            durationMap.put(video.getTitle(), (double) video.getDuration());
        }
        List<String> videoTitles = ProcessUtils.getListFromMap(durationMap,
                action.getSortType(), action.getNumber());
        return "Query result: " + videoTitles;

    }

    /**
     * Returns titles list of videos sorted by number of views
     */
    public String mostViewedVideos(final ActionInputData action) {
        List<Video> videos = ProcessUtils.getFilteredVideos(action.getFilters(),
                action.getObjectType(), database);
        Map<String, Double> viewsMap = ProcessUtils.getViewsMap(videos, database);

        List<String> videoTitles = ProcessUtils.getListFromMap(viewsMap,
                action.getSortType(), action.getNumber());
        return "Query result: " + videoTitles;

    }

    /**
     * Returns usernames list of users sorted by number
     * of given ratings
     */
    public String mostActiveUsers(final ActionInputData action) {
        Map<String, Double> usersMap = new HashMap<>();

        for (User user : database.usersDB()) {
            if (!user.getRatedVideos().isEmpty()) {
                usersMap.put(user.getUsername(), (double) user.getRatedVideos().size());
            }
        }

        List<String> usernames = ProcessUtils.getListFromMap(usersMap,
                action.getSortType(), action.getNumber());
        return "Query result: " + usernames;
    }
}
