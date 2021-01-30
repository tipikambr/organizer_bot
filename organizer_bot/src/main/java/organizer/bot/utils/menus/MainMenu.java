package organizer.bot.utils.menus;

import com.google.inject.internal.asm.$Label;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.Commands;
import organizer.bot.utils.Language;
import organizer.data.base.connection.utils.User;

import java.util.ArrayList;
import java.util.List;


public class MainMenu extends MenuPattern {
    private final static ReplyKeyboardMarkup mainMenuRU;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////

    private static final String NEW_USER_RU = "Добро пожаловать, ";
    private static final String OLD_USER_RU = "Добро пожаловать в органайзер на русском языке! ^_^";
    private static final String NONE_USER_RU = "Извините, бот в данный момент не работает(";

    private static final String BACK_TO_MAIN_MENU_MESSAGE_RU = "И снова здравствуте в главном меню";


    private static final String INFO_MESSAGE_RU = "Это информационное сообщение. Его пока нет, но оно будет :)\n" +
            "Когда-нибудь...";

    public static String getNewUser(int language, long chat_id, String name){
        switch (language){
            case Language.RU:
                return NEW_USER_RU + name + "!";
            default:
                return NEW_USER_RU + name + "!";
        }
    }

    public static String getOldUser(int language){
        switch (language){
            case Language.RU:
                return OLD_USER_RU;
            default:
                return OLD_USER_RU;
        }
    }

    public static String getNoneUser(int language){
        switch (language){
            case Language.RU:
                return NONE_USER_RU;
            default:
                return NONE_USER_RU;
        }
    }

    public static String getBackToMainMenuMessage(int language){
        switch (language){
            case Language.RU:
                return BACK_TO_MAIN_MENU_MESSAGE_RU;
            default:
                return BACK_TO_MAIN_MENU_MESSAGE_RU;
        }
    }



    public static String getInfoMessage(int language){
        switch (language){
            case Language.RU:
                return INFO_MESSAGE_RU;
            default:
                return INFO_MESSAGE_RU;
        }
    }

    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        mainMenuRU = new ReplyKeyboardMarkup();
        mainMenuRU
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.infoCommandRU),
                new KeyboardButton(Commands.mailSettingCommandRU),
                new KeyboardButton(Commands.listSettingCommandRU),
                new KeyboardButton(Commands.soundCommandRU),
                new KeyboardButton(Commands.languageCommandRU)};

        fillKeyBoard(keyboard, btns);

        mainMenuRU.setKeyboard(keyboard);
    }

    public static String getMessageForStatusUser(long chat_id, int status, int language, String name){
        return switch (status){
            case 1 -> getNewUser(language, chat_id, name );
            case 0 -> getOldUser(language);
            default -> getNoneUser(language);
        };
    }

    public static ReplyKeyboardMarkup getMainMenu(int language){
        switch (language){
            case Language.RU:
                return mainMenuRU;
            default:
                return mainMenuRU;
        }
    }

}
