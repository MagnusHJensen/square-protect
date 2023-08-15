package dk.magnusjensen.squareprotect.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatUtils {
    public static MutableComponent getTimeSince(long resultTime) {
        double timeSince = (System.currentTimeMillis() - (resultTime + 0.00)) / 1000L; // Divide by 1000 to get seconds
        if (timeSince < 0.00) {
            timeSince = 0.00;
        }

        Date logDate = new Date(resultTime);
        String formattedTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(logDate);
        MutableComponent mainComp = Component.empty().withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(formattedTimestamp))));

        // minutes
        timeSince = timeSince / 60;
        if (timeSince < 60.0) {
            return mainComp.append(Component.literal(new DecimalFormat("0.00").format(timeSince) + "/m")).append(" ago");
        }

        // hours
        timeSince = timeSince / 60;
        if (timeSince < 24.0) {
            return mainComp.append(Component.literal(new DecimalFormat("0.00").format(timeSince) + "/h")).append(" ago");
        }


        // days

        timeSince = timeSince / 24;
        return mainComp.append(Component.literal(new DecimalFormat("0.00").format(timeSince) + "/d")).append(" ago");
    }
}
