package seedu.address.model.restaurant;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents a Restaurant's weblink in the food diary.
 * Guarantees: immutable; is valid as declared in {@link #isValidWeblinkString(String)}
 */
public class Weblink {

    public static final String NO_WEBLINK_STRING = "No weblink added";
    public static final String INVALID_URL_MESSAGE = "Weblink entered is not found. Please enter a correct weblink";
    private static final String SPECIAL_CHARACTERS = "!#$%&'*+/=?`{|}~^.-";
    public static final String MESSAGE_CONSTRAINTS = "Weblinks should be of the format https://local-part.domain "
            + "and adhere to the following constraints:\n"
            + "1. The local-part should only contain alphanumeric characters and these special characters, excluding "
            + "the parentheses, (" + SPECIAL_CHARACTERS + ") .\n"
            + "2. This is followed by a '.' and then a domain name. "
            + "The domain name must:\n"
            + "    - be at least 2 characters long\n"
            + "    - start and end with alphanumeric characters\n"
            + "    - consist of alphanumeric characters, a period or a hyphen for the characters in between, if any.";
    // alphanumeric and special characters
    private static final String LOCAL_PART_REGEX = "[\\w" + SPECIAL_CHARACTERS + "]+";
    private static final String DOMAIN_FIRST_CHARACTER_REGEX = "[^\\W_]"; // alphanumeric characters except underscore
    private static final String DOMAIN_MIDDLE_REGEX = "[a-zA-Z0-9.-]*"; // alphanumeric, period and hyphen
    private static final String DOMAIN_LAST_CHARACTER_REGEX = "[^\\W_]$";
    public static final String VALIDATION_REGEX = "^(http:\\/\\/|https:\\/\\/)" + LOCAL_PART_REGEX + "."
            + DOMAIN_FIRST_CHARACTER_REGEX + DOMAIN_MIDDLE_REGEX + DOMAIN_LAST_CHARACTER_REGEX;

    public final String value;

    /**
     * Constructs an {@code Weblink}.
     *
     * @param weblink A valid email address.
     */
    public Weblink(String weblink) {
        requireNonNull(weblink);
        checkArgument(isValidWeblinkString(weblink), MESSAGE_CONSTRAINTS);
        value = weblink;
    }

    /**
     * Returns if a given string is a valid weblink.
     */
    public static boolean isValidWeblinkString(String test) {
        return test.matches(VALIDATION_REGEX) || test.matches(NO_WEBLINK_STRING);
    }

    /**
     * Checks if a given string is a valid weblink URL, ie. HTTP response code should not be 400 and above
     * The only acceptable malformed Url is the default placeholder for no weblinks, NO_WEBLINK_STRING
     */
    public static boolean isValidWeblinkUrl(String urlString) {
        try {
            URL u = new URL(urlString);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("HEAD");
            huc.connect();
            return huc.getResponseCode() < 400;
        } catch (MalformedURLException e) {
            return urlString.equals(NO_WEBLINK_STRING);
        } catch (IOException e) {
            return false;
        }
    }

    public static Weblink makeDefaultWeblink() {
        return new Weblink(NO_WEBLINK_STRING);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Weblink // instanceof handles nulls
                && value.equals(((Weblink) other).value)); // state check
    }


    @Override
    public int hashCode() {
        return value.hashCode();
    }

}