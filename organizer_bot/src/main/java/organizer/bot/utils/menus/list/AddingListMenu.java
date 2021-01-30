package organizer.bot.utils.menus.list;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.Commands;
import organizer.bot.utils.Language;
import organizer.bot.utils.menus.MenuPattern;

import java.util.ArrayList;
import java.util.List;

public class AddingListMenu extends MenuPattern {
    private final static ReplyKeyboardMarkup menuRU;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////

    private final static String ADDING_LIST_INTRO_MESSAGE_RU = "Введите название листа";
    private final static String ADDING_LIST_SUCCESS_MESSAGE_RU = "Лист был успешно создан";
    private final static String ADDING_LIST_FAIL_MESSAGE_RU = "Такой лист уже есть";

    public static String getAddingListIntroMessage(int language){
        switch (language){
            case Language.RU:
                return ADDING_LIST_INTRO_MESSAGE_RU;
            default:
                return ADDING_LIST_INTRO_MESSAGE_RU;
        }
    }

    public static String getAddingListSuccessMessage(int language){
        switch (language){
            case Language.RU:
                return ADDING_LIST_SUCCESS_MESSAGE_RU;
            default:
                return ADDING_LIST_SUCCESS_MESSAGE_RU;
        }
    }

    public static String getAddingListFailMessageRu(int language){
        switch (language){
            case Language.RU:
                return ADDING_LIST_FAIL_MESSAGE_RU;
            default:
                return ADDING_LIST_FAIL_MESSAGE_RU;
        }
    }

    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        menuRU = new ReplyKeyboardMarkup();
        menuRU
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.cancelCommandRU)
        };

        fillKeyBoard(keyboard, btns);

        menuRU.setKeyboard(keyboard);
    }

    public static ReplyKeyboardMarkup getMenu(int language){
        switch (language){
            case Language.RU:
                return menuRU;
            default:
                return menuRU;
        }
    }
}
