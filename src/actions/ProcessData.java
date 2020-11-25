package actions;

import common.Constants;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

public final class ProcessData {
    private final Database database;
    private final Commands commands;
    private final Queries queries;
    private final Recommendations recommendations;

    public ProcessData(final Database database) {
        this.database = database;
        this.commands = new Commands(database);
        this.queries = new Queries(database);
        this.recommendations = new Recommendations(database);
    }

    /**
     * Number of current season
     */
    public void process(final JSONArray arrayResult, final Writer fileWriter)
            throws IOException {
        for (ActionInputData action : database.commandsDB()) {
            String message = "";
            String field = "";
            if (action.getActionType().equals(Constants.COMMAND)) {
                switch (action.getType()) {
                    case Constants.FAVORITE -> {
                        message = commands.favorite(action);
                    }
                    case Constants.VIEW -> {
                        message = commands.view(action);
                    }
                    case Constants.RATING -> {
                        message = commands.rating(action);
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
                                message = queries.actorAverage(action);
                            }
                            case Constants.AWARDS -> {
                                message = queries.actorAwards(action);
                            }
                            case Constants.FILTER_DESCRIPTION -> {
                                message = queries.actorDescription(action);
                            }
                            default -> {
                            }
                        }
                    }
                    case Constants.MOVIES, Constants.SHOWS -> {
                        switch (action.getCriteria()) {
                            case Constants.RATINGS -> {
                                message = queries.videoRatings(action);
                            }
                            case Constants.FAVORITE -> {
                                message = queries.favoriteVideos(action);
                            }
                            case Constants.LONGEST -> {
                                message = queries.longestVideos(action);
                            }
                            case Constants.MOST_VIEWED -> {
                                message = queries.mostViewedVideos(action);
                            }
                            default -> {
                            }
                        }
                    }
                    case Constants.USERS -> {
                        if (Constants.NUM_RATINGS.equals(action.getCriteria())) {
                            message = queries.mostActiveUsers(action);
                        }
                    }
                    default -> {

                    }
                }
            }
            if (action.getActionType().equals(Constants.RECOMMENDATION)) {
                switch (action.getType()) {
                    case Constants.STANDARD -> {
                        message = recommendations.standardRecommendation(action);
                    }
                    case Constants.BEST_UNSEEN -> {
                        message = recommendations.bestUnseenRecommendation(action);
                    }
                    case Constants.POPULAR -> {
                        message = recommendations.popularRecommendation(action);
                    }
                    case Constants.FAVORITE -> {
                        message = recommendations.favoriteRecommendation(action);
                    }
                    case Constants.SEARCH -> {
                        message = recommendations.searchRecommendation(action);
                    }
                    default -> {

                    }
                }
            }
            JSONObject object = fileWriter.writeFile(action.getActionId(), field, message);
            arrayResult.add(object);
        }
    }
}
