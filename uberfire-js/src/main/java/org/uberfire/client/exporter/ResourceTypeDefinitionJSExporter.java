/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.uberfire.client.exporter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.JavaScriptObject;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.client.editor.type.JSClientResourceType;
import org.uberfire.client.editor.type.JSNativeClientResourceType;
import org.uberfire.client.plugin.JSNativePlugin;
import org.uberfire.client.workbench.type.ClientResourceType;

import static org.jboss.errai.ioc.client.QualifierUtil.DEFAULT_QUALIFIERS;

/**
 * <p>
 * Scope must be singleton so this is not proxied. Proxying breaks the JSNI method.
 */
@Singleton
public class ResourceTypeDefinitionJSExporter implements UberfireJSExporter {
    
    @Inject
    private ManagedInstance<JSNativeClientResourceType> clientResourceType;
    
    @Inject
    private SyncBeanManager beanManager;

    public void registerResourceTypeDefinition(final Object _obj) {
        final JavaScriptObject obj = (JavaScriptObject) _obj;

        if (JSNativePlugin.hasStringProperty(obj,
                                             "id")) {
            final JSNativeClientResourceType newNativeClientResourceType = clientResourceType.get();
            newNativeClientResourceType.build(obj);
            JSClientResourceType jsClientResourceType = new JSClientResourceType(newNativeClientResourceType);
            beanManager.registerBean(new SingletonBeanDef<ClientResourceType, JSClientResourceType>(jsClientResourceType,
                                                                                                    ClientResourceType.class,
                                                                                                    new HashSet<Annotation>(Arrays.asList(DEFAULT_QUALIFIERS)),
                                                                                                    jsClientResourceType.getId(),
                                                                                                    true,
                                                                                                    JSClientResourceType.class));
        }
    }

    @Override
    public void export() {
        publish(this);
    }

    private native void publish(ResourceTypeDefinitionJSExporter exporter) /*-{
        $wnd.$registerResourceType = function(obj) {
            exporter.@org.uberfire.client.exporter.ResourceTypeDefinitionJSExporter::registerResourceTypeDefinition(Ljava/lang/Object;)(obj);
        };
    }-*/;
}
