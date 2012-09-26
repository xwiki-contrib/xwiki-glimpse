package org.xwiki.contrib.glimpse;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.xwiki.contrib.glimpse.model.Agent;
import org.xwiki.contrib.glimpse.model.Service;

public class GlimpseUtils
{
    public static Agent readAgentFromJSON(String jsonData)
    {
        JSONObject jsonAgentObject = JSONObject.fromObject(jsonData);
        Agent agent = new Agent(jsonAgentObject.getString("name"), jsonAgentObject.getString("ip"));
        agent.setLastUpdateTime(System.currentTimeMillis());

        Set<Service> services = new HashSet<Service>();
        JSONArray jsonServices = jsonAgentObject.getJSONArray("services");
        Iterator iterator = jsonServices.iterator();
        while (iterator.hasNext()) {
            JSONObject jsonService = (JSONObject) iterator.next();
            Service service = new Service(agent, jsonService.getString("name"));
            service.setStatus(jsonService.getInt("status"));

            JSONObject jsonServiceProperties = jsonService.getJSONObject("properties");
            if (jsonServiceProperties != null) {
                for (Object key : jsonServiceProperties.keySet()) {
                    service.getProperties().put((String) key, jsonServiceProperties.getString((String) key));
                }
            }

            services.add(service);
        }

        agent.setServices(services);

        return agent;
    }

}
