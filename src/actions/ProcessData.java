package actions;

import actor.Actor;
import common.Constants;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ProcessData {
    private final Input input;
    private String message;

    public static List<Actor> actors;
    public static List<User> users;
    public static List<Serial> serials;
    public static List<Video> videos;
    public static List<Movie> movies;

    public ProcessData(final Input input) {
        this.input = input;
        actors = ProcessUtils.transformActorInput(input.getActors());
        users = ProcessUtils.transformUserInput(input.getUsers());
        movies = ProcessUtils.transformMovieInput(input.getMovies());
        serials = ProcessUtils.transformSerialInput(input.getSerials());
        videos = new ArrayList<>();
        videos.addAll(movies);
        videos.addAll(serials);
    }

    /**
     * Number of current season
     */
    public void processActions(final JSONArray arrayResult, final Writer fileWriter)
            throws IOException {
        for (ActionInputData action : input.getCommands()) {
            message = "";
            if (action.getActionType().equals(Constants.COMMAND)) {
                switch (action.getType()) {
                    case Constants.FAVORITE -> {
                        message = Commands.favorite(action.getUsername(), action.getTitle());
                    }
                    case Constants.VIEW -> {
                        message = Commands.view(action.getUsername(), action.getTitle());
                    }
                    case Constants.RATING -> {
                        if (action.getSeasonNumber() > 0) {
                            message = Commands.rating(action.getUsername(), action.getTitle(),
                                    action.getSeasonNumber(), action.getGrade());
                        } else {
                            message = Commands.rating(action.getUsername(), action.getTitle(),
                                    action.getGrade());
                        }
                    }
                    default -> {
                    }
                }
            }
            if (action.getActionType().equals(Constants.QUERY)) {
                switch (action.getObjectType()) {
                    case Constants.ACTORS -> {
                        switch (action.getCriteria()) {
                            case Constants.AVERAGE -> {
                                message = Queries.actorAverage(action.getNumber(),
                                        action.getSortType());
                            }
                            case Constants.AWARDS -> {
                                message = Queries.actorAwards(action.getSortType(),
                                        action.getFilters());
                            }
                            case Constants.FILTER_DESCRIPTION -> {
                                message = Queries.actorDescription(action.getSortType(),
                                        action.getFilters());
                            }
                            default -> {
                            }
                        }
                    }
                    case Constants.MOVIES, Constants.SHOWS -> {
                        switch (action.getCriteria()) {
                            case Constants.RATINGS -> {
                                message = Queries.videoRatings(action.getNumber(),
                                        action.getSortType(), action.getFilters(),
                                        action.getObjectType());
                            }
                            case Constants.FAVORITE -> {
                                message = Queries.favoriteVideos(action.getNumber(),
                                        action.getSortType(), action.getFilters(),
                                        action.getObjectType());
                            }
                            case Constants.LONGEST -> {
                                message = Queries.longestVideos(action.getNumber(),
                                        action.getSortType(), action.getFilters(),
                                        action.getObjectType());
                            }
                            case Constants.MOST_VIEWED -> {
                                message = Queries.mostViewedVideos(action.getNumber(),
                                        action.getSortType(), action.getFilters(),
                                        action.getObjectType());
                            }
                            default -> {
                            }
                        }
                    }
                    case Constants.USERS -> {
                        if (Constants.NUM_RATINGS.equals(action.getCriteria())) {
                            message = Queries.mostActiveUsers(action.getNumber(),
                                    action.getSortType());
                        }
                    }
                    default -> {

                    }
                }
            }
            if (action.getActionType().equals(Constants.RECOMMENDATION)) {
                switch (action.getType()) {
                    case Constants.STANDARD -> {
                        message = Recommendations.standardRecommendation(action.getUsername());
                    }
                    case Constants.BEST_UNSEEN -> {
                        message = Recommendations.bestUnseenRecommendation(action.getUsername());
                    }
                    case Constants.POPULAR -> {
                        message = Recommendations.popularRecommendation(action.getUsername());
                    }
                    case Constants.FAVORITE -> {
                        message = Recommendations.favoriteRecommendation(action.getUsername());
                    }
                    case Constants.SEARCH -> {
                        message = Recommendations.searchRecommendation(action.getUsername(),
                                action.getGenre());
                    }
                    default -> {

                    }
                }
            }
            JSONObject object = fileWriter.writeFile(action.getActionId(), "", message);
            arrayResult.add(object);
        }
    }
}
