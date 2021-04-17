package organizer.bot.utils.menus.mail;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.constants.Commands;
import organizer.bot.utils.constants.Language;
import organizer.bot.utils.menus.MenuPattern;

import java.util.ArrayList;
import java.util.List;

public class MailViewer extends MenuPattern {
    private final static String MAIL_VIEWER_MESSAGE_RU = "Какая из почт вам интересна?";

    public static String getMailViewerMessage(int language){
        switch (language){
            case Language.RU:
                return MAIL_VIEWER_MESSAGE_RU;
            default:
                return MAIL_VIEWER_MESSAGE_RU;
        }
    }


    public static ReplyKeyboardMarkup getMailsMarkap(String[] mails){
        ReplyKeyboardMarkup lists_menu = new ReplyKeyboardMarkup();
        lists_menu
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);
        KeyboardButton[] btns = new KeyboardButton[mails.length + 1];
        for (int i = 0; i < mails.length; i++)
            btns[i] = new KeyboardButton(mails[i]);
        btns[mails.length] = new KeyboardButton(Commands.backCommandRU);
        List<KeyboardRow> keyboard = new ArrayList<>();
        fillKeyBoard(keyboard, btns);
        lists_menu.setKeyboard(keyboard);
        return lists_menu;
    }
}
