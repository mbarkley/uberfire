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
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.core.client.JavaScriptObject;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.client.mvp.Activity;
import org.uberfire.client.mvp.ActivityBeansCache;
import org.uberfire.client.mvp.SplashScreenActivity;
import org.uberfire.client.splash.JSNativeSplashScreen;
import org.uberfire.client.splash.JSSplashScreenActivity;
import org.uberfire.client.workbench.widgets.splash.SplashView;

import static org.jboss.errai.ioc.client.QualifierUtil.DEFAULT_QUALIFIERS;

@ApplicationScoped
public class SplashScreenJSExporter implements UberfireJSExporter {
    
    @Inject
    private SyncBeanManager beanManager;
    
    @Inject
    private ActivityBeansCache activityBeansCache;
    
    @Inject
    private ManagedInstance<JSNativeSplashScreen> splashScreen;
    
    @Inject
    private ManagedInstance<SplashView> splashView;

    public void registerSplashScreen(final Object _obj) {
        final JavaScriptObject obj = (JavaScriptObject) _obj;

        if (JSNativeSplashScreen.hasStringProperty(obj,
                                                   "id") && JSNativeSplashScreen.hasTemplate(obj)) {
            final JSNativeSplashScreen newNativePlugin = splashScreen.get();
            newNativePlugin.build(obj);

            final SplashView splashView = this.splashView.get();

            JSSplashScreenActivity activity = JSExporterUtils.findActivityIfExists(beanManager,
                                                                                   newNativePlugin.getId(),
                                                                                   JSSplashScreenActivity.class);

            if (activity == null) {
                registerNewActivity(beanManager,
                                    activityBeansCache,
                                    newNativePlugin,
                                    splashView);
            } else {
                updateExistentActivity(newNativePlugin,
                                       activity);
            }
        }
    }

    private static void updateExistentActivity(final JSNativeSplashScreen newNativePlugin,
                                               final JSSplashScreenActivity activity) {
        activity.setNativeSplashScreen(newNativePlugin);
    }

    private static void registerNewActivity(final SyncBeanManager beanManager,
                                            final ActivityBeansCache activityBeansCache,
                                            final JSNativeSplashScreen newNativePlugin,
                                            final SplashView splashView) {
        final JSSplashScreenActivity activity;
        activity = new JSSplashScreenActivity(newNativePlugin,
                                              splashView);
        final Set<Annotation> qualifiers = new HashSet<Annotation>(Arrays.asList(DEFAULT_QUALIFIERS));
        final SingletonBeanDef<JSSplashScreenActivity, JSSplashScreenActivity> beanDef =
                new SingletonBeanDef<JSSplashScreenActivity, JSSplashScreenActivity>(activity,
                                                                                     JSSplashScreenActivity.class,
                                                                                     qualifiers,
                                                                                     newNativePlugin.getId(),
                                                                                     true,
                                                                                     SplashScreenActivity.class,
                                                                                     Activity.class);
        beanManager.registerBean(beanDef);
        beanManager.registerBeanTypeAlias(beanDef,
                                          SplashScreenActivity.class);
        beanManager.registerBeanTypeAlias(beanDef,
                                          Activity.class);

        activityBeansCache.addNewSplashScreenActivity(beanManager.lookupBeans(newNativePlugin.getId()).iterator().next());
    }

    @Override
    public void export() {
        publish(this);
    }

    private native void publish(SplashScreenJSExporter exporter) /*-{
        $wnd.$registerSplashScreen = exporter.@org.uberfire.client.exporter.SplashScreenJSExporter::registerSplashScreen(Ljava/lang/Object;);
    }-*/;
}
