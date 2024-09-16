import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class WeeklyPollingBot extends TelegramLongPollingBot {

    String groupChatId = System.getenv("THE_NOODZ_GROUP_CHAT_ID");
    private final List<String> daysOfTheWeek = Arrays.asList("Montag", "Dienstag", "Mittwoch",
            "Donnerstag", "Freitag", "Samstag", "Sonntag");


    final private String BOT_NAME = "The Noodz Weekly Scheduler";

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    /**
     * Gives back the Bot Token in form of a String.
     *
     * @return String of the Telegram Bot Token
     */
    @Override
    public String getBotToken() {
        return System.getenv("THE_NOODZ_BOT_TOKEN");

    }

    @Override
    public void onUpdateReceived(Update update) {
        var message = update.getMessage();
        String messageText = message.getText();

        if (message.isCommand()) {
            if (messageText.equals("/createpoll@WeeklySchedulerBot")) {
                sendThePoll();
            }
        }
    }

    /**
     * Sends a test message to the chat to check if the bot was registered correctly.
     * @param message The message with type String that should be displayed in the group chat.
     */
    public void sendTestMessage(String message){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(groupChatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
            System.out.println("Test message was sent successfully");
        } catch (TelegramApiException e) {
            System.err.println("Failed to send test message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Creates a poll and sends it to the groupchat.
     */

    public void sendThePoll() {
        String pollTitle = createPollTitle();
        SendPoll weeklyPoll = createPoll(pollTitle);

        try {
            execute(weeklyPoll);
            System.out.println("Poll was sent");
        } catch (TelegramApiException e) {
            System.err.println("Failed to send poll: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a poll with the different days of the week, in German, as the options.
     * The answers of the poll are not anonymous and the users can choose multiple days as their answer.
     *
     * @param pollTitle the starting- and end-date of the week
     * @return poll
     */
    private SendPoll createPoll(String pollTitle) {
        SendPoll poll = SendPoll.builder().
                question(pollTitle).
                allowMultipleAnswers(true).
                isAnonymous(false).
                options(daysOfTheWeek).
                allowSendingWithoutReply(true).
                chatId(groupChatId).
                build();
        System.out.println("Poll was created");
        return poll;
    }

    /**
     * Gives back the shortened title if both days in the same month or year.
     * So it doesn't write the month or year out two times, if not necessary
     * <pre>
     * Examples:
     * 12 - 19.3.2024
     * 31.11 - 6.12.2024
     * 31.12.2024 - 5.1.2025
     * </pre>
     *
     * @return String the date as a title, shortened when possible
     */

    private String createPollTitle() {
        LocalDate nextMonday = LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(1);
        LocalDate nextSunday = LocalDate.now().with(DayOfWeek.SUNDAY).plusWeeks(1);

        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("d.M.yyyy", Locale.GERMAN);
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d", Locale.GERMAN);
        DateTimeFormatter dayMonthFormatter = DateTimeFormatter.ofPattern("d.M", Locale.GERMAN);


        if (nextMonday.getYear() == nextSunday.getYear()) {
            if (nextMonday.getMonth() == nextSunday.getMonth()) {
                return nextMonday.format(dayFormatter) + " - " + nextSunday.format(fullFormatter);
            } else {
                System.out.println("Poll title was created");
                return nextMonday.format(dayMonthFormatter) + " - " + nextSunday.format(fullFormatter);
            }
        } else {
            System.out.println("poll title was created");
            return nextMonday.format(fullFormatter) + " - " + nextSunday.format(fullFormatter);
        }


    }

}
