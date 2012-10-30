package org.xwiki.contrib.glimpse;

import org.xwiki.component.manager.ComponentManager;
import org.xwiki.context.Execution;

import com.xpn.xwiki.XWikiContext;

public class Utils
{
    /**
     * <p>
     * Retrieve the XWiki context from the current execution context
     * </p>
     * 
     * @param componentManager The component manager to be used to retrieve the execution context.
     * @return The XWiki context.
     * @throws RuntimeException If there was an error retrieving the context.
     */
    public static XWikiContext getXWikiContext(ComponentManager componentManager)
    {
        Execution execution;
        XWikiContext xwikiContext;
        try {
            execution = componentManager.getInstance(Execution.class);
            xwikiContext = (XWikiContext) execution.getContext().getProperty("xwikicontext");
            return xwikiContext;
        } catch (Exception e) {
            throw new RuntimeException("Unable to get XWiki context", e);
        }
    }
}
