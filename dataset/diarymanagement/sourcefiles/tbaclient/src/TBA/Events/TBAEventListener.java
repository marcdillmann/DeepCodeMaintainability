package TBA.Events;

import java.util.EventListener;

/**
 * This is the interface for the custom {@link TBAEvent} event.
 *<p>
 * @author Dan McGrath
 *<p>
 * @see TBAEvent
 */
public interface TBAEventListener extends EventListener
{
    /**
     *
     * @param evt
     */
   public void TBAEventOccurred(TBAEvent evt);
}
