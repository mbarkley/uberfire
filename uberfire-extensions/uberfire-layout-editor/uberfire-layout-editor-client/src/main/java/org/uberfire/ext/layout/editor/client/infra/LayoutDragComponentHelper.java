/*
 * Copyright 2015 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.layout.editor.client.infra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ioc.client.container.Factory;
import org.uberfire.ext.layout.editor.api.editor.LayoutComponent;
import org.uberfire.ext.layout.editor.client.api.HasDragAndDropSettings;
import org.uberfire.ext.layout.editor.client.api.LayoutDragComponent;

/*
 * Must stay an entry point so that it is considered reachable (is looked up statically).
 */
@EntryPoint
public class LayoutDragComponentHelper {

    private DndDataJSONConverter converter = new DndDataJSONConverter();
    private List<Object> instances = new ArrayList<>();

    @Inject
    @Any
    private ManagedInstance<LayoutDragComponent> dragComponentProvider;

    public LayoutDragComponent lookupDragTypeBean(String dragTypeClassName) {
        return lookupBean(dragTypeClassName);
    }

    private LayoutDragComponent lookupBean(String dragTypeClassName) {
        for (LayoutDragComponent instance : dragComponentProvider) {
            instances.add(instance);
            if (getRealBeanClass(instance).equalsIgnoreCase(dragTypeClassName)) {
                return instance;
            }
        }
        return null;
    }

    public String getRealBeanClass(LayoutDragComponent instance) {
        return Factory.maybeUnwrapProxy(instance).getClass().getName();
    }

    public LayoutComponent getLayoutComponentFromDrop(String dropData) {
        LayoutDragComponent component = extractComponent(dropData);
        LayoutComponent layoutComponent = getLayoutComponent(component);
        return layoutComponent;
    }

    public LayoutComponent getLayoutComponent(LayoutDragComponent dragComponent) {

        LayoutComponent layoutComponent = new LayoutComponent(getRealBeanClass(dragComponent));

        if (dragComponent instanceof HasDragAndDropSettings) {
            Map<String, String> properties = ((HasDragAndDropSettings) dragComponent).getMapSettings();

            if (properties != null) {
                layoutComponent.addProperties(properties);
            }
        }

        return layoutComponent;
    }

    private LayoutDragComponent extractComponent(String dropData) {
        return converter
                .readJSONDragComponent(dropData);
    }
}
