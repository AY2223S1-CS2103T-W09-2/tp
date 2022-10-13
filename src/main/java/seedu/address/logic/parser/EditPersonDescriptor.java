package seedu.address.logic.parser;

import static seedu.address.model.person.Person.MAXIMUM_APPOINTMENTS;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.util.MaximumSortedList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;


/**
 * Stores the details to edit the person with. Each non-empty field value will replace the
 * corresponding field value of the person.
 */
public class EditPersonDescriptor {
    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private MaximumSortedList<Appointment> appointments;
    public EditPersonDescriptor() {}

    /**
     * Copy constructor.
     * A defensive copy of {@code tags} is used internally.
     */
    public EditPersonDescriptor(EditPersonDescriptor toCopy) {
        setName(toCopy.name);
        setPhone(toCopy.phone);
        setEmail(toCopy.email);
        setAddress(toCopy.address);
        setTags(toCopy.tags);
        setAppointments(toCopy.appointments);
    }

    /**
     * Returns true if at least one field is edited.
     */
    public boolean isAnyFieldEdited() {
        return CollectionUtil.isAnyNonNull(name, phone, email, address, tags, appointments);
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Optional<Name> getName() {
        return Optional.ofNullable(name);
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Optional<Phone> getPhone() {
        return Optional.ofNullable(phone);
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Optional<Email> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Sets {@code appointments} to this object's {@code appointments}.
     * A defensive copy of {@code appointments} is used internally.
     */
    public void setAppointments(MaximumSortedList<Appointment> appointments) {
        this.appointments = (appointments != null) ? new MaximumSortedList<>(appointments) : null;
    }

    public Optional<MaximumSortedList<Appointment>> getAppointments() {
        return Optional.ofNullable(appointments);
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    /**
     * Sets {@code tags} to this object's {@code tags}.
     * A defensive copy of {@code tags} is used internally.
     */
    public void setTags(Set<Tag> tags) {
        this.tags = (tags != null) ? new HashSet<>(tags) : null;
    }

    /**
     * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     * Returns {@code Optional#empty()} if {@code tags} is null.
     */
    public Optional<Set<Tag>> getTags() {
        return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    public static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with a new appointment from {@code editPersonDescriptor}.
     */
    public static Person createEditedPersonWithNewAppointment(
            Person personToEdit, EditPersonDescriptor editPersonDescriptor) throws ParseException {
        assert personToEdit != null;

        MaximumSortedList<Appointment> updatedAppointments = personToEdit.getAppointments();

        Optional<Boolean> hasAppointment = editPersonDescriptor.appointments.stream()
                .map(updatedAppointments::contains).reduce((x, y) -> x || y);
        Optional<Boolean> isAppointmentsEdited = editPersonDescriptor.appointments.stream()
                .map(updatedAppointments::add).reduce((x, y) -> x || y);

        if (hasAppointment.isEmpty() || hasAppointment.get()) {
            throw new ParseException("You have entered a duplicate appointment for this client");
        }

        if (isAppointmentsEdited.isEmpty() || !isAppointmentsEdited.get()) {
            throw new ParseException("You have already reached the maximum number of appointments ("
                    + MAXIMUM_APPOINTMENTS + ") for this client");
        }

        Name name = personToEdit.getName();
        Phone phone = personToEdit.getPhone();
        Email email = personToEdit.getEmail();
        Address address = personToEdit.getAddress();
        Set<Tag> tags = personToEdit.getTags();
        Person newPerson = new Person(name, phone, email, address, tags);

        newPerson.setAppointments(updatedAppointments);
        return newPerson;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditPersonDescriptor)) {
            return false;
        }

        // state check
        EditPersonDescriptor e = (EditPersonDescriptor) other;

        return getName().equals(e.getName())
                && getPhone().equals(e.getPhone())
                && getEmail().equals(e.getEmail())
                && getAddress().equals(e.getAddress())
                && getTags().equals(e.getTags())
                && getAppointments().equals(e.getAppointments());
    }
}
