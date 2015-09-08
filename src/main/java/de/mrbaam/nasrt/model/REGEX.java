package de.mrbaam.nasrt.model;

/**
 * Created by mrbaam on 28.08.2015.
 * @author mrbaam
 */
public final class REGEX {
    /** Regular expression that fits for a default episode numeration with season and episode number, e.g. *.S01E01.* */
    public static final String DEFAULT_EPISODE = "\\W([Ss]\\d+)?[Ee]\\d+";
    /** Regular expression that fits for the title of a season. */
    public static final String SEASON_TITLE = "[Ss](taffel)\\s\\d+";

    /** Regular expression that fits for all unsupported signs. */
    public static final String ALL_UNSUPPORTED_SIGNS = "[^a-zA-Z0-9ÄäÖöÜüß]";
    /** Regular expression that fits for one or more following whitespaces. */
    public static final String WHITESPACES = "\\s+";


    private REGEX() {}
}
