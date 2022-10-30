package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.FIRST_APPOINTMENT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CLIENTTAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_INCOME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MONTHLY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PLANTAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_RISKTAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LOCATION_FIELD_APPOINTMENT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RISKTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.FindPredicate;
import seedu.address.model.person.IncomeLevel;
import seedu.address.model.person.Monthly;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NormalTagContainsKeywordsPredicate;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RiskTagContainsKeywordsPredicate;
import seedu.address.model.tag.ClientTag;
import seedu.address.model.tag.PlanTag;
import seedu.address.model.tag.RiskTag;
import seedu.address.model.tag.Tag;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        List<FindPredicate> predicates = new ArrayList<>();
        predicates.add(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        FindCommand expectedFindCommand = new FindCommand(predicates);
        assertParseSuccess(parser, " " + PREFIX_NAME.getPrefix() + "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + PREFIX_NAME.getPrefix() + "Alice  \t \n Bob", expectedFindCommand);
    }

    @Test
    public void parse_validArgs_returnsFindRiskTagCommand() {
        // no leading and trailing whitespaces
        List<FindPredicate> predicates = new ArrayList<>();
        predicates.add(new RiskTagContainsKeywordsPredicate(Arrays.asList("high", "medium")));
        FindCommand expectedFindCommand = new FindCommand(predicates);
        assertParseSuccess(parser , " " + PREFIX_RISKTAG.getPrefix()
                + "high medium", expectedFindCommand);
        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + PREFIX_RISKTAG.getPrefix()
                + "high \n \t medium" , expectedFindCommand);
    }

    @Test
    public void parse_validArgs_returnsFindNormalTagCommand() {
        // no leading and trailing whitespaces
        List<FindPredicate> predicates = new ArrayList<>();
        predicates.add(new NormalTagContainsKeywordsPredicate(Arrays.asList("friends",
                "owesMoney")));
        FindCommand expectedFindCommand =
                new FindCommand(predicates);
        assertParseSuccess(parser , " " + PREFIX_TAG.getPrefix()
                + "friends owesMoney", expectedFindCommand);
        // multiple whitespaces between keywords
        assertParseSuccess(parser, " " + PREFIX_TAG.getPrefix()
                + "friends \n \t owesMoney" , expectedFindCommand);
    }

    @Test
    public void parse_noPrefixesPresent_failure() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE)); // invalid name
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, INVALID_RISKTAG_DESC, RiskTag.MESSAGE_CONSTRAINTS); // invalid riskTag
        assertParseFailure(parser, INVALID_PLANTAG_DESC, PlanTag.MESSAGE_CONSTRAINTS); // invalid PlanTag
        assertParseFailure(parser, INVALID_CLIENTTAG_DESC, ClientTag.MESSAGE_CONSTRAINTS); // invalid ClientTag
        assertParseFailure(parser, INVALID_INCOME_DESC, IncomeLevel.MESSAGE_CONSTRAINTS); // invalid income
        assertParseFailure(parser, INVALID_MONTHLY_DESC, Monthly.MESSAGE_CONSTRAINTS); // invalid monthly
        assertParseFailure(parser, INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid phone followed by valid email
        assertParseFailure(parser, INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // valid phone followed by invalid phone. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, PHONE_DESC_BOB + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidPrefixes_failure() {
        //prefix DATE_TIME present will throw exception
        assertParseFailure(parser, FIRST_APPOINTMENT_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        //prefix LOCATION present will throw exception
        assertParseFailure(parser, VALID_LOCATION_FIELD_APPOINTMENT_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        //both DATE_TIME and LOCATION prefixes present throws exception
        assertParseFailure(parser, VALID_LOCATION_FIELD_APPOINTMENT_DESC + FIRST_APPOINTMENT_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

}
