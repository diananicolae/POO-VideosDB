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

public class ProcessData {
    private final Input input;
    private String message;
    private User user;
    private Video video;
    private Movie movie;
    private Serial serial;

    private List<Actor> actors;
    private List<User> users;
    private List<Movie> movies;
    private List<Serial> serials;
    private List<Video> videos;

    public ProcessData(Input input) {
        this.input = input;
        users = ProcessUtils.transformUserInput(input.getUsers());
        movies = ProcessUtils.transformMovieInput(input.getMovies());
        serials = ProcessUtils.transformSerialInput(input.getSerials());
        videos = new ArrayList<>();
        videos.addAll(movies);
        videos.addAll(serials);
    }

    public void processActions(JSONArray arrayResult, Writer fileWriter) throws IOException {
        for (ActionInputData action : input.getCommands()) {
            if (action.getActionType().equals(Constants.COMMAND)) {
                switch (action.getType()) {
                    case "favorite" -> {
                        user = ProcessUtils.getUserInstance(action.getUsername(), users);
                        video = ProcessUtils.getVideoInstance(action.getTitle(), videos);
                        message = Commands.Favorite(user, video);
                    }
                    case "view" -> {
                        user = ProcessUtils.getUserInstance(action.getUsername(), users);
                        video = ProcessUtils.getVideoInstance(action.getTitle(), videos);
                        message = Commands.View(user, video);
                    }
                    case "rating" -> {
                        user = ProcessUtils.getUserInstance(action.getUsername(), users);
                        if (action.getSeasonNumber() > 0) {
                            serial = ProcessUtils.getSerialInstance(action.getTitle(), serials);
                            message = Commands.Rating(user, serial, action.getSeasonNumber(),
                                    action.getGrade());
                        } else {
                            movie = ProcessUtils.getMovieInstance(action.getTitle(), movies);
                            message = Commands.Rating(user, movie, action.getGrade());
                        }
                    }
                }
            }
            if (action.getActionType().equals(Constants.QUERY)) {

            }
            JSONObject object = fileWriter.writeFile(action.getActionId(), "", message);
            arrayResult.add(object);
        }
    }
}
