package organizer.bot.utils.menus.list;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.Commands;
import organizer.bot.utils.Language;
import organizer.bot.utils.menus.MenuPattern;

import java.util.ArrayList;
import java.util.List;

public class ListMenu extends MenuPattern {
    private final static ReplyKeyboardMarkup listMenuRU;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////

    private final static String LIST_INTRO_MESSAGE_RU = "Вы зашли в раздел настроек списков. Что вы хотели сделать?";

    public static String getListIntroMessage(int language){
        switch (language){
            case Language.RU:
                return LIST_INTRO_MESSAGE_RU;
            default:
                return LIST_INTRO_MESSAGE_RU;
        }
    }

    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        listMenuRU = new ReplyKeyboardMarkup();
        listMenuRU
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.addListCommandRU),
                new KeyboardButton(Commands.lookAllListsCommandRU),
                new KeyboardButton(Commands.soundListsCommandRU),
                new KeyboardButton(Commands.backCommandRU)};

        fillKeyBoard(keyboard, btns);

        listMenuRU.setKeyboard(keyboard);
    }

    public static ReplyKeyboardMarkup getListMenu(int language){
        switch (language){
            case Language.RU:
                return listMenuRU;
            default:
                return listMenuRU;
        }
    }


}
