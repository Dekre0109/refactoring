package theater;

/**
 * Class representing a performance of a play.
 */
public class Performance {

    private final String playID;
    private final int audience;

    /**
     * Create a new performance for the play with the given ID and audience size.
     *
     * @param playID   the play ID for this performance
     * @param audience the audience size for this performance
     */
    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    /**
     * Return the ID of the play for this performance.
     *
     * @return the play ID
     */
    public String getPlayID() {
        return this.playID;
    }

    /**
     * Return the audience size for this performance.
     *
     * @return the audience size
     */
    public int getAudience() {
        return this.audience;
    }
}

