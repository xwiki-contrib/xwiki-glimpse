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

import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.contrib.glimpse.Glimpse;
import org.xwiki.contrib.glimpse.GlimpseException;
import org.xwiki.contrib.glimpse.GlimpseUtils;
import org.xwiki.contrib.glimpse.Utils;
import org.xwiki.contrib.glimpse.model.Agent;
import org.xwiki.contrib.glimpse.model.Service;
import org.xwiki.rest.XWikiRestComponent;

import com.xpn.xwiki.XWikiContext;

@Component("org.xwiki.contrib.glimpse.resources.AgentsResource")
@Path("/glimpse/agents")
public class AgentsResource implements XWikiRestComponent
{
    /**
     * <p>
     * The XWiki component manager that is used to lookup XWiki components and context.
     * </p>
     */
    @Inject
    protected ComponentManager componentManager;

    @Inject
    private Glimpse glimpse;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get()
    {
        List<Agent> agents = glimpse.getAgents();

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

        JSON json = JSONSerializer.toJSON(agents, config);

        return Response.ok(json.toString()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(String jsonData)
    {
        try {
            Agent agent = GlimpseUtils.readAgentFromJSON(jsonData);

            glimpse.storeAgent(agent);

            return Response.ok().entity(agent.toString()).build();
        } catch (JSONException e) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (GlimpseException e) {
            return Response.serverError().build();
        }
    }

}
