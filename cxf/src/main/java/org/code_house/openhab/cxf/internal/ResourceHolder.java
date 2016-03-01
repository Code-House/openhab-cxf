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

import java.util.*;

/**
 * Bag for registered resources which should be used to construct CXF service.
 *
 * @author Łukasz Dywicki <luke@code-house.org>
 */
public class ResourceHolder {

    private List<Object> resources = new ArrayList<>();

    public void addResource(Object resource) {
        resources.add(resource);
    }

    public void removeResource(Object resource) {
        resources.remove(resource);
    }

    public List<Object> getResources() {
        return Collections.unmodifiableList(resources);
    }

}
