package utils;

import actions.Database;
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

import java.util.*;
import java.util.regex.Pattern;

public final class ProcessUtils {
    private ProcessUtils() {

    }

    /**
     * Uses the string username and returns the User instance from database
     * @param username user we need the instance of
     * @param users list of users from Database
     */
    public static User getUserInstance(final String username, final List<User> users) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Uses the string title and returns the Video instance from database
     * @param title video we need the instance of
     * @param videos list of videos from Database
     */
    public static Video getVideoInstance(final String title, final List<Video> videos) {
        for (Video video : videos) {
            if (video.getTitle().equals(title)) {
                return video;
            }
        }
        return null;
    }

    /**
     * Uses the string title and returns the Movie instance from database
     * @param title movie we need the instance of
     * @param movies list of movies from Database
     */
    public static Movie getMovieInstance(final String title, final List<Movie> movies) {
        for (Movie movie : movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Uses the string title and returns the Serial instance from database
     * @param title serial we need the instance of
     * @param serials list of serials from Database
     */
    public static Serial getSerialInstance(final String title, final List<Serial> serials) {
        for (Serial serial : serials) {
            if (serial.getTitle().equals(title)) {
                return serial;
            }
        }
        return null;
    }

    /**
     * Transforms the input objects for the database
     * @param input list of UserInputData instances
     * @return final list of users
     */
    public static List<User> transformUserInput(final List<UserInputData> input) {
        List<User> list = new ArrayList<>();
        for (UserInputData user : input) {
            User newUser = new User(user.getUsername(), user.getSubscriptionType(),
                    user.getHistory(), user.getFavoriteMovies());
            list.add(newUser);
        }
        return list;
    }

    /**
     * Transforms the input objects for the database
     * @param input list of ActorInputData instances
     * @return final list of actors
     */
    public static List<Actor> transformActorInput(final List<ActorInputData> input) {
        List<Actor> list = new ArrayList<>();
        for (ActorInputData actor : input) {
            Actor newActor = new Actor(actor.getName(), actor.getCareerDescription(),
                    actor.getFilmography(), actor.getAwards());
            list.add(newActor);
        }
        return list;
    }

    /**
     * Transforms the input objects for the database
     * @param input list of MovieInputData instances
     * @return final list of movies
     */
    public static List<Movie> transformMovieInput(final List<MovieInputData> input) {
        List<Movie> list = new ArrayList<>();
        for (MovieInputData movie : input) {
            Movie newMovie = new Movie(movie.getTitle(), movie.getCast(), movie.getGenres(),
                    movie.getYear(), movie.getDuration());
            list.add(newMovie);
        }
        return list;
    }

    /**
     * Transforms the input objects for the database
     * @param input list of SerialInputData instances
     * @return final list of serials
     */
    public static List<Serial> transformSerialInput(final List<SerialInputData> input) {
        List<Serial> list = new ArrayList<>();
        for (SerialInputData serial : input) {
            Serial newSerial = new Serial(serial.getTitle(), serial.getCast(), serial.getGenres(),
                    serial.getNumberSeason(), serial.getSeasons(), serial.getYear());
            list.add(newSerial);
        }
        return list;
    }

    /**
     * Filters actors by Career Description and Awards
     * Removes Actor instance if the filters don't apply
     * @param filters list of lists of strings containing filters
     * @return list of filtered actors
     */
    public static List<Actor> getFilteredActors(final List<List<String>> filters,
                                                final Database database) {
        List<Actor> filteredList = new ArrayList<>(database.actorsDB());

        if (filters.get(Constants.WORDS_FILTER) != null) {
            /* Perform regex for every word */
            for (String word : filters.get(Constants.WORDS_FILTER)) {
                if (word == null) {
                    continue;
                }
                /* Remove actor if their description doesn't contain the wanted word */
                filteredList.removeIf(actor -> !Pattern.compile("[ -]" + word + "[ .,]").
                        matcher(actor.getCareerDescription().toLowerCase()).find());
            }
        }
        if (filters.get(Constants.AWARDS_FILTER) != null) {
            for (String award : filters.get(Constants.AWARDS_FILTER)) {
                if (award == null) {
                    continue;
                }
                /* Remove actor if they haven't won the wanted award */
                filteredList.removeIf(actor ->
                        !actor.getAwards().containsKey(Utils.stringToAwards(award)));
            }
        }
        return filteredList;
    }

    /**
     * Transforms list of actors to list of actors names
     */
    public static String getActorsList(final List<Actor> actors) {
        List<String> actorNames = new ArrayList<>();
        for (Actor actor : actors) {
            actorNames.add(actor.getName());
        }
        return "Query result: " + actorNames;
    }

    /**
     * Filters videos by Genre and Year
     * Removes Video instance if the filters don't apply
     * @param filters list of lists of strings containing filters
     * @return list of filtered videos
     */
    public static List<Video> getFilteredVideos(final List<List<String>> filters,
                                                final String objectType,
                                                final Database database) {
        List<Video> filteredList = new ArrayList<>();

        /* Select which list is going to be filtered */
        switch (objectType) {
            case Constants.MOVIES -> {
                filteredList.addAll(database.moviesDB());
            }
            case Constants.SHOWS -> {
                filteredList.addAll(database.serialsDB());
            }
            case Constants.VIDEOS -> {
                filteredList.addAll(database.videosDB());
            }
            default -> {
            }
        }

        if (filters.get(Constants.YEAR_FILTER) != null) {
            for (String year : filters.get(Constants.YEAR_FILTER)) {
                if (year != null) {
                    /* Remove video if the release year doesn't match */
                    filteredList.removeIf(video -> video.getYear() != Integer.parseInt(year));
                }
            }
        }

        if (filters.get(Constants.GENRE_FILTER) != null) {
            for (String genre : filters.get(Constants.GENRE_FILTER)) {
                if (genre != null) {
                    /* Remove video if it's not of the wanted genre */
                    filteredList.removeIf(video -> !video.getGenres().contains(genre));
                }
            }
        }
        return filteredList;
    }

    /**
     * Creates map of favorite movies sorted by the number of appearances
     * in every user's list of favorite videos
     * @param sortType the order in which we need to sort
     * @param number the number of elements to be returned
     * @return list of video titles
     */
    public static List<String> getFavoriteVideos(final List<Video> videos, final String sortType,
                                                 final int number, final Database database) {
        Map<String, Double> favoriteMap = new HashMap<>();

        /* For every video, iterate through every user's favorite list */
        for (Video video : videos) {
            double favoriteCount = 0;
            for (User user : database.usersDB()) {
                if (user.getFavoriteMovies().contains(video.getTitle())) {
                    favoriteCount++;
                }
            }
            /* Add entry of type <title, numberOfAppearances> */
            if (favoriteCount != 0) {
                favoriteMap.put(video.getTitle(), favoriteCount);
            }
        }
        return getListFromMap(favoriteMap, sortType, number);
    }

    /**
     * Transforms map to a sorted list of titles
     * @param sortType the order in which we need to sort
     * @param number the number of elements to be returned
     * @return sorted list of video titles / actor names
     */
    public static List<String> getListFromMap(final Map<String, Double> map, final String sortType,
                                              final int number) {
        List<Map.Entry<String, Double>> mapList = new LinkedList<>(map.entrySet());
        List<String> titles = new ArrayList<>();

        /* Sorts the map entries by the given sort type
        * ASC/DESC -> by rating, second criteria is the name
        * DATABASE -> only by rating */
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
                case Constants.DATABASE -> {
                    return entry2.getValue().compareTo(entry1.getValue());
                }
                default -> {
                    return 0;
                }
            }
        });

