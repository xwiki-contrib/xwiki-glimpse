package org.xwiki.contrib.glimpse.model;

import java.util.HashSet;
import java.util.Set;

public class Agent
{
    private String ip;

    private String name;

    private long lastUpdateTime;

    private Set<Service> services = new HashSet<Service>();

    public String getIp()
    {
        return ip;
    }

    public long getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public String getName()
    {
        return name;
    }

    public Set<Service> getServices()
    {
        return services;
    }

    public Service getService(String name)
    {
        for (Service service : services) {
            if (service.getName().equals(name)) {
                return service;
            }
        }

        return null;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public void setLastUpdateTime(long lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setServices(Set<Service> services)
    {
        this.services = services;
    }

    @Override
    public String toString()
    {
        return String.format("Agent [ip=%s, name=%s, lastUpdateTime=%s, services=%s]", ip, name, lastUpdateTime,
            services);
    }

}
