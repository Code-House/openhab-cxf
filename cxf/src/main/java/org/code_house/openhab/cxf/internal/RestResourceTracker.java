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
import org.osgi.util.tracker.ServiceTracker;

public class RestResourceTracker extends ServiceTracker<RESTResource, RESTResource> {
    public RestResourceTracker(BundleContext context, ResourceHolder holder) {
        super(context, RESTResource.class, new RestResourceTrackerCustomizer(context, holder));
    }


}
