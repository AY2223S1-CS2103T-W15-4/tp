package friday.logic.commands;

import static friday.logic.parser.CliSyntax.PREFIX_CONSULTATION;
import static friday.logic.parser.CliSyntax.PREFIX_MASTERYCHECK;
import static friday.logic.parser.CliSyntax.PREFIX_NAME;
import static friday.logic.parser.CliSyntax.PREFIX_TAG;
import static friday.logic.parser.CliSyntax.PREFIX_TELEGRAMHANDLE;
import static friday.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import friday.commons.core.Messages;
import friday.commons.core.index.Index;
import friday.commons.util.CollectionUtil;
import friday.logic.commands.exceptions.CommandException;
import friday.model.Model;
import friday.model.student.Consultation;
import friday.model.student.MasteryCheck;
import friday.model.student.Name;
import friday.model.student.Remark;
import friday.model.student.Student;
import friday.model.student.TelegramHandle;
import friday.model.tag.Tag;

/**
 * Edits the details of an existing person in the masteryCheck book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_TELEGRAMHANDLE + "PHONE] "
            + "[" + PREFIX_CONSULTATION + "EMAIL] "
            + "[" + PREFIX_MASTERYCHECK + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TELEGRAMHANDLE + "91234567 "
            + PREFIX_CONSULTATION + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the masteryCheck book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Student studentToEdit = lastShownList.get(index.getZeroBased());
        Student editedStudent = createEditedPerson(studentToEdit, editPersonDescriptor);

        if (!studentToEdit.isSamePerson(editedStudent) && model.hasPerson(editedStudent)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(studentToEdit, editedStudent);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedStudent));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Student createEditedPerson(Student studentToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert studentToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(studentToEdit.getName());
        TelegramHandle updatedTelegramHandle = editPersonDescriptor.getTelegramHandle().orElse(studentToEdit
                .getTelegramHandle());
        Consultation updatedConsultation = editPersonDescriptor.getConsultation()
                .orElse(studentToEdit.getConsultation());
        MasteryCheck updatedMasteryCheck = editPersonDescriptor.getMasteryCheck()
                .orElse(studentToEdit.getMasteryCheck());
        Remark updatedRemark = studentToEdit.getRemark(); // edit command does not allow editing remarks
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(studentToEdit.getTags());

        return new Student(updatedName, updatedTelegramHandle, updatedConsultation, updatedMasteryCheck, updatedRemark,
                updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private TelegramHandle telegramHandle;
        private Consultation consultation;
        private MasteryCheck masteryCheck;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setTelegramHandle(toCopy.telegramHandle);
            setConsultation(toCopy.consultation);
            setMasteryCheck(toCopy.masteryCheck);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, telegramHandle, consultation, masteryCheck, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setTelegramHandle(TelegramHandle telegramHandle) {
            this.telegramHandle = telegramHandle;
        }

        public Optional<TelegramHandle> getTelegramHandle() {
            return Optional.ofNullable(telegramHandle);
        }

        public void setConsultation(Consultation consultation) {
            this.consultation = consultation;
        }

        public Optional<Consultation> getConsultation() {
            return Optional.ofNullable(consultation);
        }

        public void setMasteryCheck(MasteryCheck masteryCheck) {
            this.masteryCheck = masteryCheck;
        }

        public Optional<MasteryCheck> getMasteryCheck() {
            return Optional.ofNullable(masteryCheck);
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
                    && getTelegramHandle().equals(e.getTelegramHandle())
                    && getConsultation().equals(e.getConsultation())
                    && getMasteryCheck().equals(e.getMasteryCheck())
                    && getTags().equals(e.getTags());
        }
    }
}
