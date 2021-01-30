package organizer.bot.utils.menus.mail;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.Commands;
import organizer.bot.utils.Language;
import organizer.bot.utils.menus.MenuPattern;

import java.util.ArrayList;
import java.util.List;

public class MailMenu extends MenuPattern {
    private final static ReplyKeyboardMarkup mailMenuRU;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////

    private final static String MAIL_MENU_INTRO_MESSAGE_RU = "Вы зашли в раздел почтовых ящиков. Что вы хотели сделать?";

    public static String getMailMenuIntroMessage(int language){
        switch (language){
            case Language.RU:
                return MAIL_MENU_INTRO_MESSAGE_RU;
            default:
                return MAIL_MENU_INTRO_MESSAGE_RU;
        }
    }

    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        mailMenuRU = new ReplyKeyboardMarkup();
        mailMenuRU
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.addMailRU),
                new KeyboardButton(Commands.lookMailsRU),
                new KeyboardButton(Commands.soundMailsCommandRU),
                new KeyboardButton(Commands.backCommandRU)};

        fillKeyBoard(keyboard, btns);

        mailMenuRU.setKeyboard(keyboard);
    }
    public static ReplyKeyboardMarkup getMailMenu(int language){
        switch (language){
            case Language.RU:
                return mailMenuRU;
            default:
                return mailMenuRU;
        }
    }
}
