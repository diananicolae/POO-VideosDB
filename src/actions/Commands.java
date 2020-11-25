package actions;

import entertainment.Movie;
import entertainment.Season;
import entertainment.Serial;
import entertainment.Video;
import fileio.ActionInputData;
import user.User;

import java.util.Map;

public final class Commands {
    private final Database database;

    public Commands(final Database database) {
        this.database = database;
    }

    /**
     * Adds video to user's favorites list, if it is already seen
     */
    public String favorite(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        Video video = ProcessUtils.getVideoInstance(action.getTitle(), database.videosDB());

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

    /**
     * Marks video as viewed by adding it to user's history
     */
    public String view(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        Video video = ProcessUtils.getVideoInstance(action.getTitle(), database.videosDB());

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

    /**
     * Gives rating to an already watched video
     */
    public String rating(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        Video video = ProcessUtils.getVideoInstance(action.getTitle(), database.videosDB());

        if (!user.getHistory().containsKey(video.getTitle())) {
            return "error -> " + video.getTitle() + " is not seen";
        }
        if (user.getRatedVideos().containsKey(video.getTitle())) {
            for (Map.Entry<String, Integer> entry : user.getRatedVideos().entrySet()) {
                if (entry.getKey().equals(video.getTitle())
                        && (entry.getValue().equals(action.getSeasonNumber())
                        || action.getSeasonNumber() < 0)) {
                    return "error -> " + video.getTitle() + " has been already rated";
                }
            }
        }

        // give rating to a season
        if (action.getSeasonNumber() != 0) {
            Serial serial = ProcessUtils.getSerialInstance(action.getTitle(),
                    database.serialsDB());
            Season season = serial.getSeasons().get(action.getSeasonNumber() - 1);

            season.getRatings().add(action.getGrade());
            user.getRatedVideos().put(serial.getTitle(), action.getSeasonNumber());

            return "success -> " + serial.getTitle() + " was rated with "
                    + action.getGrade() + " by " + user.getUsername();
        } else {
            // give rating to a movie
            Movie movie = ProcessUtils.getMovieInstance(action.getTitle(), database.moviesDB());
            movie.getRatings().add(action.getGrade());
            user.getRatedVideos().put(movie.getTitle(), 0);

            return "success -> " + movie.getTitle() + " was rated with "
                    + action.getGrade() + " by " + user.getUsername();
        }


    }
}
