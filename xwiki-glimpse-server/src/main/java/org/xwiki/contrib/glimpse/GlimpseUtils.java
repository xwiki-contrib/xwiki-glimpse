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
