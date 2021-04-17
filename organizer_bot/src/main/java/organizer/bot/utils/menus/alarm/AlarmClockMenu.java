package organizer.bot.utils.menus.alarm;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import organizer.bot.utils.menus.Clock;

import java.util.Date;
import java.util.List;

public class AlarmClockMenu extends Clock {
    public static InlineKeyboardMarkup initHourForAlarm(Integer year, Integer month, Integer day, String dayS){
        List<List<InlineKeyboardButton>> btns_menu = getHourMenu();
        for(var btns: btns_menu)
            for (var btn:btns)
                btn.setCallbackData(((dayS == null) ? (month + "/" + year + "/" + day) : (dayS)) + "/" + btn.getCallbackData() + "alarm");
        InlineKeyboardMarkup res = new InlineKeyboardMarkup();
        res.setKeyboard(btns_menu);
        return res;
    }

    public static InlineKeyboardMarkup initMinuteForAlarm(Integer year, Integer month, Integer day, Integer hour, String dayS ){
        List<List<InlineKeyboardButton>> btns_menu = getMinuteMenu();
        for(var btns: btns_menu)
            for (var btn:btns)
                btn.setCallbackData(((dayS == null) ? (month + "/" + year + "/" + day) : (dayS)) + "/" + hour + "/" + btn.getCallbackData() + "alarm");
        InlineKeyboardMarkup res = new InlineKeyboardMarkup();
        res.setKeyboard(btns_menu);
        return res;
    }
}
