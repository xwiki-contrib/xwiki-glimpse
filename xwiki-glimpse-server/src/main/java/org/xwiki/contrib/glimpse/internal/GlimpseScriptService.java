package org.xwiki.contrib.glimpse.internal;

import java.util.List;

import javax.inject.Inject;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.glimpse.Glimpse;
import org.xwiki.contrib.glimpse.model.Agent;
import org.xwiki.script.service.ScriptService;

@Component("glimpse")
public class GlimpseScriptService implements ScriptService
{
    @Inject
    private Glimpse glimpse;

    public List<Agent> getAgents()
    {
        return glimpse.getAgents();
    }
}
