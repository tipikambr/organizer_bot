package organizer.bot.utils.menus.mail;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.constants.Commands;
import organizer.bot.utils.constants.Language;
import organizer.bot.utils.menus.MenuPattern;

import java.util.ArrayList;
import java.util.List;

public class MailController extends MenuPattern {
    private final static ReplyKeyboardMarkup mailControllerRU;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////



    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        mailControllerRU = new ReplyKeyboardMarkup();
        mailControllerRU
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.addMailFilterCommandRU),
                new KeyboardButton(Commands.lookMailFilterCommandRU),
                new KeyboardButton(Commands.soundSpecificMailCommandRU),
                new KeyboardButton(Commands.backCommandRU)};

        fillKeyBoard(keyboard, btns);

        mailControllerRU.setKeyboard(keyboard);
    }
    public static ReplyKeyboardMarkup getMailController(int language){
        switch (language){
            case Language.RU:
                return mailControllerRU;
            default:
                return mailControllerRU;
        }
    }

}
