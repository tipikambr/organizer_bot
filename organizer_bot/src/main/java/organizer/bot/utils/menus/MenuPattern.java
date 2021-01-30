package organizer.bot.utils.menus;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public abstract class MenuPattern {
    protected static void fillKeyBoard(List<KeyboardRow> keyboard, KeyboardButton[] btns){
        for (int i = 0; i < btns.length; i++){
            KeyboardRow row = new KeyboardRow();
            row.add(btns[i]);
            keyboard.add(row);
        }
    }
}
