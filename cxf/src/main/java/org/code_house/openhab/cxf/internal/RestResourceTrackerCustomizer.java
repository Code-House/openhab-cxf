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

import org.eclipse.smarthome.io.rest.RESTResource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class RestResourceTrackerCustomizer implements ServiceTrackerCustomizer<RESTResource, RESTResource> {

    private final BundleContext context;
    private final ResourceHolder holder;

    public RestResourceTrackerCustomizer(BundleContext context, ResourceHolder holder) {
        this.context = context;
        this.holder = holder;
    }

    @Override
    public RESTResource addingService(ServiceReference<RESTResource> reference) {
        RESTResource resource = context.getService(reference);
        holder.addResource(resource);
        return resource;
    }

    @Override
    public void modifiedService(ServiceReference<RESTResource> reference,  RESTResource resource) {

    }

    @Override
    public void removedService(ServiceReference<RESTResource> reference,  RESTResource resource) {
        holder.removeResource(resource);
    }

}
