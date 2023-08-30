package TBA.Events;

import java.util.EventObject;

/**
 * This is custom TBA event. It is used to flag that the session state has
 * possibly changed.
 *<p>
 * @author Dan McGrath
 *<p>
 * @see TBAEventListener
 */
public class TBAEvent extends EventObject
{
  /**
   * TBAEvent constructor. It does nothing but call the super for
   * {@link java.util.EventObject}
   *<p>
   * @param source An Object passed to {@link java.util.EventObject}
   *<p>
   * @see java.util.EventObject
   */
  public TBAEvent(Object source)
  {
      super(source);
  }
}
