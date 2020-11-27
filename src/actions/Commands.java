package actions;

import entertainment.Movie;
import entertainment.Season;
import entertainment.Serial;
import entertainment.Video;
import fileio.ActionInputData;
import user.User;
import utils.ProcessUtils;

import java.util.Map;

public final class Commands {
    private final Database database;

    public Commands(final Database database) {
        this.database = database;
    }

    /**
     * Adds video to user's favorites list, if it is already seen
     * @param action instance of action from input
     * @return message String result of action
     */
    public String favorite(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        Video video = ProcessUtils.getVideoInstance(action.getTitle(), database.videosDB());

        /* If the video is not seen or
        * already in the favorite list -> error */
        if (user.getFavoriteMovies().contains(video.getTitle())) {
            return "error -> " + video.getTitle()
                    + " is already in favourite list";
        }
        if (!user.getHistory().containsKey(video.getTitle())) {
            return "error -> " + video.getTitle() + " is not seen";
        } else {
            /* Adds video to favorite list */
            user.getFavoriteMovies().add(video.getTitle());
            return "success -> " + video.getTitle()
                    + " was added as favourite";
        }
    }

    /**
     * Marks video as viewed by adding it to user's history
     * @param action instance of action from input
     * @return message String result of action
     */
    public String view(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        Video video = ProcessUtils.getVideoInstance(action.getTitle(), database.videosDB());

        int noViews;
        /* If the video is already seen, increase number of views */
        if (user.getHistory().containsKey(video.getTitle())) {
            noViews = user.getHistory().get(video.getTitle());
            noViews++;
            user.getHistory().replace(video.getTitle(), noViews);
        } else {
            /* Otherwise, add it to history */
            noViews = 1;
            user.getHistory().put(video.getTitle(), noViews);
        }
        return "success -> " + video.getTitle()
                + " was viewed with total views of " + noViews;
    }

    /**
     * Gives rating to an already watched video
     * @param action instance of action from input
     * @return message String result of action
     */
    public String rating(final ActionInputData action) {
        User user = ProcessUtils.getUserInstance(action.getUsername(), database.usersDB());
        Video video = ProcessUtils.getVideoInstance(action.getTitle(), database.videosDB());

        /* If the video is not seen or
         * already rated -> error */
        if (!user.getHistory().containsKey(video.getTitle())) {
            return "error -> " + video.getTitle() + " is not seen";
        }

        /* For every user, keep a map of rated videos
        * entry values represent season number for serials
        * or 0 for movies */
        if (user.getRatedVideos().containsKey(video.getTitle())) {
            /* Verify is movie or season is already rated */
            for (Map.Entry<String, Integer> entry : user.getRatedVideos().entrySet()) {
                if (entry.getKey().equals(video.getTitle())
                        && (entry.getValue().equals(action.getSeasonNumber())
                        || action.getSeasonNumber() < 0)) {
                    return "error -> " + video.getTitle() + " has been already rated";
                }
            }
        }

        /* Give rating to a season */
        if (action.getSeasonNumber() != 0) {
            /* Get season instance and add rating to ratings list */
            Serial serial = ProcessUtils.getSerialInstance(action.getTitle(),
                    database.serialsDB());
            Season season = serial.getSeasons().get(action.getSeasonNumber() - 1);

            season.getRatings().add(action.getGrade());
            user.getRatedVideos().put(serial.getTitle(), action.getSeasonNumber());

            return "success -> " + serial.getTitle() + " was rated with "
                    + action.getGrade() + " by " + user.getUsername();
        } else {
            /* Give rating to a movie
             * Get movie instance and add rating to ratings list */
            Movie movie = ProcessUtils.getMovieInstance(action.getTitle(), database.moviesDB());
            movie.getRatings().add(action.getGrade());
            user.getRatedVideos().put(movie.getTitle(), 0);

            return "success -> " + movie.getTitle() + " was rated with "
                    + action.getGrade() + " by " + user.getUsername();
        }
    }
}
