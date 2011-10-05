package org.xwiki.contrib.glimpse.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.codehaus.groovy.util.DoubleKeyHashMap.Entry;
import org.hibernate.Query;
import org.hibernate.Session;
import org.xwiki.component.annotation.Component;
import org.xwiki.context.Execution;
import org.xwiki.contrib.glimpse.Glimpse;
import org.xwiki.contrib.glimpse.GlimpseException;
import org.xwiki.contrib.glimpse.model.Agent;
import org.xwiki.contrib.glimpse.model.Service;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.store.XWikiHibernateStore;

@Component
public class GlimpseImpl implements Glimpse
{
    @Inject
    private Execution execution;

    public boolean storeAgent(Agent agent) throws GlimpseException
    {
        XWikiContext xwikiContext = (XWikiContext) execution.getContext().getProperty("xwikicontext");
        XWikiHibernateStore hibernateStore = xwikiContext.getWiki().getHibernateStore();

        try {
            hibernateStore.beginTransaction(xwikiContext);
            Session session = hibernateStore.getSession(xwikiContext);
            session.saveOrUpdate(agent);
            hibernateStore.endTransaction(xwikiContext, true);
        } catch (Exception e) {
            throw new GlimpseException(e);
        } finally {
            hibernateStore.endTransaction(xwikiContext, false);
        }

        return true;
    }

    public List<Agent> getAgents()
    {
        List<Agent> agents = new ArrayList<Agent>();

        XWikiContext xwikiContext = (XWikiContext) execution.getContext().getProperty("xwikicontext");
        XWikiHibernateStore hibernateStore = xwikiContext.getWiki().getHibernateStore();

        try {
            hibernateStore.beginTransaction(xwikiContext);
            Session session = hibernateStore.getSession(xwikiContext);
            Query query = session.createQuery("select agent from Agent as agent");

            List results = query.list();

            /*
             * TODO: I don't know if this can be avoided, but directly returning objects from the result list will cause
             * exceptions later about lazy retrieval. So create a POJO instance for each result, and return those copies
             */
            for (Object result : results) {
                agents.add(copyAgent((Agent) result));
            }

            hibernateStore.endTransaction(xwikiContext, false);
        } catch (XWikiException e) {
            hibernateStore.endTransaction(xwikiContext, false);
        }

        return agents;
    }

    private Agent copyAgent(Agent agent)
    {
        Agent copy = new Agent();
        copy.setIp(agent.getIp());
        copy.setName(agent.getName());
        copy.setLastUpdateTime(agent.getLastUpdateTime());

        for (Service currentService : agent.getServices()) {
            Service service = new Service(copy, currentService.getName());
            service.setId(currentService.getId());
            service.setStatus(currentService.getStatus());

            Map<String, String> data = currentService.getData();
            for (String key : data.keySet()) {
                service.getData().put(key, data.get(key));
            }

            copy.getServices().add(service);
        }

        return copy;
    }

}
