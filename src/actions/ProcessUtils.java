package actions;

import actor.Actor;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import user.User;

import java.util.ArrayList;
import java.util.List;

public class ProcessUtils {
    public static User getUserInstance(String username, List<User> users) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static Actor getActorInstance(String name, List<Actor> actors) {
        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }
        return null;
    }

    public static Video getVideoInstance(String title, List<Video> videos) {
        for (Video video : videos) {
            if (video.getTitle().equals(title)) {
                return video;
            }
        }
        return null;
    }

    public static Movie getMovieInstance(String title, List<Movie> movies) {
        for (Movie movie : movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    public static Serial getSerialInstance(String title, List<Serial> serials) {
        for (Serial serial : serials) {
            if (serial.getTitle().equals(title)) {
                return serial;
            }
        }
        return null;
    }

    public static List<User> transformUserInput(List<UserInputData> input) {
        List<User> list = new ArrayList<>();
        for (UserInputData user : input) {
            User newUser = new User(user.getUsername(), user.getSubscriptionType(),
                    user.getHistory(), user.getFavoriteMovies());
            list.add(newUser);
        }
        return list;
    }

    public static List<Movie> transformMovieInput(List<MovieInputData> input) {
        List<Movie> list = new ArrayList<>();
        for (MovieInputData movie : input) {
            Movie newMovie = new Movie(movie.getTitle(), movie.getCast(), movie.getGenres(),
                    movie.getYear(), movie.getDuration());
            list.add(newMovie);
        }
        return list;
    }

    public static List<Serial> transformSerialInput(List<SerialInputData> input) {
        List<Serial> list = new ArrayList<>();
        for (SerialInputData serial : input) {
            Serial newSerial = new Serial(serial.getTitle(), serial.getCast(), serial.getGenres(),
                    serial.getNumberSeason(), serial.getSeasons(), serial.getYear());
            list.add(newSerial);
        }
        return list;
    }


}
