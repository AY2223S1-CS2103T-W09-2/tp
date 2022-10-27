package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLANTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RISKTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.IncomeContainsKeywordsPredicate;
import seedu.address.model.person.MonthlyContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NormalTagContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.PlanTagContainsKeywordsPredicate;
import seedu.address.model.person.RiskTagContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) or risk tags, \n"
            + "and displays them as a list with index numbers.\n\n"
            + "Parameters: PREFIX/ KEYWORD [MORE_KEYWORDS]\n"
            + PREFIX_NAME.getPrefix() + " KEYWORD [MORE_KEYWORDS]...\n"
            + PREFIX_RISKTAG.getPrefix() + " RISKTAG [MORE_RISKTAGS]...\n"
            + PREFIX_TAG.getPrefix() + " TAG [MORE_TAGS]...\n"
            + PREFIX_PHONE.getPrefix() + " PHONE [MORE_PHONE]...\n"
            + PREFIX_INCOME.getPrefix() + ">/< INCOME \n"
            + "Examples: " + COMMAND_WORD + " " + PREFIX_NAME.getPrefix() + " alice bob charlie\n"
            + COMMAND_WORD + " " + PREFIX_RISKTAG.getPrefix() + " high low\n"
            + COMMAND_WORD + " " + PREFIX_PLANTAG.getPrefix() + " savings plan"
            + COMMAND_WORD + " " + PREFIX_PHONE.getPrefix() + " 912345678"
            + COMMAND_WORD + " " + PREFIX_INCOME.getPrefix() + " >1000";

    private final Predicate<Person> predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindCommand(RiskTagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindCommand(NormalTagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindCommand(PlanTagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindCommand(PhoneContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindCommand(IncomeContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }
    public FindCommand(MonthlyContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
