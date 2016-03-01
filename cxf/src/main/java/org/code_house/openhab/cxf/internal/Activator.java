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

import org.apache.karaf.util.tracker.BaseActivator;

/**
 * Bundle activator working with services.
 *
 * @author Łukasz Dywicki <luke@code-house.org>
 */
public class Activator extends BaseActivator {

    private RestResourceTracker restResourceTracker;
    private Thread thread;

    @Override
    protected void doOpen() throws Exception {
        EndpointPublisher endpointPublisher = new EndpointPublisher(bundleContext);

        ResourceHolder resourceHolder = new ResourceHolder();

        RefreshRunnable refreshRunnable = new RefreshRunnable(endpointPublisher, resourceHolder, 600L);
        thread = new Thread(refreshRunnable, "openhab-cxf-context-refresh");
        thread.setDaemon(true);

        restResourceTracker = new RestResourceTracker(bundleContext, resourceHolder);
        restResourceTracker.open(true);
    }

    @Override
    protected void doStart() throws Exception {
        thread.start();
    }

    @Override
    protected void doStop() {
        thread.interrupt();
    }

    @Override
    protected void doClose() {
        restResourceTracker.close();
    }

}
