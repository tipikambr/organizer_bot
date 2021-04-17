package organizer.bot.utils.menus.alarm;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.constants.Commands;
import organizer.bot.utils.constants.Language;
import organizer.bot.utils.menus.MenuPattern;

import java.util.ArrayList;
import java.util.List;

public class AlarmMenu extends MenuPattern {
    private final static ReplyKeyboardMarkup alarmMenuRU;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////

    private final static String ALARM_MENU_INTRO_MESSAGE_RU = "Вы зашли в раздел будильников. Что вы хотели сделать?";

    public static String getAlarmMenuIntroMessage(int language){
        switch (language){
            case Language.RU:
                return ALARM_MENU_INTRO_MESSAGE_RU;
            default:
                return ALARM_MENU_INTRO_MESSAGE_RU;
        }
    }

    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        alarmMenuRU = new ReplyKeyboardMarkup();
        alarmMenuRU
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.addAlarmRU),
                new KeyboardButton(Commands.lookAlarmRU),
                new KeyboardButton(Commands.backCommandRU)};

        fillKeyBoard(keyboard, btns);

        alarmMenuRU.setKeyboard(keyboard);
    }
    public static ReplyKeyboardMarkup getAlarmMenu(int language){
        switch (language){
            case Language.RU:
                return alarmMenuRU;
            default:
                return alarmMenuRU;
        }
    }
}
