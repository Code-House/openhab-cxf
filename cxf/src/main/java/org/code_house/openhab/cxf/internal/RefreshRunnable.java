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

import org.apache.cxf.Bus;
import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Timer task to refresh cxf endpoint when new resource list is changed.
 *
 * @author Łukasz Dywicki <luke@code-house.org>
 */
public class RefreshRunnable implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(RefreshRunnable.class);

    private final EndpointPublisher publisher;
    private final ResourceHolder holder;
    private final long refreshPeriod;

    private List<Object> previousResources;

    public RefreshRunnable(EndpointPublisher publisher, ResourceHolder holder, long refreshPeriod) {
        this.publisher = publisher;
        this.holder = holder;
        this.refreshPeriod = refreshPeriod;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                List<Object> currentResources = holder.getResources();
                if (previousResources == null) {
                    publisher.publish(currentResources);
                    previousResources = currentResources;
                } else if (!previousResources.equals(currentResources)) {
                    publisher.unpublish();
                    publisher.publish(currentResources);
                    previousResources = currentResources;
                }

                Thread.sleep(refreshPeriod);
            }
        } catch (InterruptedException e) {
            logger.warn("Refresh thread was interrupted");
        }
    }

}
