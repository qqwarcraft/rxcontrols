package rx.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class RXActionEvent extends Event {

    private static final long serialVersionUID = -2980852708549565117L;

    public static final EventType<RXActionEvent> RXACTION =
            new EventType<RXActionEvent>(Event.ANY, "RXACTION");

    public static final EventType<RXActionEvent> ANY = RXACTION;


    public RXActionEvent() {
        super(RXACTION);
    }


    public RXActionEvent(Object source, EventTarget target) {
        super(source, target, RXACTION);
    }

    @Override
    public RXActionEvent copyFor(Object newSource, EventTarget newTarget) {
        return (RXActionEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends RXActionEvent> getEventType() {
        return (EventType<? extends RXActionEvent>) super.getEventType();
    }
}
