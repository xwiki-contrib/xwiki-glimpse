package org.xwiki.contrib.glimpse.resources;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.hibernate.Query;
import org.hibernate.Session;
import org.xwiki.component.annotation.Component;
import org.xwiki.context.Execution;
import org.xwiki.contrib.glimpse.model.Agent;
import org.xwiki.contrib.glimpse.model.Service;
import org.xwiki.rest.XWikiRestComponent;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.store.XWikiHibernateStore;

@Component("org.xwiki.contrib.glimpse.AgentsResource")
@Path("/glimpse/agents")
public class AgentsResource implements XWikiRestComponent
{
    @Inject
    Execution execution;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get()
    {
        XWikiContext xwikiContext = (XWikiContext) execution.getContext().getProperty("xwikicontext");
        XWikiHibernateStore hibernateStore = xwikiContext.getWiki().getHibernateStore();

        try {
            hibernateStore.beginTransaction(xwikiContext);
            Session session = hibernateStore.getSession(xwikiContext);
            Query query = session.createQuery("select agent from Agent as agent");

            List results = query.list();

            JsonConfig config = new JsonConfig();
            config.setJsonPropertyFilter(new PropertyFilter()
            {
                public boolean apply(Object source, String name, Object value)
                {
                    /* Filter agent and id fields in Service class in order to avoid a cyclic object graph */
                    if (source.getClass().equals(Service.class) && ("agent".equals(name) || "id".equals(name))) {
                        return true;
                    }
                    return false;
                }
            });

            JSON json = JSONSerializer.toJSON(results, config);

            hibernateStore.endTransaction(xwikiContext, false);

            return Response.ok(json.toString()).build();
        } catch (XWikiException e) {
            hibernateStore.endTransaction(xwikiContext, false);
            return Response.serverError().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(String jsonData)
    {
        XWikiContext xwikiContext = (XWikiContext) execution.getContext().getProperty("xwikicontext");
        XWikiHibernateStore hibernateStore = xwikiContext.getWiki().getHibernateStore();

        try {
            Agent agent = readAgentFromJSON(jsonData);

            hibernateStore.beginTransaction(xwikiContext);
            Session session = hibernateStore.getSession(xwikiContext);
            session.saveOrUpdate(agent);
            hibernateStore.endTransaction(xwikiContext, true);

            return Response.ok().entity(agent.toString()).build();
        } catch (JSONException e) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (XWikiException e) {
            hibernateStore.endTransaction(xwikiContext, false);
            return Response.serverError().build();
        }
    }

    private Agent readAgentFromJSON(String jsonData)
    {
        JSONObject jsonAgentObject = JSONObject.fromObject(jsonData);
        Agent agent = new Agent();
        agent.setIp(jsonAgentObject.getString("ip"));
        agent.setName(jsonAgentObject.getString("name"));
        agent.setLastUpdateTime(System.currentTimeMillis());

        Set<Service> services = new HashSet<Service>();
        JSONArray jsonServices = jsonAgentObject.getJSONArray("services");
        Iterator iterator = jsonServices.iterator();
        while (iterator.hasNext()) {
            JSONObject jsonService = (JSONObject) iterator.next();
            Service service = new Service(agent, jsonService.getString("name"));
            service.setStatus(jsonService.getInt("status"));

            JSONObject jsonServiceData = jsonService.getJSONObject("data");
            if (jsonServiceData != null) {
                for (Object key : jsonServiceData.keySet()) {
                    service.getData().put((String) key, jsonServiceData.getString((String) key));
                }
            }

            services.add(service);
        }

        agent.setServices(services);

        return agent;
    }

}
