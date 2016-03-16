/*
 * (C) Copyright 2016 Code-House and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.code_house.openhab.cxf.internal;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.bus.osgi.OSGIBusListener;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.security.JAASAuthenticationFilter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * Element responsible for publishing cxf endpoint with rest resources.
 *
 * @author Łukasz Dywicki <luke@code-house.org>
 */
public class EndpointPublisher {

    private Logger logger = LoggerFactory.getLogger(EndpointPublisher.class);

    private final BundleContext context;

    private ExtensionManagerBus bus;
    private ServiceRegistration serviceRegistration;

    public EndpointPublisher(BundleContext context) {
        this.context = context;
    }

    private void stop() {
        if (bus != null) {
            if (bus.getState() != Bus.BusState.SHUTDOWN) {
                bus.shutdown(true);
            }
            bus = null;
        }
    }

    private void start(List<Object> resources) {
        if (bus != null) {
            logger.error("Unable to start new cxf endpoint cause previous one still exists: {}", bus);
            return;
        }
        ExtensionManagerBus bus = new ExtensionManagerBus();
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setBus(bus);
        factoryBean.setAddress("/openhab");
        factoryBean.setServiceBeans(resources);
        JAASAuthenticationFilter filter = new JAASAuthenticationFilter();
        filter.setContextName("karaf");

        factoryBean.setProviders(Arrays.asList(new JacksonJsonProvider(), filter));
        factoryBean.setFeatures(Arrays.asList(new LoggingFeature()));

        factoryBean.create();

        bus.initialize();

        Hashtable<String, Object> properties = new Hashtable<>();
        properties.put(OSGIBusListener.CONTEXT_SYMBOLIC_NAME_PROPERTY, "org.code-house.openhab.cxf");
        properties.put(OSGIBusListener.CONTEXT_VERSION_PROPERTY, "0.0.0");
        properties.put(OSGIBusListener.CONTEXT_NAME_PROPERTY, "openhab");

        this.bus = bus;
        serviceRegistration = context.registerService(Bus.class, bus, properties);
    }

    public void publish(List<Object> resources) {
        start(resources);
    }

    public void unpublish() {
        serviceRegistration.unregister();
        stop();
    }
}
