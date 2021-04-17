package organizer.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.BuildVars;
import organizer.bot.utils.constants.Commands;
import organizer.bot.utils.constants.Language;
import organizer.bot.utils.constants.Menus;
import organizer.bot.utils.menus.alarm.AlarmCalendarMenu;
import organizer.bot.utils.menus.alarm.AlarmClockMenu;
import organizer.bot.utils.menus.alarm.AlarmMenu;
import organizer.bot.utils.menus.language.LanguageMenu;
import organizer.bot.utils.menus.list.AddingListMenu;
import organizer.bot.utils.menus.list.ListMenu;
import organizer.bot.utils.menus.list.ListViewer;
import organizer.bot.utils.menus.mail.MailMenu;
import organizer.bot.utils.menus.*;
import organizer.bot.utils.menus.mail.MailViewer;
import organizer.bot.utils.menus.sound.SoundMenu;
import organizer.bot.utils.menus.task.TaskCalendarMenu;
import organizer.bot.utils.menus.task.TaskClockMenu;
import organizer.bot.utils.menus.task.TaskMenu;
import organizer.data.base.connection.utils.*;
import organizer.mail.MailCheck;

import javax.mail.MessagingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class BotOrganizer extends TelegramLongPollingBot {
    /**
     * <telegram_id, menu_state>
     */
    private final static HashMap<Long, Integer> usersPositionMenu = new HashMap<>();
    private final static HashMap<Long, String> usersNames = new HashMap<>();
    private final static HashMap<Long, Integer> userLang = new HashMap<>();
    private final static HashMap<Long, String> userList = new HashMap<>();
    private final static HashMap<Long, String> userMail = new HashMap<>();
    private final static HashMap<Long, Boolean> userSound = new HashMap<>();
    private final static HashMap<Long, ArrayList<Integer> > userMessages = new HashMap<>();
    private final static HashMap<Long, Timestamp> userTaskTime = new HashMap<>();
    private final static HashMap<Long, String> userTask = new HashMap<>();
    private final static HashMap<Integer, Integer> alarmEnd = new HashMap<>();
    private final static HashMap<Integer, Boolean> taskEnd = new HashMap<>();

    static boolean working = false;
    /**
     * Метод, отправляющий сообщение в чат
     * @param chat_id чат, куда надо отправить сообщение
     * @param message сообщение, которое надо отправить
     */

    public void sendMessage(long chat_id, String message){
        SendMessage sendMessage = new SendMessage()
                .setChatId(chat_id)
                .setText(message);
        if (userSound.get(chat_id) == null || userSound.get(chat_id))
            sendMessage.enableNotification();
        else
            sendMessage.disableNotification();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            BotLogger.error(this.getClass().getName(), e.getMessage(), e);
        }
    }


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
        else
            sendMessage.enableNotification();
        try {
            var res = execute(sendMessage);
            userMessages.get(chat_id).add(res.getMessageId());
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
        else
            sendMessage.enableNotification();
        try {
            var res = execute(sendMessage);
            userMessages.get(chat_id).add(res.getMessageId());
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
                userMessages.put(chatId, new ArrayList<>(0));
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
            case Commands.taskSettingCommandRU:
                usersPositionMenu.put(chatId, Menus.TASK_MENU);
                sendMessage(chatId, TaskMenu.getTaskMenuIntroMessage(Language.RU),
                        TaskMenu.getTaskMenu(Language.RU), false);
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
                usersPositionMenu.put(chatId, Menus.getUpperMenuNumber(usersPositionMenu.get(chatId)));
                sendMessage(chatId, MainMenu.getBackToMainMenuMessage(Language.RU),
                        Menus.getMenu(usersPositionMenu.get(chatId), Language.RU), false);
                break;
            case Commands.soundOnCommandRU:
                if (User.setSound(chatId, true))
                    sendMessage(chatId, SoundMenu.getSoundTurnOnMessage(Language.RU),
                        Menus.getMenu(usersPositionMenu.get(chatId), Language.RU), false);
                else
                    sendMessage(chatId, SoundMenu.getSoundChangeError(Language.RU), Menus.getMenu(usersPositionMenu.get(chatId), Language.RU), false);
                break;
            case Commands.soundOffCommandRU:
                if (User.setSound(chatId, false))
                    sendMessage(chatId, SoundMenu.getSoundTurnOffMessage(Language.RU),
                        Menus.getMenu(usersPositionMenu.get(chatId), Language.RU), false);
                else
                    sendMessage(chatId, SoundMenu.getSoundChangeError(Language.RU), Menus.getMenu(usersPositionMenu.get(chatId), Language.RU), false);
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
            case Commands.lookAllListsCommandRU:
                usersPositionMenu.put(chatId, Menus.CHECK_AVAILABLE_LIST);
                sendMessage(chatId, ListViewer.getListViewerMessage(Language.RU),
                        ListViewer.getListsMarkap(List.userLists(chatId)), false);
                break;
            case Commands.addItemRU:
                usersPositionMenu.put(chatId, Menus.ADD_LIST_ITEM);
                sendMessage(chatId, "Напишите элемент списка", AddingListMenu.getMenu(Language.RU), false);
                break;
            case Commands.addFilterRU:
                usersPositionMenu.put(chatId, Menus.ADD_MAIL_CHECKER);
                sendMessage(chatId, "Напишите слово или словосочетание, по которому будут проверяться почты", AddingListMenu.getMenu(Language.RU), false);
                break;
            case Commands.removeListRU:
                usersPositionMenu.put(chatId, Menus.LIST_MENU);
                List.deleteList(chatId, getOwner(userList.get(chatId)), getDescription(userList.get(chatId)));
                userList.put(chatId, null);
                sendMessage(chatId, ListMenu.getListIntroMessage(userLang.get(chatId)), ListMenu.getListMenu(userLang.get(chatId)), false);
                break;
            case Commands.removeMailRU:
                usersPositionMenu.put(chatId, Menus.MAIL_MENU);
                Mail.deleteMail(chatId, userMail.get(chatId));
                userMail.put(chatId, null);
                sendMessage(chatId, MailMenu.getMailMenuIntroMessage(userLang.get(chatId)), MailMenu.getMailMenu(userLang.get(chatId)), false);
                break;
            case Commands.addMailRU:
                usersPositionMenu.put(chatId, Menus.ADD_MAIL);
                sendMessage(chatId, "Введите вашу почту", AddingListMenu.getMenu(Language.RU), false);
                break;
            case Commands.lookMailsRU:
                usersPositionMenu.put(chatId, Menus.CHECK_MAIL);
                sendMessage(chatId, MailViewer.getMailViewerMessage(Language.RU),
                        MailViewer.getMailsMarkap(Mail.userMails(chatId)), false);
                break;
            case Commands.shareListRU:
                if (!User.isOwner(chatId, getOwner(userList.get(chatId)))){
                    sendMessage(chatId, "Какие мы хитренькие, это не твой списокчек) Вот иди теперь в главное меню!", MainMenu.getMainMenu(Language.RU), false);
                    usersPositionMenu.put(chatId, Menus.MAIN_MENU);
                    break;
                }
                usersPositionMenu.put(chatId, Menus.SHARE_LIST);
                sendMessage(chatId, "Напишите логин другого пользователя", AddingListMenu.getMenu(Language.RU), false);
                break;
            case Commands.lookSharePersonsRU:
                if (!User.isOwner(chatId, getOwner(userList.get(chatId)))){
                    sendMessage(chatId, "Какие мы хитренькие, это не твой списокчек) Вот иди теперь в главное меню!", MainMenu.getMainMenu(Language.RU), false);
                    usersPositionMenu.put(chatId, Menus.MAIN_MENU);
                    break;
                }
                usersPositionMenu.put(chatId, Menus.LOOK_USERS_SHARED);
                showShareList(chatId);
                break;
            case Commands.alarmSettingCommandRU:
                usersPositionMenu.put(chatId, Menus.ALARM_MENU);
                sendMessage(chatId, AlarmMenu.getAlarmMenuIntroMessage(Language.RU),
                        AlarmMenu.getAlarmMenu(Language.RU), false);
                break;
            case Commands.addAlarmRU:
                usersPositionMenu.put(chatId, Menus.ALARM_ADD);
                sendMessage(chatId, "Выберите время", AlarmCalendarMenu.initForAlarm(new java.util.Date(), Language.RU), false);
                break;
            case Commands.lookAlarmRU:
                usersPositionMenu.put(chatId, Menus.ALARM_LOOK);
                showAlarms(chatId);
                break;
            case Commands.addTasksRU:
                usersPositionMenu.put(chatId, Menus.ADD_TASK_NAME);
                sendMessage(chatId, "Напишите задачу", AddingListMenu.getMenu(Language.RU), false);
                break;
            case Commands.lookTasksRU:
                usersPositionMenu.put(chatId, Menus.LOOK_TASK);
                showTasks(chatId);
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
                if (message.equals("Отмена") || message.equals("Назад")) {
                    sendMessage(chatId, "Может без глупых имен? :)", false);
                    return;
                }
                message = message.toLowerCase();
                usersNames.put(chatId, message);
                userSound.put(chatId, true);
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
                userSound.put(chatId, User.isSoundOn(chatId));
                usersPositionMenu.put(chatId, Menus.MAIN_MENU);
                break;
            case (Menus.WRITING_LIST_NAME):
                int lang = userLang.get(chatId);
                if (message.equals(Commands.backCommandRU) || message.equals(Commands.cancelCommandRU)) {
                    usersPositionMenu.put(chatId, Menus.LIST_MENU);
                    sendMessage(chatId, ListMenu.getListIntroMessage(Language.RU),
                            ListMenu.getListMenu(Language.RU), false);
                    break;
                }
                if (organizer.data.base.connection.utils.List.createList(chatId, message))
                    sendMessage(chatId, AddingListMenu.getAddingListSuccessMessage(lang),false );
                else
                    sendMessage(chatId, AddingListMenu.getAddingListFailMessageRu(lang),false );
                usersPositionMenu.put(chatId, Menus.LIST_MENU);
                sendMessage(chatId, ListMenu.getListIntroMessage(lang), ListMenu.getListMenu(lang),false );
                break;
            case (Menus.CHECK_AVAILABLE_LIST):
                if (message.equals(Commands.backCommandRU) || message.equals(Commands.cancelCommandRU)) {
                    usersPositionMenu.put(chatId, Menus.LIST_MENU);
                    sendMessage(chatId, ListMenu.getListIntroMessage(Language.RU),
                            ListMenu.getListMenu(Language.RU), false);
                } else {
                    showList(chatId, message);
                    usersPositionMenu.put(chatId, Menus.LIST_DETAILS_MENU);
                }
                break;
            case Menus.ADD_LIST_ITEM:
                if (!message.equals(Commands.cancelCommandRU)) {
                    List.insertItemList(chatId, getOwner(userList.get(chatId)), getDescription(userList.get(chatId)), message);
                    sendMessage(chatId, "Элемент добавлен. Можете ввести следующий.", AddingListMenu.getMenu(Language.RU), false);
                    break;
                }
                usersPositionMenu.put(chatId, Menus.LIST_DETAILS_MENU);
                showList(chatId, userList.get(chatId));
                break;
            case Menus.ADD_MAIL_CHECKER:
                if (!message.equals(Commands.cancelCommandRU)) {
                    Mail.insertMailChecker(chatId, userMail.get(chatId), message);
                    sendMessage(chatId, "Фильтр добавлен. Можете ввести следующий.", AddingListMenu.getMenu(Language.RU),false);
                    break;
                }
                usersPositionMenu.put(chatId, Menus.CHECK_MAIL_FILTERS);
                showMail(chatId, userMail.get(chatId));
                break;
            case Menus.ADD_MAIL:
                if (!message.equals(Commands.cancelCommandRU)){
//                    Mail.insertMailChecker(chatId, userList.get(chatId), message);
                    usersPositionMenu.put(chatId, Menus.ADD_MAIL_PASSWORD);
                    userMail.put(chatId, message);
                    sendMessage(chatId, "Введите пароль для доступа к почте (по протоколу imap)\n" +
                            "Подробнее про imap можно посмотреть здесь: https://yandex.ru/support/mail/mail-clients.html", false);
                    break;
                }
                usersPositionMenu.put(chatId, Menus.MAIL_MENU);
                sendMessage(chatId, MailMenu.getMailMenuIntroMessage(Language.RU),
                        MailMenu.getMailMenu(Language.RU), false);
                break;
            case Menus.ADD_MAIL_PASSWORD:
                usersPositionMenu.put(chatId, Menus.MAIL_MENU);

                String ans = "Не удалось получить доступ к почте.";
                try {
                    if (MailCheck.isAlive(userMail.get(chatId), message)){
                        Mail.insertMail(chatId, userMail.get(chatId), message);
                        ans = "Ваша почта добавлена.";
                    }
                } catch (MessagingException ignored) { }
                sendMessage(chatId, ans,
                        MailMenu.getMailMenu(Language.RU), false);
                break;
            case Menus.CHECK_MAIL:
                if (message.equals(Commands.cancelCommandRU) || message.equals(Commands.backCommandRU)){
                    usersPositionMenu.put(chatId, Menus.MAIL_MENU);
                    sendMessage(chatId, MailMenu.getMailMenuIntroMessage(Language.RU),
                            MailMenu.getMailMenu(Language.RU), false);
                    break;
                }
                usersPositionMenu.put(chatId, Menus.CHECK_MAIL_FILTERS);
                showMail(chatId, message);
                break;
            case Menus.SHARE_LIST:
                if (!message.equals(Commands.cancelCommandRU) && !message.equals(Commands.backCommandRU)) {
                    if (List.shareList(chatId, getDescription(userList.get(chatId)), message.toLowerCase()))
                        sendMessage(chatId, "Вы поделились с пользователем своим списком. Можете поделиться с кем-нибудь еще :З", AddingListMenu.getMenu(Language.RU), false);
                    else
                        sendMessage(chatId, "Такого пользователя не существует... Ну или вы с ним уже поделились... А может даже просто что-то пошло не так. Можете попробовать поделиться Вашим списком с кем-нибудь еще :D", AddingListMenu.getMenu(Language.RU), false);
                    break;
                }
                usersPositionMenu.put(chatId, Menus.LIST_DETAILS_MENU);
                showList(chatId, userList.get(chatId));
                break;
            case Menus.LOOK_USERS_SHARED:
                if (message.equals(Commands.cancelCommandRU) || message.equals(Commands.backCommandRU)){
                    usersPositionMenu.put(chatId, Menus.LIST_DETAILS_MENU);
                    showList(chatId, userList.get(chatId));
                }
                break;
            case Menus.ADD_TASK_NAME:
                if (message.equals(Commands.cancelCommandRU) || message.equals(Commands.backCommandRU)){
                    usersPositionMenu.put(chatId, Menus.TASK_MENU);
                    sendMessage(chatId, TaskMenu.getTaskMenuIntroMessage(Language.RU),
                            TaskMenu.getTaskMenu(Language.RU), false);
                    break;
                }
                userTask.put(chatId, message);
                usersPositionMenu.put(chatId, Menus.ADD_TASK_TIME);
                sendMessage(chatId, "Выберете день для этой задачи", TaskCalendarMenu.initForAlarm(new java.util.Date(), Language.RU), false);
                break;
            case Menus.ADD_TASK_MINUTE_WAIT:
                if (message.equals(Commands.cancelCommandRU) || message.equals(Commands.backCommandRU)){
                    usersPositionMenu.put(chatId, Menus.TASK_MENU);
                    sendMessage(chatId, TaskMenu.getTaskMenuIntroMessage(Language.RU),
                            TaskMenu.getTaskMenu(Language.RU), false);
                    break;
                }
                int minute;
                try{
                    minute = Integer.parseInt(message);
                    if (minute < 1){
                        sendMessage(chatId, "Число минут должно быть положительным числом");
                        break;
                    }
                    usersPositionMenu.put(chatId, Menus.TASK_MENU);
                    if (Task.addTask(chatId, userTask.get(chatId), userTaskTime.get(chatId), minute))
                        sendMessage(chatId, TaskMenu.getTaskMenuIntroMessage(Language.RU),
                                TaskMenu.getTaskMenu(Language.RU), false);
                    else
                        sendMessage(chatId, TaskMenu.getErrorMessage(Language.RU),
                                TaskMenu.getTaskMenu(Language.RU), false);
                    break;
                } catch (Exception e){
                    sendMessage(chatId, "Это не целое число минут, попробуйте еще раз.", false);
                }
        }
    }

    private void showShareList(long chatId){
        Integer lang = userLang.get(chatId);
        ArrayList<String> users = List.getSharedUsers(chatId, getOwner(userList.get(chatId)), getDescription(userList.get(chatId)));
        if (users == null){
            sendMessage(chatId, "Такой листа не существует...",
                    ListMenu.getListMenu(lang), false);
            usersPositionMenu.put(chatId, Menus.LIST_MENU);
            return;
        }
        for( var user : users){
            java.util.List<java.util.List<InlineKeyboardButton> > buttons = new ArrayList<>();
            java.util.List<InlineKeyboardButton> buttons1 = new ArrayList<>(0);
            buttons1.add(new InlineKeyboardButton().setText("Удалить").setCallbackData("deleteU"));
            buttons.add(buttons1);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(buttons);
            sendMessage(chatId, user, inlineKeyboardMarkup, false);
        }
        {
            ReplyKeyboardMarkup menu_back = new ReplyKeyboardMarkup();
            menu_back
                    .setSelective(true)
                    .setResizeKeyboard(true)
                    .setOneTimeKeyboard(true);
            KeyboardButton[] btns = new KeyboardButton[1];
            btns[0] = new KeyboardButton(Commands.backCommandRU);
            java.util.List<KeyboardRow> keyboard = new ArrayList<>();
            for (int i = 0; i < btns.length; i++) {
                KeyboardRow row = new KeyboardRow();
                row.add(btns[i]);
                keyboard.add(row);
            }
            menu_back.setKeyboard(keyboard);
            sendMessage(chatId, "Есть кнопочка вернуться)) Загляни!", menu_back, false);
        }
    }

    private void showMail(long chatId, String message){
        Integer lang = userLang.get(chatId);
        String[] filters = Mail.userMailFilter(chatId, message);
        if (filters == null){
            sendMessage(chatId, "Такой почты не существует...",
                    MailMenu.getMailMenu(lang), false);
            usersPositionMenu.put(chatId, Menus.MAIL_MENU);
            return;
        }
        userMail.put(chatId, message);
        for( var item : filters){
            java.util.List<java.util.List<InlineKeyboardButton> > buttons = new ArrayList<>();
            java.util.List<InlineKeyboardButton> buttons1 = new ArrayList<>(0);
            buttons1.add(new InlineKeyboardButton().setText("Удалить").setCallbackData("deleteM"));
            buttons.add(buttons1);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(buttons);
            sendMessage(chatId, item, inlineKeyboardMarkup, false);
        }
        {
            ReplyKeyboardMarkup menu_back = new ReplyKeyboardMarkup();
            menu_back
                    .setSelective(true)
                    .setResizeKeyboard(true)
                    .setOneTimeKeyboard(true);
            KeyboardButton[] btns = new KeyboardButton[3];
            btns[0] = new KeyboardButton(Commands.addFilterRU);
            btns[1] = new KeyboardButton(Commands.removeMailRU);
            btns[2] = new KeyboardButton(Commands.backCommandRU);
            java.util.List<KeyboardRow> keyboard = new ArrayList<>();
            for (int i = 0; i < btns.length; i++) {
                KeyboardRow row = new KeyboardRow();
                row.add(btns[i]);
                keyboard.add(row);
            }
            menu_back.setKeyboard(keyboard);
            sendMessage(chatId, "Меню фильтров", menu_back, false);
        }
    }

    private String getDescription(String message){
        String[] tmp = message.split("\\(");
        String ans = tmp[0];
        for (int i = 1; i < tmp.length - 1; i++){
            ans += "(" + tmp[i];
        }
        return ans.substring(0, ans.length() - 1);
    }

    private String getOwner(String message){
        String[] tmp = message.split("\\(");
        String ans = tmp[tmp.length - 1].substring(0,tmp[tmp.length - 1].length() - 1);
        return ans;
    }

    private void showList(long chatId, String message){
        Integer lang = userLang.get(chatId);
        String[] item_lists = List.getListItems(getDescription(message), getOwner(message));
        if (item_lists == null){
            sendMessage(chatId, "Такого списка не существует...",
                    ListMenu.getListMenu(lang), false);
            usersPositionMenu.put(chatId, Menus.LIST_MENU);
            return;
        }
        userList.put(chatId, message);
        for( var item : item_lists){
            java.util.List<java.util.List<InlineKeyboardButton> > buttons = new ArrayList<>();
            java.util.List<InlineKeyboardButton> buttons1 = new ArrayList<>(0);
            buttons1.add(new InlineKeyboardButton().setText("Удалить").setCallbackData("deleteL"));
            buttons.add(buttons1);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(buttons);
            sendMessage(chatId, item, inlineKeyboardMarkup, false);
        }
        {
            boolean isOwner = User.isOwner(chatId, getOwner(userList.get(chatId)));
            ReplyKeyboardMarkup menu_back = new ReplyKeyboardMarkup();
            menu_back
                    .setSelective(true)
                    .setResizeKeyboard(true)
                    .setOneTimeKeyboard(true);
            KeyboardButton[] btns;
            if (isOwner) {
                btns = new KeyboardButton[5];
                btns[0] = new KeyboardButton(Commands.addItemRU);
                btns[1] = new KeyboardButton(Commands.shareListRU);
                btns[2] = new KeyboardButton(Commands.lookSharePersonsRU);
                btns[3] = new KeyboardButton(Commands.removeListRU);
                btns[4] = new KeyboardButton(Commands.backCommandRU);
            } else {
                btns = new KeyboardButton[3];
                btns[0] = new KeyboardButton(Commands.addItemRU);
                btns[1] = new KeyboardButton(Commands.removeListRU);
                btns[2] = new KeyboardButton(Commands.backCommandRU);
            }
            java.util.List<KeyboardRow> keyboard = new ArrayList<>();
            for (KeyboardButton btn : btns) {
                KeyboardRow row = new KeyboardRow();
                row.add(btn);
                keyboard.add(row);
            }
            menu_back.setKeyboard(keyboard);
            sendMessage(chatId, "Меню списка", menu_back, false);
        }
    }

    private void showAlarms(long chatId){
        Integer lang = userLang.get(chatId);
        ArrayList<String[]> alarms = Alarm.getUserAlarm(chatId);
        if (alarms == null){
            sendMessage(chatId, "Что-то не так с будильниками...",
                    AlarmMenu.getAlarmMenu(lang), false);
            usersPositionMenu.put(chatId, Menus.ALARM_MENU);
            return;
        }
        for( var alarm : alarms){
            java.util.List<java.util.List<InlineKeyboardButton> > buttons = new ArrayList<>();
            java.util.List<InlineKeyboardButton> buttons1 = new ArrayList<>(0);
            buttons1.add(new InlineKeyboardButton().setText("Удалить").setCallbackData("deleteA/" + alarm[0]));
            buttons.add(buttons1);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(buttons);
            sendMessage(chatId, alarm[1], inlineKeyboardMarkup, false);
        }
        {
            ReplyKeyboardMarkup menu_back = new ReplyKeyboardMarkup();
            menu_back
                    .setSelective(true)
                    .setResizeKeyboard(true)
                    .setOneTimeKeyboard(true);
            KeyboardButton[] btns = new KeyboardButton[1];
            btns[0] = new KeyboardButton(Commands.backCommandRU);
            java.util.List<KeyboardRow> keyboard = new ArrayList<>();
            for (int i = 0; i < btns.length; i++) {
                KeyboardRow row = new KeyboardRow();
                row.add(btns[i]);
                keyboard.add(row);
            }
            menu_back.setKeyboard(keyboard);
            sendMessage(chatId, "Есть кнопочка вернуться)) Загляни!", menu_back, false);
        }
    }

    private void showTasks(long chatId){
        Integer lang = userLang.get(chatId);
        ArrayList<TaskEntity> tasks = Task.getUserTasks(chatId);
        if (tasks == null){
            sendMessage(chatId, "Что-то не так с задачами...",
                    TaskMenu.getTaskMenu(lang), false);
            usersPositionMenu.put(chatId, Menus.TASK_MENU);
            return;
        }
        for( var task : tasks){
            java.util.List<java.util.List<InlineKeyboardButton> > buttons = new ArrayList<>();
            java.util.List<InlineKeyboardButton> buttons1 = new ArrayList<>(0);
            buttons1.add(new InlineKeyboardButton().setText("Удалить").setCallbackData("deleteT/" + task.getChatID() + "/" + task.getTask_id()));
            buttons.add(buttons1);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(buttons);
            sendMessage(chatId, task.getName() + "\n" + task.getTime() + "\nБудет напоминать каждые " + task.getPeriod() + " минуты.", inlineKeyboardMarkup, false);
        }
        {
            ReplyKeyboardMarkup menu_back = new ReplyKeyboardMarkup();
            menu_back
                    .setSelective(true)
                    .setResizeKeyboard(true)
                    .setOneTimeKeyboard(true);
            KeyboardButton[] btns = new KeyboardButton[1];
            btns[0] = new KeyboardButton(Commands.backCommandRU);
            java.util.List<KeyboardRow> keyboard = new ArrayList<>();
            for (int i = 0; i < btns.length; i++) {
                KeyboardRow row = new KeyboardRow();
                row.add(btns[i]);
                keyboard.add(row);
            }
            menu_back.setKeyboard(keyboard);
            sendMessage(chatId, "Есть кнопочка вернуться)) Загляни!", menu_back, false);
        }
    }


    /**
     * Разгрузочный метод, определяющий состояние меню (активно оно или нет)
     * @param message сообщение, которое отправил пользователь
     * @param chatId id чата, из которого пришло сообщение, и куда отвечать, если надо.
     */
    private void messageController(String message, long chatId){
        Integer state = usersPositionMenu.get(chatId);
        if (Menus.isEditing(state)/* && !Commands.isCancel(message)*/)
            editController(message, chatId);
        else
            commandController(message, chatId);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (!working){
                new Thread(() ->{
                    while(true){
                        try{ ArrayList<String[]> mails_to_check = Mail.allMails();
                            for(String[] mail_info : mails_to_check){
                                String[] filters = Mail.userMailFilter(Long.parseLong(mail_info[0]), mail_info[1]);
                                try {
                                    ArrayList<String> messages = MailCheck.checkMail(mail_info[1], mail_info[2], filters, Date.from(Timestamp.valueOf(mail_info[3]).toInstant()));
                                    for (var message : messages)
                                        sendMessage(Long.parseLong(mail_info[0]),message);
                                    Mail.updateMailChecker(Long.parseLong(mail_info[0]), mail_info[1]);
                                } catch (MessagingException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception ignored) { }
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }

                }).start();
                new Thread(() ->{
                    while(true){
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MINUTE, 5);
                        ArrayList<Long> alarmsWORK = Alarm.getAllAlarmForNow();
                        for(Long alarm : alarmsWORK){
                            new Thread(() -> {
                                alarmEnd.put(this.hashCode(), 0);
                                java.util.List<java.util.List<InlineKeyboardButton> > buttons = new ArrayList<>();
                                java.util.List<InlineKeyboardButton> buttons1 = new ArrayList<>(0);
                                buttons1.add(new InlineKeyboardButton().setText("Успокойся!").setCallbackData("stopA/" + this.hashCode()));
                                buttons.add(buttons1);
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                inlineKeyboardMarkup.setKeyboard(buttons);
                                Calendar cal1 = Calendar.getInstance();
                                cal1.add(Calendar.MINUTE, 2);
                                while (cal1.compareTo(Calendar.getInstance()) != -1 && alarmEnd.get(this.hashCode()) != 10){

                                    SendMessage sendMessage = new SendMessage()
                                            .setChatId(alarm)
                                            .setReplyMarkup(inlineKeyboardMarkup)
                                            .setText("Встваааааай :D\nТы не остановишь этот спам :D")
                                            .enableNotification();
                                    try {
                                        Message id = execute(sendMessage);
                                        Thread.sleep(5000);
                                        DeleteMessage del = new DeleteMessage();
                                        del.setChatId(id.getChatId());
                                        del.setMessageId(id.getMessageId());
                                        execute(del);
                                    } catch (TelegramApiException e) {
                                        BotLogger.error(this.getClass().getName(), e.getMessage(), e);
                                    } catch (InterruptedException ignored) { }
                                }
                            }).start();
                        }
                        while (cal.compareTo(Calendar.getInstance()) != -1)
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                return;
                            }
                    }

                }).start();
                new Thread(() ->{
                    while(true){
                        ArrayList<TaskEntity> tasksWORK = Task.getTasksForNow();
                        for(TaskEntity task : tasksWORK){
                            new Thread(() -> {
                                java.util.List<java.util.List<InlineKeyboardButton> > buttons = new ArrayList<>();
                                java.util.List<InlineKeyboardButton> buttons1 = new ArrayList<>(0);
                                buttons1.add(new InlineKeyboardButton().setText("Сделано!").setCallbackData("stopT/" + task.hashCode() + "/" + task.getChatID() +"/" + task.getTask_id()));
                                Task.completeTask(task.getChatID(), task.getTask_id());
                                buttons.add(buttons1);
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                inlineKeyboardMarkup.setKeyboard(buttons);
                                taskEnd.put(task.hashCode(), false);
                                while (!taskEnd.get(task.hashCode())){
                                    SendMessage sendMessage = new SendMessage()
                                            .setChatId(task.getChatID())
                                            .setText(task.getName())
                                            .setReplyMarkup(inlineKeyboardMarkup)
                                            .enableNotification();
                                    try {
                                        Message id = execute(sendMessage);
                                        Thread.sleep(task.getPeriod()*60000L);
                                        DeleteMessage del = new DeleteMessage();
                                        del.setChatId(id.getChatId());
                                        del.setMessageId(id.getMessageId());
                                        execute(del);
                                    } catch (TelegramApiException e) {
                                        sendMessage(task.getChatID(), userLang.get(task.getChatID()) == Language.RU ? "Таск \"" + task.getName() + "\" прекратил уведомления" : "task stop working", false);
                                        taskEnd.put(task.hashCode(), true);
                                    } catch (InterruptedException e) {
                                        BotLogger.error(this.getClass().getName(), e.getMessage(), e); }
                                }
                            }).start();
                        }
                        try {
                            Thread.sleep(280000);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }).start();
                working = true;
            }

            BotLogger.debug(this.getClass().getName(), update.getMessage().getText());

            String message = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            DeleteMessage password = new DeleteMessage();
            password.setChatId(update.getMessage().getChatId());
            password.setMessageId(update.getMessage().getMessageId());
            try {
                execute(password);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            if (userMessages.get(chat_id) != null){
                for (int i = 0; i < userMessages.get(chat_id).size(); i++){
                    DeleteMessage mes = new DeleteMessage();
                    mes.setChatId(chat_id);
                    mes.setMessageId(userMessages.get(chat_id).get(i));
                    try {
                        execute(mes);
                    } catch (TelegramApiException ignored) { }
                }
                userMessages.put(chat_id, new ArrayList<>(0));
            }
            if(userMessages.get(chat_id) == null)
                message = "/start";
            messageController(message, chat_id);
        } else if (update.hasCallbackQuery()){
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            if (update.getCallbackQuery().getData().contains("stopT")) {
                String[] data = update.getCallbackQuery().getData().split("/");
                int hash = Integer.parseInt(data[1]);
                taskEnd.put(hash, true);
                DeleteMessage message = new DeleteMessage();
                message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            }
            if (update.getCallbackQuery().getData().contains("stopA")) {
                int hash = Integer.parseInt(update.getCallbackQuery().getData().split("/")[1]);
                DeleteMessage message = new DeleteMessage();
                message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                alarmEnd.put(hash, alarmEnd.get(hash) + 1);
                return;
            }
            if (update.getCallbackQuery().getData().equals("deleteL")) {
                if (List.deleteItemList(chat_id,
                        getOwner(userList.get(chat_id)),
                        getDescription(userList.get(chat_id)),
                        update.getCallbackQuery().getMessage().getText())){
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            if (update.getCallbackQuery().getData().equals("deleteM")) {
                if (Mail.deleteMailChecker(chat_id,
                        userMail.get(chat_id),
                        update.getCallbackQuery().getMessage().getText())){
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            if (update.getCallbackQuery().getData().equals("deleteU")) {
                if (List.stopShareList(chat_id,
                        getDescription(userList.get(chat_id)),
                        update.getCallbackQuery().getMessage().getText())){
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            if (update.getCallbackQuery().getData().contains("deleteA")) {
                int id = Integer.parseInt(update.getCallbackQuery().getData().split("/")[1]);
                if (Alarm.deleteAlarm(id)){
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            if (update.getCallbackQuery().getData().contains("deleteT")) {
                long chatId = Long.parseLong(update.getCallbackQuery().getData().split("/")[1]);
                long taskId = Long.parseLong(update.getCallbackQuery().getData().split("/")[2]);
                if (Task.deleteTask(chatId, taskId)){
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            if (usersPositionMenu.get(chat_id) == Menus.ALARM_ADD){
                if (update.getCallbackQuery().getData().contains("назад")){
                    usersPositionMenu.put(chat_id, Menus.getUpperMenuNumber(usersPositionMenu.get(chat_id)));
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(chat_id);
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMessage(chat_id, AlarmMenu.getAlarmMenuIntroMessage(Language.RU),
                            AlarmMenu.getAlarmMenu(Language.RU), false);
                    return;
                }
                String[] data = update.getCallbackQuery().getData().split("/");

                if (!data[data.length-1].equals("alarm"))
                    return;
                if (data.length == 4) {
                    if (data[2].contains("<") || data[2].contains(">")){
                        //Нажаты стрелочки
                        int month = Integer.parseInt(data[0]);
                        int year = Integer.parseInt(data[1]);
                        Calendar newDate = new GregorianCalendar();;
                        newDate.set(Calendar.YEAR, year);
                        newDate.set(Calendar.MONTH, month);
                        switch (data[2]) {
                            case "<<":
                                newDate.set(Calendar.YEAR, year - 1);
                                newDate.set(Calendar.MONTH, month);
                                break;
                            case "<":
                                newDate.set(Calendar.YEAR, year);
                                newDate.set(Calendar.MONTH, month - 1);
                                break;
                            case ">":
                                newDate.set(Calendar.YEAR, year);
                                newDate.set(Calendar.MONTH, month + 1);
                                break;
                            case ">>":
                                newDate.set(Calendar.YEAR, year + 1);
                                newDate.set(Calendar.MONTH, month);
                                break;
                        }
                        DeleteMessage message = new DeleteMessage();
                        message.setChatId(chat_id);
                        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        sendMessage(chat_id, "Выберете дату", AlarmCalendarMenu.initForAlarm(newDate.getTime(), Language.RU), false);
                    } else {
                        //Выбрана дата
                        int month = Integer.parseInt(data[0]);
                        int year = Integer.parseInt(data[1]);
                        int day = Integer.parseInt(data[2]);
                        usersPositionMenu.put(chat_id, Menus.ALARM_ADD_HOUR);
                        DeleteMessage message = new DeleteMessage();
                        message.setChatId(chat_id);
                        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        sendMessage(chat_id, "Выберете час", AlarmClockMenu.initHourForAlarm(year, month, day, null), false);
                    }
                }
                if (data.length == 2) {
                    //Выбран день
                    usersPositionMenu.put(chat_id, Menus.ALARM_ADD_HOUR);
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(chat_id);
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMessage(chat_id, "Выберете час", AlarmClockMenu.initHourForAlarm(null, null, null, data[0]), false);
                }
                return;
            }
            if (usersPositionMenu.get(chat_id) == Menus.ALARM_ADD_HOUR){
                if (update.getCallbackQuery().getData().contains("назад")){
                    usersPositionMenu.put(chat_id, Menus.getUpperMenuNumber(usersPositionMenu.get(chat_id)));
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(chat_id);
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMessage(chat_id, AlarmMenu.getAlarmMenuIntroMessage(Language.RU),
                            AlarmMenu.getAlarmMenu(Language.RU), false);
                    return;
                }
                String[] data = update.getCallbackQuery().getData().split("/");
                usersPositionMenu.put(chat_id, Menus.ALARM_ADD_MINUTE);
                DeleteMessage message = new DeleteMessage();
                message.setChatId(chat_id);
                message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                if (data.length == 5){
                    //Выбрана была дата
                    int month = Integer.parseInt(data[0]);
                    int year = Integer.parseInt(data[1]);
                    int day = Integer.parseInt(data[2]);
                    int hour = Integer.parseInt(data[3]);
                    sendMessage(chat_id, "Выберете минуту", AlarmClockMenu.initMinuteForAlarm(year, month, day, hour, null), false);
                }
                if (data.length == 3){
                    //Выбран день
                    int hour = Integer.parseInt(data[1]);
                    sendMessage(chat_id, "Выберете минуту", AlarmClockMenu.initMinuteForAlarm(null, null, null, hour, data[0]), false);
                }
                return;
            }
            if (usersPositionMenu.get(chat_id) == Menus.ALARM_ADD_MINUTE) {
                DeleteMessage message = new DeleteMessage();
                message.setChatId(chat_id);
                message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                if (update.getCallbackQuery().getData().equals("назадalarm")){
                    usersPositionMenu.put(chat_id, Menus.getUpperMenuNumber(usersPositionMenu.get(chat_id)));
                    sendMessage(chat_id, AlarmMenu.getAlarmMenuIntroMessage(Language.RU),
                            AlarmMenu.getAlarmMenu(Language.RU), false);
                    return;
                }
                String[] data = update.getCallbackQuery().getData().split("/");
                if (data.length == 6){
                    //Выбор даты
                    int month = Integer.parseInt(data[0]);
                    int year = Integer.parseInt(data[1]);
                    int day = Integer.parseInt(data[2]);
                    int hour = Integer.parseInt(data[3]);
                    int minute = Integer.parseInt(data[4]);
                    Calendar cal = new GregorianCalendar();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, day);
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, minute);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    Timestamp time = Timestamp.from(cal.toInstant());
                    Alarm.addAlarm(chat_id, time, -1);
                }
                if (data.length == 4){
                    //Выбор дня
                    int hour = Integer.parseInt(data[1]);
                    int minute = Integer.parseInt(data[2]);

                    Calendar cal = new GregorianCalendar();;
                    cal.set(Calendar.YEAR, 2000);
                    cal.set(Calendar.MONTH, 1);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, minute);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);Timestamp time = Timestamp.from(cal.toInstant());
                    Alarm.addAlarm(chat_id, time, stringToIntegerday(data[0]));
                }
                sendMessage(chat_id, AlarmMenu.getAlarmMenuIntroMessage(Language.RU),
                        AlarmMenu.getAlarmMenu(Language.RU), false);
                return;
            }
            if (usersPositionMenu.get(chat_id) == Menus.ADD_TASK_TIME){
                if (update.getCallbackQuery().getData().equals("назадtask")){
                    usersPositionMenu.put(chat_id, Menus.getUpperMenuNumber(usersPositionMenu.get(chat_id)));
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(chat_id);
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMessage(chat_id, TaskMenu.getTaskMenuIntroMessage(Language.RU),
                            TaskMenu.getTaskMenu(Language.RU), false);
                    return;
                }
                String[] data = update.getCallbackQuery().getData().split("/");

                if (!data[data.length-1].equals("task"))
                    return;
                if (data.length == 4) {
                    if (data[2].contains("<") || data[2].contains(">")){
                        //Нажаты стрелочки
                        int month = Integer.parseInt(data[0]);
                        int year = Integer.parseInt(data[1]);
                        Calendar newDate = new GregorianCalendar();;
                        newDate.set(Calendar.YEAR, year);
                        newDate.set(Calendar.MONTH, month);
                        switch (data[2]) {
                            case "<<":
                                newDate.set(Calendar.YEAR, year - 1);
                                newDate.set(Calendar.MONTH, month);
                                break;
                            case "<":
                                newDate.set(Calendar.YEAR, year);
                                newDate.set(Calendar.MONTH, month - 1);
                                break;
                            case ">":
                                newDate.set(Calendar.YEAR, year);
                                newDate.set(Calendar.MONTH, month + 1);
                                break;
                            case ">>":
                                newDate.set(Calendar.YEAR, year + 1);
                                newDate.set(Calendar.MONTH, month);
                                break;
                        }
                        DeleteMessage message = new DeleteMessage();
                        message.setChatId(chat_id);
                        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        sendMessage(chat_id, "Выберете дату", AlarmCalendarMenu.initForAlarm(newDate.getTime(), Language.RU), false);
                    } else {
                        //Выбрана дата
                        int month = Integer.parseInt(data[0]);
                        int year = Integer.parseInt(data[1]);
                        int day = Integer.parseInt(data[2]);
                        usersPositionMenu.put(chat_id, Menus.ADD_TASK_HOUR);
                        DeleteMessage message = new DeleteMessage();
                        message.setChatId(chat_id);
                        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        sendMessage(chat_id, "Со скольки начать напоминать?", TaskClockMenu.initHourForAlarm(year, month, day), false);
                    }
                }
                return;
            }
            if (usersPositionMenu.get(chat_id) == Menus.ADD_TASK_HOUR){
                if (update.getCallbackQuery().getData().contains("назадtask")){
                    usersPositionMenu.put(chat_id, Menus.getUpperMenuNumber(usersPositionMenu.get(chat_id)));
                    DeleteMessage message = new DeleteMessage();
                    message.setChatId(chat_id);
                    message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMessage(chat_id, TaskMenu.getTaskMenuIntroMessage(Language.RU),
                            TaskMenu.getTaskMenu(Language.RU), false);
                    return;
                }
                String[] data = update.getCallbackQuery().getData().split("/");
                usersPositionMenu.put(chat_id, Menus.ADD_TASK_MINUTE_WAIT);
                DeleteMessage message = new DeleteMessage();
                message.setChatId(chat_id);
                message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                if (data.length == 5){
                    //Выбрана была дата
                    int month = Integer.parseInt(data[0]);
                    int year = Integer.parseInt(data[1]);
                    int day = Integer.parseInt(data[2]);
                    int hour = Integer.parseInt(data[3]);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, day);
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    userTaskTime.put(chat_id, Timestamp.from(cal.toInstant()));
                    sendMessage(chat_id, "Каждые сколько минут уведомлять о задаче?", AddingListMenu.getMenu(Language.RU), false);
                }
                return;
            }
        }
    }

    private static Integer stringToIntegerday(String day){
        return switch (day){
            case "Пн" -> 0;
            case "Вт" -> 1;
            case "Ср" -> 2;
            case "Чт" -> 3;
            case "Пт" -> 4;
            case "Сб" -> 5;
            case "Вс" -> 6;
            default -> 0;
        };
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
