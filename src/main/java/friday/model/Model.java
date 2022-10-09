package friday.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import friday.commons.core.GuiSettings;
import friday.model.student.Student;
import javafx.collections.ObservableList;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Student> PREDICATE_SHOW_ALL_STUDENTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' FRIDAY file path.
     */
    Path getFridayFilePath();

    /**
     * Sets the user prefs' FRIDAY file path.
     */
    void setFridayFilePath(Path fridayFilePath);

    /**
     * Replaces FRIDAY's data with the data in {@code friday}.
     */
    void setFriday(ReadOnlyFriday friday);

    /** Returns FRIDAY */
    ReadOnlyFriday getFriday();

    /**
     * Returns true if a student with the same identity as {@code student} exists in FRIDAY.
     */
    boolean hasStudent(Student student);

    /**
     * Deletes the given student.
     * The student must exist in STUDENT.
     */
    void deleteStudent(Student target);

    /**
     * Adds the given student.
     * {@code student} must not already exist in FRIDAY.
     */
    void addStudent(Student student);

    /**
     * Replaces the given student {@code target} with {@code editedStudent}.
     * {@code target} must exist in FRIDAY.
     * The student identity of {@code editedStudent} must not be the same as another existing student in FRIDAY.
     */
    void setStudent(Student target, Student editedStudent);

    /** Returns an unmodifiable view of the filtered student list */
    ObservableList<Student> getFilteredStudentList();

    /**
     * Updates the filter of the filtered student list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredStudentList(Predicate<Student> predicate);
}
