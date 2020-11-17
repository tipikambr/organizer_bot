package organizer.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.BuildVars;

public class BotOrganizer extends TelegramLongPollingBot {
    private static final String LOGTAG = "BOT_ORGANIZER";


    @Override
    public void onUpdateReceived(Update update) {
        BotLogger.debug(LOGTAG, update.getMessage().getText());
    }

    @Override
    public String getBotUsername() {
        return BuildVars.CHANNEL_USER;
    }

    @Override
    public String getBotToken() {
        return BuildVars.CHANNEL_TOKEN;
    }
}
