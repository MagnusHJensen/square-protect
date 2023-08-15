package dk.magnusjensen.squareprotect.utils;

import net.minecraft.network.chat.Component;

import java.util.Objects;

public final class ChatComponent implements Comparable<ChatComponent> {
    private final Component chatComponent;
    private final long timestamp;

    public ChatComponent(Component chatComponent, long timestamp) {
        this.chatComponent = chatComponent;
        this.timestamp = timestamp;
    }


    @Override
    public int compareTo(ChatComponent o) {
        return Long.compare(timestamp, o.timestamp);
    }

    public Component chatComponent() {
        return chatComponent;
    }

    public long timestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChatComponent) obj;
        return Objects.equals(this.chatComponent, that.chatComponent) &&
            this.timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatComponent, timestamp);
    }

    @Override
    public String toString() {
        return "ChatComponent[" +
            "chatComponent=" + chatComponent + ", " +
            "timestamp=" + timestamp + ']';
    }

}
