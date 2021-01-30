package organizer.bot.utils.menus;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import organizer.bot.utils.Commands;
import organizer.bot.utils.Language;

import java.util.ArrayList;
import java.util.List;

public class SoundMenu extends  MenuPattern {
    private final static ReplyKeyboardMarkup soundMenuRU;

    ///////////////////////////////////////////////////////////////
    //                         MESSAGES                          //
    ///////////////////////////////////////////////////////////////

    private final static String SOUND_INTRO_MESSAGE_RU = "Вы зашли в раздел настроек уведомлений. Что вы хотели сделать?";
    private final static String SOUND_TURN_ON_RU = "На данный момент управление звуком невозможно, потерпите немного";
    private final static String SOUND_TURN_OFF_RU = "На данный момент управление звуком невозможно, потерпите немного";
    private final static String SOUND_NOT_CHANGE_RU = "Не изменять, так не изменять ;)";

    public static String getSoundIntroMessage(int language){
        switch (language){
            case Language.RU:
                return SOUND_INTRO_MESSAGE_RU;
            default:
                return SOUND_INTRO_MESSAGE_RU;
        }
    }

    public static String getSoundTurnOnMessage(int language){
        switch (language){
            case Language.RU:
                return SOUND_TURN_ON_RU;
            default:
                return SOUND_TURN_ON_RU;
        }
    }

    public static String getSoundTurnOffMessage(int language){
        switch (language){
            case Language.RU:
                return SOUND_TURN_OFF_RU;
            default:
                return SOUND_TURN_OFF_RU;
        }
    }

    public static String getSoundNotChangeMessage(int language){
        switch (language){
            case Language.RU:
                return SOUND_NOT_CHANGE_RU;
            default:
                return SOUND_NOT_CHANGE_RU;
        }
    }

    ///////////////////////////////////////////////////////////////
    //                         KEYBOARD                          //
    ///////////////////////////////////////////////////////////////

    static {
        soundMenuRU = new ReplyKeyboardMarkup();
        soundMenuRU
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardButton[] btns = {
                new KeyboardButton(Commands.soundOnCommandRU),
                new KeyboardButton(Commands.soundOffCommandRU),
                new KeyboardButton(Commands.soundNotChangeCommandRU)};

        fillKeyBoard(keyboard, btns);

        soundMenuRU.setKeyboard(keyboard);
    }

    public static ReplyKeyboardMarkup getSoundMenu(int language){
        switch (language){
            case Language.RU:
                return soundMenuRU;
            default:
                return soundMenuRU;
        }
    }

    /**
     * Возвращает номер меню настроек уведомлений, в который можно попасть из текущего меню
     * @param fromMenu номер меню, из которого происходит вход
     * @return номер звукогого меню
     */
    public static int getNumberMenu(int fromMenu){
        return fromMenu * 10 + 9;
    }

}
