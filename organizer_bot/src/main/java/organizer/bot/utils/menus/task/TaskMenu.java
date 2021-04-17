package organizer.bot.utils.menus.task;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.constants.Commands;
import organizer.bot.utils.constants.Language;
import organizer.bot.utils.menus.MenuPattern;

import java.util.ArrayList;
import java.util.List;

public class TaskMenu extends MenuPattern {
    private final static ReplyKeyboardMarkup taskMenuRU;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////

    private final static String TASK_MENU_INTRO_MESSAGE_RU = "Вы зашли в раздел задач. Что вы хотели сделать?";
    private final static String ERROR_MESSAGE_RU = "Произошла ошибка при добавлении задачи";

    public static String getTaskMenuIntroMessage(int language){
        switch (language){
            case Language.RU:
                return TASK_MENU_INTRO_MESSAGE_RU;
            default:
                return TASK_MENU_INTRO_MESSAGE_RU;
        }
    }
    public static String getErrorMessage(int language){
        switch (language){
            case Language.RU:
                return ERROR_MESSAGE_RU;
            default:
                return ERROR_MESSAGE_RU;
        }
    }


    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        taskMenuRU = new ReplyKeyboardMarkup();
        taskMenuRU
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.addTasksRU),
                new KeyboardButton(Commands.lookTasksRU),
//                new KeyboardButton(Commands.soundMailsCommandRU),
                new KeyboardButton(Commands.backCommandRU)};

        fillKeyBoard(keyboard, btns);

        taskMenuRU.setKeyboard(keyboard);
    }
    public static ReplyKeyboardMarkup getTaskMenu(int language){
        switch (language){
            case Language.RU:
                return taskMenuRU;
            default:
                return taskMenuRU;
        }
    }
}
