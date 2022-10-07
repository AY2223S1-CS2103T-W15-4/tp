package friday.logic.parser;

import static friday.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static friday.logic.parser.CliSyntax.PREFIX_CONSULTATION;
import static friday.logic.parser.CliSyntax.PREFIX_MASTERYCHECK;
import static friday.logic.parser.CliSyntax.PREFIX_NAME;
import static friday.logic.parser.CliSyntax.PREFIX_TAG;
import static friday.logic.parser.CliSyntax.PREFIX_TELEGRAMHANDLE;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import friday.logic.commands.AddCommand;
import friday.logic.parser.exceptions.ParseException;
import friday.model.student.Consultation;
import friday.model.student.MasteryCheck;
import friday.model.student.Name;
import friday.model.student.Remark;
import friday.model.student.Student;
import friday.model.student.TelegramHandle;
import friday.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TELEGRAMHANDLE, PREFIX_CONSULTATION,
                        PREFIX_MASTERYCHECK);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        TelegramHandle telegramHandle = ParserUtil.parseTelegramHandle(argMultimap
                .getValue(PREFIX_TELEGRAMHANDLE).orElse(""));
        Consultation consultation = ParserUtil.parseConsultation(LocalDate.parse(argMultimap
                .getValue(PREFIX_CONSULTATION).orElse("2001-01-01")));
        MasteryCheck masteryCheck = ParserUtil.parseMasteryCheck(LocalDate.parse(argMultimap
                .getValue(PREFIX_MASTERYCHECK).orElse("2001-01-01")));
        Remark remark = new Remark(""); // add command does not allow adding remarks straight away

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Student student = new Student(name, telegramHandle, consultation, masteryCheck, remark, tagList);

        return new AddCommand(student);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
