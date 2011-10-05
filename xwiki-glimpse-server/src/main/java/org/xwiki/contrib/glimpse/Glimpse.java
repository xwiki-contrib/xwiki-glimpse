package org.xwiki.contrib.glimpse;

import java.util.List;

import org.xwiki.component.annotation.ComponentRole;
import org.xwiki.contrib.glimpse.model.Agent;

@ComponentRole
public interface Glimpse
{
    boolean storeAgent(Agent agent) throws GlimpseException;
    
    List<Agent> getAgents();
}
