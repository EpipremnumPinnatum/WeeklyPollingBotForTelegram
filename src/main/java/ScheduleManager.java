import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Sends a poll every week with a stylised title and all weekdays as options to the specified telegram chat.
 *
 * @author Valentin Iseli
 */
public class ScheduleManager {
    private static final ZoneId TIME_ZONE = ZoneId.of("Europe/Zurich");
    static final int STARTING_DAY = DayOfWeek.SUNDAY.getValue();
    static final int STARTING_TIME_HOUR = 18;
    static final int STARTING_TIME_MINUTE = 26;

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        WeeklyPollingBot weeklyPollingBot = new WeeklyPollingBot();
        botsApi.registerBot(weeklyPollingBot);

        //Test if message gets sent to chat -> Bot was registered correctly
        weeklyPollingBot.sendTestMessage("If this appears in the chat the bot was registered correctly.");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        LocalDateTime timeNow = LocalDateTime.now(TIME_ZONE);


        LocalDateTime timeOfNextRun = getNextWeekdayRunTime(timeNow);
        System.out.println("Next scheduled run: " + timeOfNextRun);
        long initialDelay = ChronoUnit.MINUTES.between(timeNow, timeOfNextRun);
        System.out.println("Initial delay: " + initialDelay + " minutes");
        //7*24*60 calculates the minutes in a week
        scheduler.scheduleAtFixedRate(weeklyPollingBot::sendThePoll, initialDelay, 7 * 24 * 60, TimeUnit.MINUTES);
    }

    /**
     * Gets the timestamp of the following Friday at the time specified.
     *
     * @param timeStampNow is the present time as a LocalDateTime object
     * @return date of the coming Friday at the time specified
     */



    private static LocalDateTime getNextWeekdayRunTime(LocalDateTime timeStampNow) {
        LocalDateTime timeStampOfNextRun = timeStampNow.withHour(STARTING_TIME_HOUR).withMinute(STARTING_TIME_MINUTE);

        // If it's already passed Friday or it's Friday, but already after the specified time
        // (measured by day, hours and minutes), add a week to the nextRun timestamp
        if (timeStampNow.getDayOfWeek().getValue() > STARTING_DAY ||
                (timeStampNow.getDayOfWeek().getValue() == STARTING_DAY
                        && timeStampNow.getHour() >= STARTING_TIME_HOUR
                        && timeStampNow.getMinute() >= STARTING_TIME_MINUTE)) {
            timeStampOfNextRun = timeStampOfNextRun.plusWeeks(1);
        }

        timeStampOfNextRun = switch (STARTING_DAY) {
            case 1 -> timeStampOfNextRun.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
            case 2 -> timeStampOfNextRun.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
            case 3 -> timeStampOfNextRun.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
            case 4 -> timeStampOfNextRun.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
            case 5 -> timeStampOfNextRun.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
            case 6 -> timeStampOfNextRun.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
            case 7 -> timeStampOfNextRun.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            default -> timeStampOfNextRun;
        };

        return timeStampOfNextRun;
    }
}
