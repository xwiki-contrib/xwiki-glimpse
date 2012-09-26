package org.xwiki.contrib.glimpse.model;

import java.util.HashMap;
import java.util.Map;

public class Service
{
    private String id;

    private Agent agent;

    private String name;

    private int status;

    private Map<String, String> properties = new HashMap<String, String>();

    private Service()
    {
    }

    public Service(Agent agent, String name)
    {
        this.agent = agent;
        this.name = name;

        id = String.format("%s:%s:%s", agent.getName(), agent.getIp(), name);
    }

    public Agent getAgent()
    {
        return agent;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public String getProperty(String key)
    {
        return properties.get(key);
    }

    public String getId()
    {
        if (id == null) {
            id = String.format("%s:%s:%s", agent.getName(), agent.getIp(), name);
        }

        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getStatus()
    {
        return status;
    }

    public void setAgent(Agent agent)
    {
        this.agent = agent;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return String.format("Service [id=%s, agent=%s, name=%s, status=%s, data=%s]", id, agent.getIp(), name, status,
            properties);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Service other = (Service) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
