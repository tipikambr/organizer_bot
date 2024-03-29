package organizer.bot.utils.menus.list;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.constants.Commands;
import organizer.bot.utils.constants.Language;
import organizer.bot.utils.menus.MenuPattern;
import organizer.data.base.connection.utils.User;

import java.util.ArrayList;
import java.util.List;

public class ListViewer extends MenuPattern {
    private final static String LIST_VIEWER_MESSAGE_RU = "Какой из списков вам интересен?";

    public static String getListViewerMessage(int language){
        switch (language){
            case Language.RU:
                return LIST_VIEWER_MESSAGE_RU;
            default:
                return LIST_VIEWER_MESSAGE_RU;
        }
    }

    public static ReplyKeyboardMarkup getListsMarkap(ArrayList<ArrayList<String>> lists){
        ReplyKeyboardMarkup lists_menu = new ReplyKeyboardMarkup();
        lists_menu
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);
        KeyboardButton[] btns = new KeyboardButton[lists.get(0).size() + 1];
        for (int i = 0; i < lists.get(0).size(); i++)
            btns[i] = new KeyboardButton(lists.get(0).get(i) + " (" + User.getUserName(Long.parseLong(lists.get(1).get(i))) + ")");
        btns[lists.get(0).size()] = new KeyboardButton(Commands.backCommandRU);
        List<KeyboardRow> keyboard = new ArrayList<>();
        fillKeyBoard(keyboard, btns);
        lists_menu.setKeyboard(keyboard);
        return lists_menu;
    }
}
