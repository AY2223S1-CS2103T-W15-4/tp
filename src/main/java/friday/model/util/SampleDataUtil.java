package friday.model.util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import friday.model.AddressBook;
import friday.model.ReadOnlyAddressBook;
import friday.model.student.Consultation;
import friday.model.student.MasteryCheck;
import friday.model.student.Name;
import friday.model.student.Remark;
import friday.model.student.Student;
import friday.model.student.TelegramHandle;
import friday.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    public static final Remark EMPTY_REMARK = new Remark("");

    public static Student[] getSamplePersons() {
        return new Student[] {
            new Student(new Name("Alex Yeoh"), new TelegramHandle("al3x"), new Consultation(LocalDate.now()),
                new MasteryCheck(LocalDate.of(2022, 8, 16)), EMPTY_REMARK,
                getTagSet("friends")),
            new Student(new Name("Bernice Yu"), new TelegramHandle("bernice123"),
                    new Consultation(LocalDate.of(2022, 9, 1)),
                    new MasteryCheck(LocalDate.of(2022, 7, 30)), new Remark("Weak at recursion"),
                    getTagSet("colleagues", "friends")),
            new Student(new Name("Charlotte Oliveiro"), new TelegramHandle("Char_Oli"),
                    new Consultation(LocalDate.of(2021, 10, 21)),
                    new MasteryCheck(LocalDate.of(2022, 12, 27)), new Remark("Smart"),
                    getTagSet("neighbours")),
            new Student(new Name("David Li"), new TelegramHandle("d4viD"),
                    new Consultation(LocalDate.of(2022, 6, 17)),
                    new MasteryCheck(LocalDate.of(2022, 11, 11)), EMPTY_REMARK,
                    getTagSet("family")),
            new Student(new Name("Irfan Ibrahim"), new TelegramHandle("irfan72345"),
                    new Consultation(LocalDate.of(2025, 2, 20)),
                    new MasteryCheck(LocalDate.of(2020, 9, 1)), EMPTY_REMARK,
                    getTagSet("classmates")),
            new Student(new Name("Roy Balakrishnan"), new TelegramHandle("iAmRoy"),
                    new Consultation(LocalDate.of(2022, 7, 12)),
                    new MasteryCheck(LocalDate.of(2029, 9, 26)), EMPTY_REMARK,
                    getTagSet("colleagues"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Student sampleStudent : getSamplePersons()) {
            sampleAb.addPerson(sampleStudent);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
