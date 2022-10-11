package seedu.address.logic.parser.sort;

import java.util.Comparator;

import seedu.address.model.person.Person;

/**
 * Custom comparator that sorts Person based on their Names.
 */
public class SortByName implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        return p1.getName().fullName.compareTo(p2.getName().fullName);
    }
}