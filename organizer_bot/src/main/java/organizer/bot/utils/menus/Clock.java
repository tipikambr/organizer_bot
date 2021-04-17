package organizer.bot.utils.menus;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Clock {
    protected static List<List<InlineKeyboardButton>> getHourMenu(){
        List<List<InlineKeyboardButton> > buttons = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            List<InlineKeyboardButton> tmp = new ArrayList<>(0);
            for (int j = 0; j < 4; j++){
                tmp.add(new InlineKeyboardButton(String.valueOf(i + j*6)).setCallbackData((i + j * 6) + "/"));
            }
            buttons.add(tmp);
        }
        buttons.add(new ArrayList<>(0));
        buttons.get(buttons.size()-1).add(new InlineKeyboardButton("Назад").setCallbackData("назад"));
        return buttons;
    }

    protected static List<List<InlineKeyboardButton>> getMinuteMenu(){
        List<List<InlineKeyboardButton> > buttons = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            List<InlineKeyboardButton> tmp = new ArrayList<>(0);
            for (int j = 0; j < 4; j++){
                tmp.add(new InlineKeyboardButton(String.valueOf(i*5 + j*15)).setCallbackData((i*5 + j*15) + "/"));
            }
            buttons.add(tmp);
        }
        buttons.add(new ArrayList<>(0));
        buttons.get(buttons.size()-1).add(new InlineKeyboardButton("Назад").setCallbackData("назад"));
        return buttons;
    }
}
