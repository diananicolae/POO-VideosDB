package actions;

import actor.Actor;
import common.Constants;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import user.User;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class ProcessUtils {
    private ProcessUtils() {

    }

    public static User getUserInstance(final String username, final List<User> users) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static Actor getActorInstance(final String name, final List<Actor> actors) {
        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }
        return null;
    }

    public static Video getVideoInstance(final String title, final List<Video> videos) {
        for (Video video : videos) {
            if (video.getTitle().equals(title)) {
                return video;
            }
        }
        return null;
    }

    public static Movie getMovieInstance(final String title, final List<Movie> movies) {
        for (Movie movie : movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    public static Serial getSerialInstance(final String title, final List<Serial> serials) {
        for (Serial serial : serials) {
            if (serial.getTitle().equals(title)) {
                return serial;
            }
        }
        return null;
    }

    public static List<User> transformUserInput(final List<UserInputData> input) {
        List<User> list = new ArrayList<>();
        for (UserInputData user : input) {
            User newUser = new User(user.getUsername(), user.getSubscriptionType(),
                    user.getHistory(), user.getFavoriteMovies());
            list.add(newUser);
        }
        return list;
    }

    public static List<Actor> transformActorInput(final List<ActorInputData> input) {
        List<Actor> list = new ArrayList<>();
        for (ActorInputData actor : input) {
            Actor newActor = new Actor(actor.getName(), actor.getCareerDescription(),
                    actor.getFilmography(), actor.getAwards());
            list.add(newActor);
        }
        return list;
    }

    public static List<Movie> transformMovieInput(final List<MovieInputData> input) {
        List<Movie> list = new ArrayList<>();
        for (MovieInputData movie : input) {
            Movie newMovie = new Movie(movie.getTitle(), movie.getCast(), movie.getGenres(),
                    movie.getYear(), movie.getDuration());
            list.add(newMovie);
        }
        return list;
    }

    public static List<Serial> transformSerialInput(final List<SerialInputData> input) {
        List<Serial> list = new ArrayList<>();
        for (SerialInputData serial : input) {
            Serial newSerial = new Serial(serial.getTitle(), serial.getCast(), serial.getGenres(),
                    serial.getNumberSeason(), serial.getSeasons(), serial.getYear());
            list.add(newSerial);
        }
        return list;
    }

    public static List<Actor> getFilteredActors(final List<List<String>> filters) {
        List<Actor> filteredList = new ArrayList<>(ProcessData.actors);

        if (filters.get(Constants.WORDS_FILTER) != null) {
            for (String word : filters.get(Constants.WORDS_FILTER)) {
                filteredList.removeIf(actor -> !actor.getCareerDescription().toLowerCase().contains(word));
            }
        }
        if (filters.get(Constants.AWARDS_FILTER) != null) {
            for (String award : filters.get(Constants.AWARDS_FILTER)) {
                filteredList.removeIf(actor ->
                        !actor.getAwards().containsKey(Utils.stringToAwards(award)));
            }
        }
        return filteredList;
    }

    public static String getActorsList(final List<Actor> actors) {
        if (actors.isEmpty()) {
            return "Query result: []";
        }
        List<String> actorNames = new ArrayList<>();
        for (Actor actor : actors) {
            actorNames.add(actor.getName());
        }
        return "Query result: " + actorNames;
    }

    public static List<Video> getFilteredVideos(final List<List<String>> filters,
                                                final String objectType) {
        List<Video> filteredList = new ArrayList<>();

        switch (objectType) {
            case Constants.MOVIES -> {
                filteredList.addAll(ProcessData.movies);
            }
            case Constants.SHOWS -> {
                filteredList.addAll(ProcessData.serials);
            }
            case Constants.VIDEOS -> {
                filteredList.addAll(ProcessData.videos);
            }
        }

        if (filters.get(Constants.YEAR_FILTER) != null) {
            for (String year : filters.get(Constants.YEAR_FILTER)) {
                if (year != null) {
                    filteredList.removeIf(video -> video.getYear() != Integer.parseInt(year));
                }
            }
        }

        if (filters.get(Constants.GENRE_FILTER) != null) {
            for (String genre : filters.get(Constants.GENRE_FILTER)) {
                if (genre != null) {
                    filteredList.removeIf(video -> !video.getGenres().contains(genre));
                }
            }
        }
        return filteredList;
    }

    public static List<String> getListFromMap(final Map<String, Double> map, final String sortType,
                                              final int number) {
        List<Map.Entry<String, Double>> mapList = new LinkedList<>(map.entrySet());
        List<String> titles = new ArrayList<>();

        mapList.sort((entry1, entry2) -> {
            switch (sortType) {
                case Constants.ASC -> {
                    if (entry1.getValue().equals(entry2.getValue())) {
                        return entry1.getKey().compareTo(entry2.getKey());
                    }
                    return entry1.getValue().compareTo(entry2.getValue());
                }
                case Constants.DESC -> {
                    if (entry1.getValue().equals(entry2.getValue())) {
                        return entry2.getKey().compareTo(entry1.getKey());
                    }
                    return entry2.getValue().compareTo(entry1.getValue());
                }
                default -> {
                    return 0;
                }
            }
        });

        for (Map.Entry<String, Double> entry : mapList) {
            titles.add(entry.getKey());
        }
        if (number < titles.size()) {
            titles = titles.subList(0, number);
        }

        return titles;
    }

}
