package actions;

import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import user.User;

public final class Commands {
    private Commands() {
    }

    public static String favorite(final String username, final String title) {
        User user = ProcessUtils.getUserInstance(username, ProcessData.users);
        Video video = ProcessUtils.getVideoInstance(title, ProcessData.videos);

        if (user.getFavoriteMovies().contains(video.getTitle())) {
            return "error -> " + video.getTitle()
                    + " is already in favourite list";
        }
        if (user.getHistory().containsKey(video.getTitle())) {
            user.getFavoriteMovies().add(video.getTitle());
            return "success -> " + video.getTitle()
                    + " was added as favourite";
        } else {
            return "error -> " + video.getTitle() + " is not seen";
        }
    }

    public static String view(final String username, final String title) {
        User user = ProcessUtils.getUserInstance(username, ProcessData.users);
        Video video = ProcessUtils.getVideoInstance(title, ProcessData.videos);

        int noViews;
        if (user.getHistory().containsKey(video.getTitle())) {
            noViews = user.getHistory().get(video.getTitle());
            noViews++;
            user.getHistory().replace(video.getTitle(), noViews);
        } else {
            noViews = 1;
            user.getHistory().put(video.getTitle(), noViews);
        }
        return "success -> " + video.getTitle()
                + " was viewed with total views of " + noViews;
    }

    public static String rating(final String username, final String title, final Double rating) {
        User user = ProcessUtils.getUserInstance(username, ProcessData.users);
        Movie movie = ProcessUtils.getMovieInstance(title, ProcessData.movies);

        if (user.getRatedMovies().containsKey(movie.getTitle())) {
            return "error -> " + movie.getTitle() + " has been already rated";
        }
        if (user.getHistory().containsKey(movie.getTitle())) {
            movie.getRatings().add(rating);
            user.getRatedMovies().put(movie.getTitle(), 0);
            return "success -> " + movie.getTitle() + " was rated with "
                    + rating + " by " + user.getUsername();
        } else {
            return "error -> " + movie.getTitle() + " is not seen";
        }
    }

    public static String rating(final String username, final String title,
                                final Integer season, final Double rating) {
        User user = ProcessUtils.getUserInstance(username, ProcessData.users);
        Serial serial = ProcessUtils.getSerialInstance(title, ProcessData.serials);

        if (user.getRatedMovies().containsKey(serial.getTitle())
                && user.getRatedMovies().get(serial.getTitle()).equals(season)) {
            return "error -> " + serial.getTitle() + " has been already rated";
        }
        if (user.getHistory().containsKey(serial.getTitle())) {
            serial.getSeasons().get(season - 1).getRatings().add(rating);
            user.getRatedMovies().put(serial.getTitle(), season);
            return "success -> " + serial.getTitle() + " was rated with "
                    + rating + " by " + user.getUsername();
        } else {
            return "error -> " + serial.getTitle() + " is not seen";
        }
    }
}
