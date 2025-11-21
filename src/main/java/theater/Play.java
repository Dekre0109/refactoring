package theater;

/**
 * Class representing a play with a name and type.
 */
public class Play {

    private final String name;
    private final String type;

    /**
     * Create a new play with the given name and type.
     *
     * @param name the name of the play
     * @param type the type of the play (e.g., tragedy or comedy)
     */
    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Return the name of this play.
     *
     * @return the play name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the type of this play.
     *
     * @return the play type
     */
    public String getType() {
        return this.type;
    }
}

