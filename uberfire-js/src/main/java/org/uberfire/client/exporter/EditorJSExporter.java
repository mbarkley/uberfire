/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.JavaScriptObject;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.client.editor.JSEditorActivity;
import org.uberfire.client.editor.JSNativeEditor;
import org.uberfire.client.mvp.Activity;
import org.uberfire.client.mvp.ActivityBeansCache;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.WorkbenchEditorActivity;

import static org.jboss.errai.ioc.client.QualifierUtil.DEFAULT_QUALIFIERS;

/**
 * <p>
 * Scope must be singleton so this is not proxied. Proxying breaks the JSNI method.
 */
@Singleton
public class EditorJSExporter implements UberfireJSExporter {

    @Inject
    private ActivityBeansCache activityBeansCache;

    @Inject
    private ManagedInstance<JSNativeEditor> nativeEditor;

    @Inject
    private PlaceManager placeManager;

    /*
     * Only use this for looking up dynamically added JS beans!
     * All beans implemneted in GWT should be looked up via ManagedInstance
     * so we can determine reachability at compile-time.
     */
    @Inject
    private SyncBeanManager beanManager;

    public void registerEditor(final Object _obj) {
        final JavaScriptObject obj = (JavaScriptObject) _obj;
        if (JSNativeEditor.hasStringProperty(obj,
                                             "id")) {
            final JSNativeEditor newNativeEditor = nativeEditor.get();
            newNativeEditor.build(obj);

            JSEditorActivity activity = JSExporterUtils.findActivityIfExists(beanManager,
                                                                             newNativeEditor.getId(),
                                                                             JSEditorActivity.class);

            if (activity == null) {
                registerNewActivity(beanManager,
                                    activityBeansCache,
                                    newNativeEditor,
                                    placeManager);
            } else {
                updateExistentActivity(newNativeEditor,
                                       activity);
            }
        }
    }

    private static void updateExistentActivity(final JSNativeEditor newNativeEditor,
                                               final JSEditorActivity activity) {
        activity.setNativeEditor(newNativeEditor);
    }

    private static void registerNewActivity(final SyncBeanManager beanManager,
                                            final ActivityBeansCache activityBeansCache,
                                            final JSNativeEditor newNativeEditor,
                                            final PlaceManager placeManager) {
        final JSEditorActivity activity;
        activity = new JSEditorActivity(newNativeEditor,
                                        placeManager);

        final Set<Annotation> qualifiers = new HashSet<>(Arrays.asList(DEFAULT_QUALIFIERS));
        final SingletonBeanDef<JSEditorActivity, JSEditorActivity> beanDef = new SingletonBeanDef<>(activity,
                                                                                                                                      JSEditorActivity.class,
                                                                                                                                      qualifiers,
                                                                                                                                      newNativeEditor.getId(),
                                                                                                                                      true,
                                                                                                                                      WorkbenchEditorActivity.class,
                                                                                                                                      Activity.class);
        beanManager.registerBean(beanDef);
        beanManager.registerBeanTypeAlias(beanDef,
                                          WorkbenchEditorActivity.class);
        beanManager.registerBeanTypeAlias(beanDef,
                                          Activity.class);

        activityBeansCache.addNewEditorActivity(beanManager.lookupBeans(newNativeEditor.getId()).iterator().next(),
                                                newNativeEditor.getPriority(),
                                                newNativeEditor.getResourceType());
    }

    @Override
    public void export() {
        publish(this);
    }

    private native void publish(EditorJSExporter exporter) /*-{
        $wnd.$registerEditor = function(obj) {
            exporter.@org.uberfire.client.exporter.EditorJSExporter::registerEditor(Ljava/lang/Object;)(obj);
        };
    }-*/;

    public static class EditorResourceTypeNotFound extends RuntimeException {

    }
}