        /* Returns a list of titles or names */
        for (Map.Entry<String, Double> entry : mapList) {
            titles.add(entry.getKey());
        }
        if (number < titles.size()) {
            titles = titles.subList(0, number);
        }

        return titles;
    }

    /**
     * Creates map of most viewed movies sorted by the number of appearances
     * in every user's video history
     */
    public static Map<String, Double> getViewsMap(final List<Video> videos,
                                                  final Database database) {
        Map<String, Double> viewsMap = new HashMap<>();

        /* For every video, iterate through every user's history */
        for (Video video : videos) {
            double noViews = 0;
            for (User user : database.usersDB()) {
                if (user.getHistory().containsKey(video.getTitle())) {
                    noViews += user.getHistory().get(video.getTitle());
                }
            }
            /* Add entry of type <title, totalNoViews */
            if (noViews > 0) {
                viewsMap.put(video.getTitle(), noViews);
            }
        }
        return viewsMap;
    }

    /**
     * Creates list of most popular genres using the
     * most viewed videos for sorting
     * @return list of genres sorted by popularity
     */
    public static List<String> genrePopularity(final List<Video> videos,
                                               final Database database) {
        Map<String, Double> genreMap = new HashMap<>();
        Map<String, Double> viewsMap = getViewsMap(videos, database);

        /* For every genre of the current video,
        * add an entry using the video's number of views */
        for (Video video : videos) {
            if (!viewsMap.containsKey(video.getTitle())) {
                continue;
            }
            /* Add map entries of type <genreName, totalNoViews> */
            for (String genre : video.getGenres()) {
                /* If the genre already exists in the map
                * increase the number of views */
                if (genreMap.containsKey(genre)) {
                    double noViews = genreMap.get(genre);
                    noViews += viewsMap.get(video.getTitle());
                    genreMap.replace(genre, noViews);
                } else {
                    genreMap.put(genre, viewsMap.get(video.getTitle()));
                }
            }
        }
        return getListFromMap(genreMap, Constants.DATABASE, genreMap.size());
    }
}
