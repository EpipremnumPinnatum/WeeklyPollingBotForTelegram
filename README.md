# WeeklyPollingBotForTelegram
Sets up a bot for weekly polling with all the weekdays in German as options. 
The title of the poll will be the starting and end dates of the week.

The program used Maven and I would recommend to have a look at this tutorial to set up your bot first:

https://core.telegram.org/bots/tutorial

Maven helps with structuring the folders and manage the library dependencies, in this case I used:

https://github.com/rubenlagus/TelegramBots

as is described in the Bot Tutorial of telegram. 

There you will get a **bot token** and you have to find out what the **group ID** is
of the group you want to send your poll to.

The code makes reference to the following variables in the _ScheduleManager.java_ file that you have to pay attention to


**What time zone do you need your bot to work with? It has to be specified here:**
- private static final ZoneId TIME_ZONE = ZoneId.of("Europe/Zurich");

**At what day/hour and minute of the day should it be sent? If you want a different day or time, you have to change it
accordingly:**

- static final int STARTING_DAY = DayOfWeek.<span style="color:green;">SUNDAY</span>.getValue();
- static final int STARTING_TIME_HOUR = <span style="color:green;">18</span>;
- static final int STARTING_TIME_MINUTE = <span style="color:green;">30</span>;

So that not anybody can easily send messages to my groupchat and manipulate my bot I created some 
<a href="https://en.wikipedia.org/wiki/Environment_variable" >environment variables</a> 
that are saved on the computer locally. So this program is set out, so you can do the same. 
I named them:

-THE_NOODZ_BOT_TOKEN

-THE_NOODZ_GROUP_CHAT_ID

and they are in the _WeeklyPollingBot.java_ file.


The Noodz is the name of my <a href="https://www.instagram.com/thenoodz_official?igsh=MWVrcGl1cjB0b3V5ZA==">band</a>. 
However, you can change that in the WeeklyPollingBot.java file
at: 

- String groupChatId = System.getenv("THE_NOODZ_GROUP_CHAT_ID");


and

- return System.getenv("THE_NOODZ_BOT_TOKEN");

The names have to be consistent with the ones that you created on your system (Windows 10, Apple, Linux etc.) 
otherwise the message will not reach the intended chat. In the main method there is a small check 
to see if the bot was registered correctly:

weeklyPollingBot.sendTestMessage("<span style="color:green">If this appears in the chat the bot was 
registered correctly.</span>");

If that message appears in your chat when you run the program, then the bot token and group chat ID are correctly set up.



