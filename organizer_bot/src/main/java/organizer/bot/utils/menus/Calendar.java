package organizer.bot.utils.menus;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import organizer.bot.utils.constants.Language;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Calendar {
    private static String getMonthName(int monthNumber, int lang){
        return switch (lang){
            case Language.RU ->
                    switch (monthNumber) {
                        case 0 -> "Январь";
                        case 1 -> "Февраль";
                        case 2 -> "Март";
                        case 3 -> "Апрель";
                        case 4 -> "Май";
                        case 5 -> "Июнь";
                        case 6 -> "Июль";
                        case 7 -> "Август";
                        case 8 -> "Сентябрь";
                        case 9 -> "Октябрь";
                        case 10 -> "Ноябрь";
                        case 11 -> "Декабрь";
                        default -> "Упс";
                    };
            default -> "**Непонятный язык";
        };
    }

    private static String getMainName(Date date, int lang){
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);

        int month = cal.get(java.util.Calendar.MONTH);
        int year = cal.get(java.util.Calendar.YEAR);

        return getMonthName(month, lang) + " " + year;
    }

    private static ArrayList<InlineKeyboardButton> getDaysRU(){
        ArrayList days = new ArrayList<InlineKeyboardButton>(0);
        days.add(new InlineKeyboardButton("Пн").setCallbackData("Пн/"));
        days.add(new InlineKeyboardButton("Вт").setCallbackData("Вт/"));
        days.add(new InlineKeyboardButton("Ср").setCallbackData("Ср/"));
        days.add(new InlineKeyboardButton("Чт").setCallbackData("Чт/"));
        days.add(new InlineKeyboardButton("Пт").setCallbackData("Пт/"));
        days.add(new InlineKeyboardButton("Сб").setCallbackData("Сб/"));
        days.add(new InlineKeyboardButton("Вс").setCallbackData("Вс/"));
        return days;
    }


    private static ArrayList<InlineKeyboardButton> getDays(int lang){
        return switch (lang){
            case Language.RU ->
                    getDaysRU();
            default -> getDaysRU();
        };
    }

    private static boolean isGregor(int year){
        return (year % 4 == 0) && (year % 100 != 0) && (year % 400 == 0);
    }

    private static int daysInMonth(int month, int year){
        return switch (month){
            case 0 -> 31;
            case 1 -> 28 + (isGregor(year) ? 1 : 0);
            case 2 -> 31;
            case 3 -> 30;
            case 4 ->31;
            case 5 -> 30;
            case 6 -> 31;
            case 7 -> 31;
            case 8 -> 30;
            case 9 -> 31;
            case 10 -> 30;
            case 11 -> 31;
            default -> 30;
        };
    }

    private static List<List<InlineKeyboardButton> > getMonthDaysButton(Date date){
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        List<List<InlineKeyboardButton> > buttons = new ArrayList<>(0);
        int month = cal.get(java.util.Calendar.MONTH);
        int year = cal.get(java.util.Calendar.YEAR);
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        int t = (cal.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7;
        buttons.add(new ArrayList<>(0));
        for (int i = 0; i < t; i++)
            buttons.get(0).add(new InlineKeyboardButton(" "));
        do {
            buttons.get(buttons.size() - 1).add(new InlineKeyboardButton(String.valueOf(cal.get(java.util.Calendar.DAY_OF_MONTH))).setCallbackData(month + "/" + year + "/" + cal.get(java.util.Calendar.DAY_OF_MONTH) + "/"));
            if ((cal.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7 == 6)
                buttons.add(new ArrayList<>(0));
            cal.set(java.util.Calendar.DAY_OF_MONTH, cal.get(java.util.Calendar.DAY_OF_MONTH) + 1);
        } while ((cal.get(java.util.Calendar.DAY_OF_MONTH) != daysInMonth(month, year)));
        for (int i = (cal.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7; i < 7; i++)
            buttons.get(buttons.size() - 1).add(new InlineKeyboardButton(" "));
        return buttons;
    }

    private static List<InlineKeyboardButton> getControls(Date date){
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(java.util.Calendar.MONTH);
        int year = cal.get(java.util.Calendar.YEAR);

        List<InlineKeyboardButton> control = new ArrayList<>();
        control.add(new InlineKeyboardButton("<<").setCallbackData(month + "/" + year + "/<</"));
        control.add(new InlineKeyboardButton("<").setCallbackData(month + "/" + year + "/</"));
        control.add(new InlineKeyboardButton(">").setCallbackData(month + "/" + year + "/>/"));
        control.add(new InlineKeyboardButton(">>").setCallbackData(month + "/" + year + "/>>/"));
        return control;
    }

    protected static List<List<InlineKeyboardButton>> getCalendar(Date date, int lang){
        List<List<InlineKeyboardButton> > buttons = new ArrayList<>();

        ArrayList<InlineKeyboardButton> year_month = new ArrayList<>(0);
        year_month.add(new InlineKeyboardButton(getMainName(date, lang)));
        buttons.add(year_month);
        buttons.add(getDays(lang));
        buttons.addAll(getMonthDaysButton(date));
        buttons.add(getControls(date));
        buttons.add(new ArrayList<>(0));
        buttons.get(buttons.size()-1).add(new InlineKeyboardButton("Назад").setCallbackData("назад"));
        return buttons;
    }

}
