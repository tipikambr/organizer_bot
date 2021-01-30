package organizer.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.BuildVars;
import organizer.bot.utils.Commands;
import organizer.bot.utils.Language;
import organizer.bot.utils.Menus;
import organizer.bot.utils.menus.list.AddingListMenu;
import organizer.bot.utils.menus.list.ListMenu;
import organizer.bot.utils.menus.mail.MailMenu;
import organizer.bot.utils.menus.*;
import organizer.data.base.connection.utils.User;

import java.util.HashMap;

public class BotOrganizer extends TelegramLongPollingBot {
    /**
     * <telegram_id, menu_state>
     */
    private final static HashMap<Long, Integer> usersPositionMenu = new HashMap<>();
    private final static HashMap<Long, String> usersNames = new HashMap<>();
    private final static HashMap<Long, Integer> userLang = new HashMap<>();

    /**
     * Метод, отправляющий сообщение в чат
     * @param chat_id чат, куда надо отправить сообщение
     * @param message сообщение, которое надо отправить
     */

    private void sendMessage(long chat_id, String message, boolean enabledNotification){
        SendMessage sendMessage = new SendMessage()
                .setChatId(chat_id)
                .setText(message);
        if (!enabledNotification)
            sendMessage.disableNotification();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            BotLogger.error(this.getClass().getName(), e.getMessage(), e);
        }
    }
    /**
     * Метод, отправляющий сообщение в чат, создавая кнопки под сообщением
     * @param chat_id чат, куда надо отправить сообщение
     * @param message сообщение, которое надо отправить
     * @param markup кнопки, которые надо отправить
     */
    private void sendMessage(long chat_id, String message, ReplyKeyboard markup, boolean enabledNotification){
        SendMessage sendMessage = new SendMessage()
                .setChatId(chat_id)
                .setText(message)
                .setReplyMarkup(markup);
        if (!enabledNotification)
            sendMessage.disableNotification();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            BotLogger.error(this.getClass().getName(), e.getMessage(), e);
        }
    }

    /**
     * Метод, обрабатывающий действия в меню
     * @param message сообщение, которое пришло от пользователя
     * @param chatId id чата, откуда пришло сообщение, и куда отвечать, при необходимости.
     */
    private void commandController(String message, long chatId){
        switch (message){
            case "/start":
                sendMessage(chatId, FirstMenu.getFirstMessage(), FirstMenu.getFirstMenu(), false);
                usersNames.remove(chatId);
                usersPositionMenu.remove(chatId);
                break;
            case Commands.russianLanguageCommand:
                if (usersPositionMenu.containsKey(chatId)){
                    usersPositionMenu.put(chatId, Menus.MAIN_MENU);
                    sendMessage(chatId, MainMenu.getBackToMainMenuMessage(Language.RU),
                            MainMenu.getMainMenu(Language.RU), false);
                } else {
                    usersPositionMenu.put(chatId, Menus.WRITING_NAME);
                    userLang.put(chatId, Language.RU);
                    sendMessage(chatId, FirstMenu.getFirstNameMessage(Language.RU), new ReplyKeyboardRemove(), false);
                }
                break;
            case Commands.infoCommandRU:
                usersPositionMenu.put(chatId, Menus.MAIN_MENU);
                sendMessage(chatId, MainMenu.getInfoMessage(Language.RU),
                        MainMenu.getMainMenu(Language.RU), false);
                break;
            case Commands.mailSettingCommandRU:
                usersPositionMenu.put(chatId, Menus.MAIL_MENU);
                sendMessage(chatId, MailMenu.getMailMenuIntroMessage(Language.RU),
                        MailMenu.getMailMenu(Language.RU), false);
                break;
            case Commands.listSettingCommandRU:
                usersPositionMenu.put(chatId, Menus.LIST_MENU);
                sendMessage(chatId, ListMenu.getListIntroMessage(Language.RU),
                        ListMenu.getListMenu(Language.RU), false);
                break;
            case Commands.soundCommandRU:
                usersPositionMenu.put(chatId, SoundMenu.getNumberMenu(usersPositionMenu.get(chatId)));
                sendMessage(chatId, SoundMenu.getSoundIntroMessage(Language.RU),
                        SoundMenu.getSoundMenu(Language.RU), false);
                break;
            case Commands.languageCommandRU:
                usersPositionMenu.put(chatId, Menus.LANGUAGE_MENU);
                userLang.put(chatId, Language.RU);
                sendMessage(chatId, LanguageMenu.getLanguageIntroMessageRuIntroMessage(Language.RU),
                        LanguageMenu.getLanguageMenu(Language.RU), false);
                break;
            case Commands.cancelCommandRU:
            case Commands.backCommandRU:
                //TODO Тут надо настроить правильное написание сообщения!
                System.out.println(usersPositionMenu.get(chatId));
                usersPositionMenu.put(chatId, Menus.getUpperMenuNumber(usersPositionMenu.get(chatId)));
                sendMessage(chatId, MainMenu.getBackToMainMenuMessage(Language.RU),
                        Menus.getMenu(usersPositionMenu.get(chatId), Language.RU), false);
                break;
            case Commands.soundOnCommandRU:
                //TODO надо реализовать включение звука
                sendMessage(chatId, SoundMenu.getSoundTurnOnMessage(Language.RU),
                        Menus.getMenu(usersPositionMenu.get(chatId), Language.RU), false);
                break;
            case Commands.soundOffCommandRU:
                //TODO надо реализовать выключение звука
                sendMessage(chatId, SoundMenu.getSoundTurnOffMessage(Language.RU),
                        Menus.getMenu(usersPositionMenu.get(chatId), Language.RU), false);
                break;
            case Commands.soundNotChangeCommandRU:
                usersPositionMenu.put(chatId, Menus.MAIN_MENU);
                sendMessage(chatId, SoundMenu.getSoundNotChangeMessage(Language.RU),
                        Menus.getMenu(usersPositionMenu.get(chatId), Language.RU), false);
                break;
            case Commands.addListCommandRU:
                usersPositionMenu.put(chatId, Menus.WRITING_LIST_NAME);
                sendMessage(chatId, AddingListMenu.getAddingListIntroMessage(Language.RU),
                        AddingListMenu.getMenu(Language.RU), false);
                break;
        }
    }

    /**
     * Метод, определяющий действия бота, в случае, если пользователь отправил сообщение
     * для изменения данных в базе данных
     * @param message сообщение, которое отправил пользователь
     * @param chatId id чата, из которого пришло сообщение, и куда отвечать, если надо.
     */
    private void editController(String message, long chatId){
        switch (usersPositionMenu.get(chatId)){
            case (Menus.WRITING_NAME):
                usersNames.put(chatId, message);
                usersPositionMenu.put(chatId, Menus.WRITING_PASSWORD);
                sendMessage(chatId, FirstMenu.getFirstPasswordMessage(userLang.get(chatId)),
                        false);
                break;
            case (Menus.WRITING_PASSWORD):
                if (User.authUser(chatId, usersNames.get(chatId), message) != 1){
                    usersPositionMenu.put(chatId, Menus.WRITING_NAME);
                    sendMessage(chatId, FirstMenu.getRetryMessage(userLang.get(chatId)), false);
                    break;
                };
                sendMessage(chatId, MainMenu.getNewUser(userLang.get(chatId), chatId, usersNames.get(chatId)),
                        MainMenu.getMainMenu(userLang.get(chatId)), false);
                usersPositionMenu.put(chatId, Menus.MAIN_MENU);
                break;
            case (Menus.WRITING_LIST_NAME):
                int lang = userLang.get(chatId);
                if (organizer.data.base.connection.utils.List.createList(usersNames.get(chatId), message))
                    sendMessage(chatId, AddingListMenu.getAddingListSuccessMessage(lang),false );
                 else
                    sendMessage(chatId, AddingListMenu.getAddingListFailMessageRu(lang),false );
                usersPositionMenu.put(chatId, Menus.LIST_MENU);
                sendMessage(chatId, ListMenu.getListIntroMessage(lang), ListMenu.getListMenu(lang),false );

                break;
        }
    }

    /**
     * Разгрузочный метод, определяющий состояние меню (активно оно или нет)
     * @param message сообщение, которое отправил пользователь
     * @param chatId id чата, из которого пришло сообщение, и куда отвечать, если надо.
     */
    private void messageController(String message, long chatId){
        Integer state = usersPositionMenu.get(chatId);
        if (Menus.isEditing(state) && !Commands.isCancel(message))
            editController(message, chatId);
        else
            commandController(message, chatId);
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            BotLogger.debug(this.getClass().getName(), update.getMessage().getText());

            String message = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            messageController(message, chat_id);
        }
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
