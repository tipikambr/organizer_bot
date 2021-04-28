package organizer.bot.utils.menus;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.constants.Commands;
import organizer.bot.utils.constants.Language;

import java.util.ArrayList;
import java.util.List;

public class FirstMenu extends MenuPattern {
    private final static ReplyKeyboardMarkup firstMenu;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////

    private static final String FIRST_MESSAGE = "Вы хотите начать пользоваться ботом органайзером?\n" +
            "Если да, то выберете язык, который вам предпочтительней:";
    private final static String FIRST_NAME_USER_RU = "Введите ваш логин для бота";
    private final static String FIRST_PASSWORD_USER_RU = "Введите пароль для бота";
    private final static String RETRY_USER_RU = "Данный логин уже занят, попробуйте еще раз :с";
    private final static String YOU_ALREADY_HAVE_ACCOUNT_RU = "У вас уже есть аккаунт, вспомните его)";

    public static String getFirstNameMessage(int language){
        switch (language){
            case Language.RU:
                return FIRST_NAME_USER_RU;
            default:
                return FIRST_NAME_USER_RU;
        }
    }

    public static String getFirstPasswordMessage(int language){
        switch (language){
            case Language.RU:
                return FIRST_PASSWORD_USER_RU;
            default:
                return FIRST_PASSWORD_USER_RU;
        }
    }
    public static String getRetryMessage(int language){
        switch (language){
            case Language.RU:
                return RETRY_USER_RU;
            default:
                return RETRY_USER_RU;
        }
    }
    public static String getYouAlreadyHaveAccountMessage(int language){
        switch (language){
            case Language.RU:
                return YOU_ALREADY_HAVE_ACCOUNT_RU;
            default:
                return YOU_ALREADY_HAVE_ACCOUNT_RU;
        }
    }

    public static String getFirstMessage(){
        return FIRST_MESSAGE;
    }

    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        firstMenu = new ReplyKeyboardMarkup();
        firstMenu
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.russianLanguageCommand)};

        fillKeyBoard(keyboard, btns);

        firstMenu.setKeyboard(keyboard);
    }

    public static ReplyKeyboardMarkup getFirstMenu(){
        return firstMenu;
    }
}
