package actions;

import actor.Actor;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import fileio.ActionInputData;
import fileio.Input;
import user.User;
import utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for all of the information in the database
 */
public class Database {
    private final List<ActionInputData> commands;
    private final List<Actor> actors;
    private final List<User> users;
    private final List<Video> videos;
    private final List<Movie> movies;
    private final List<Serial> serials;

    public Database(final Input input) {
        commands = input.getCommands();
        actors = ProcessUtils.transformActorInput(input.getActors());
        users = ProcessUtils.transformUserInput(input.getUsers());
        movies = ProcessUtils.transformMovieInput(input.getMovies());
        serials = ProcessUtils.transformSerialInput(input.getSerials());
        videos = new ArrayList<>();
        videos.addAll(movies);
        videos.addAll(serials);
    }

    /**
     * List of commands
     */
    public List<ActionInputData> commandsDB() {
        return commands;
    }

    /**
     * List of actors
     */
    public List<Actor> actorsDB() {
        return actors;
    }

    /**
     * List of users
     */
    public List<User> usersDB() {
        return users;
    }

    /**
     * List of videos
     */
    public List<Video> videosDB() {
        return videos;
    }

    /**
     * List of movies
     */
    public List<Movie> moviesDB() {
        return movies;
    }

    /**
     * List of serials
     */
    public List<Serial> serialsDB() {
        return serials;
    }
}
