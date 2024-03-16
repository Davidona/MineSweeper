package mines;

import javafx.event.Event;

public interface EventHandler<T extends Event> {
    void handle(T event);
}
