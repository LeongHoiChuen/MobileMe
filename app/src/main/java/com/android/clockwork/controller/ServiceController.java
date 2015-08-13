package com.android.clockwork.controller;

import com.android.clockwork.model.ServiceManager;

import org.apache.http.NameValuePair;

import java.util.List;

public class ServiceController {
    private ServiceManager serviceManager;

    public ServiceController() {
        setServiceManager(new ServiceManager());
    }

    public String callService(String URL, List<NameValuePair> nvps) {
        return serviceManager.POST(URL, nvps);
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
