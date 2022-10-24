package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.person.Person.MAXIMUM_NUM_OF_APPOINTMENTS;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.util.MaximumSortedList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.DateTime;
import seedu.address.model.person.Email;
import seedu.address.model.person.IncomeLevel;
import seedu.address.model.person.Location;
import seedu.address.model.person.Monthly;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.RiskTag;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        requireNonNull(oneBasedIndex);
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code personAppointmentIndex} into an {@code Index} and returns the appointment index.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseAppointmentIndex(String personAppointmentIndex) throws ParseException {
        requireNonNull(personAppointmentIndex);
        String trimmedAppointmentIndex = personAppointmentIndex.trim();
        String[] splitStr = trimmedAppointmentIndex.split("\\.");

        if (splitStr.length != 2 || !StringUtil.isNonZeroUnsignedInteger(splitStr[1])) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(splitStr[1]));
    }

    /**
     * Parses {@code personAppointmentIndex} into an {@code Index} and returns the person index.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parsePersonIndex(String personAppointmentIndex) throws ParseException {
        requireNonNull(personAppointmentIndex);
        String trimmedAppointmentIndex = personAppointmentIndex.trim();
        String[] splitStr = trimmedAppointmentIndex.split("\\.");

        if (splitStr.length != 2 || !StringUtil.isNonZeroUnsignedInteger(splitStr[0])) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(splitStr[0]));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String dateAndTime} and a {@code String Location} into an {@code Appointment}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Appointment parseAppointment(String dateAndTime, String location) throws ParseException {
        requireNonNull(dateAndTime);
        requireNonNull(location);
        DateTime appointmentDateTime = parseDateTime(dateAndTime);
        Location appointmentLocation = parseLocation(location);
        if (!Appointment.isValidAppointment(appointmentDateTime, appointmentLocation)) {
            throw new ParseException(Appointment.MESSAGE_CONSTRAINTS);
        }
        return new Appointment(appointmentDateTime, appointmentLocation);
    }

    /**
     * Parses {@code Collection<String> datesAndTimes} into a {@code Set<Appointment>}.
     */
    public static MaximumSortedList<Appointment> parseAppointmentsIntoSortedList(Collection<Appointment> appointments) {
        requireNonNull(appointments);
        final MaximumSortedList<Appointment> appointmentSet =
                new MaximumSortedList<>(MAXIMUM_NUM_OF_APPOINTMENTS);
        for (Appointment appointment : appointments) {
            appointmentSet.add(appointment);
        }
        return appointmentSet;
    }

    /**
     * Parses a {@code String dateAndTime} into an {@code DateTime}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static DateTime parseDateTime(String dateAndTime) {
        requireNonNull(dateAndTime);
        String trimmedDateAndTime = dateAndTime.trim();
        LocalDateTime localDateTime = DateTimeParser.parseLocalDateTimeFromString(trimmedDateAndTime);
        return new DateTime(localDateTime);
    }
    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Location parseLocation(String location) throws ParseException {
        requireNonNull(location);
        String trimmedLocation = location.trim();
        if (!Location.isValidLocation(trimmedLocation)) {
            throw new ParseException(Location.MESSAGE_CONSTRAINTS);
        }
        return new Location(trimmedLocation);
    }

    /**
     * Parses a {@code String income} into an {@code IncomeLevel}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static IncomeLevel parseIncomeLevel(String income) throws ParseException {
        requireNonNull(income);
        String trimmedIncome = income.trim();
        if (!IncomeLevel.isValidIncome(income)) {
            throw new ParseException(IncomeLevel.MESSAGE_CONSTRAINTS);
        }
        return new IncomeLevel(trimmedIncome);
    }

    /**
     * Parses a {@code String monthly} into an {@code Monthly}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Monthly parseMonthly(String monthly) throws ParseException {
        requireNonNull(monthly);
        String trimmedMonthly = monthly.trim();
        if (!Monthly.isValidMonthly(monthly)) {
            throw new ParseException(Monthly.MESSAGE_CONSTRAINTS);
        }
        return new Monthly(trimmedMonthly);
    }

    /**
     * Parses a {@code String dateAndTime} into an {@code DateTime}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static RiskTag parseRiskTag(String riskTag) throws ParseException {
        requireNonNull(riskTag);
        String trimmedRiskTag = riskTag.trim();
        if (!RiskTag.isValidRiskTagName(trimmedRiskTag)) {
            throw new ParseException(RiskTag.MESSAGE_CONSTRAINTS);
        }
        return new RiskTag(trimmedRiskTag);
    }


    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
