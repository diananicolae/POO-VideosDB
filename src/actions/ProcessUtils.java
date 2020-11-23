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

import java.util.*;
import java.util.regex.Pattern;

public final class ProcessUtils {
    private ProcessUtils() {

    }

    /**
     * Number of current season
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
     * Number of current season
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
     * Number of current season
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
     * Number of current season
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
     * Number of current season
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
     * Number of current season
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
     * Number of current season
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
     * Number of current season
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
     * Number of current season
     */
    public static List<Actor> getFilteredActors(final List<List<String>> filters) {
        List<Actor> filteredList = new ArrayList<>(ProcessData.actors);

        if (filters.get(Constants.WORDS_FILTER) != null) {
            for (String word : filters.get(Constants.WORDS_FILTER)) {
                if (word == null) {
                    continue;
                }
                filteredList.removeIf(actor -> !Pattern.compile("[ -]" + word + "[ .,]").
                        matcher(actor.getCareerDescription().toLowerCase()).find());
            }
        }
        if (filters.get(Constants.AWARDS_FILTER) != null) {
            for (String award : filters.get(Constants.AWARDS_FILTER)) {
                if (award == null) {
                    continue;
                }
                filteredList.removeIf(actor ->
                        !actor.getAwards().containsKey(Utils.stringToAwards(award)));
            }
        }
        return filteredList;
    }

    /**
     * Number of current season
     */
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

    /**
     * Number of current season
     */
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
            default -> {
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

    /**
     * Number of current season
     */
    public static List<String> getFavoriteVideos(final List<Video> videos, final String sortType,
                                                 final int number) {
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
        return getListFromMap(favoriteMap, sortType, number);
    }

    /**
     * Number of current season
     */
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
                case Constants.DATABASE -> {
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

    /**
     * Number of current season
     */
    public static Map<String, Double> getViewsMap(final List<Video> videos) {
        Map<String, Double> viewsMap = new HashMap<>();

        for (Video video : videos) {
            double noViews = 0;
            for (User user : ProcessData.users) {
                if (user.getHistory().containsKey(video.getTitle())) {
                    noViews += user.getHistory().get(video.getTitle());
                }
            }
            if (noViews > 0) {
                viewsMap.put(video.getTitle(), noViews);
            }
        }
        return viewsMap;
    }

    /**
     * Number of current season
     */
    public static List<String> genrePopularity(final List<Video> videos) {
        Map<String, Double> genreMap = new HashMap<>();
        Map<String, Double> viewsMap = getViewsMap(videos);

        for (Video video : videos) {
            if (!viewsMap.containsKey(video.getTitle())) {
                continue;
            }
            for (String genre : video.getGenres()) {
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
