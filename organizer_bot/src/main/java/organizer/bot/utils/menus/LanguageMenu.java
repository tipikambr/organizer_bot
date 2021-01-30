package organizer.bot.utils.menus;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.Commands;
import organizer.bot.utils.Language;

import java.util.ArrayList;
import java.util.List;

public class LanguageMenu extends MenuPattern {
    private final static ReplyKeyboardMarkup languageMenuRU;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////

    private final static String LANGUAGE_INTRO_MESSAGE_RU = "Вы зашли в раздел настроек языка. Что вы хотели сделать?";

    public static String getLanguageIntroMessageRuIntroMessage(int language){
        switch (language){
            case Language.RU:
                return LANGUAGE_INTRO_MESSAGE_RU;
            default:
                return LANGUAGE_INTRO_MESSAGE_RU;
        }
    }

    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        languageMenuRU = new ReplyKeyboardMarkup();
        languageMenuRU
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.russianLanguageCommand),
                new KeyboardButton(Commands.backCommandRU)};

        fillKeyBoard(keyboard, btns);

        languageMenuRU.setKeyboard(keyboard);
    }

    public static ReplyKeyboardMarkup getLanguageMenu(int language){
        switch (language){
            case Language.RU:
                return languageMenuRU;
            default:
                return languageMenuRU;
        }
    }

}
