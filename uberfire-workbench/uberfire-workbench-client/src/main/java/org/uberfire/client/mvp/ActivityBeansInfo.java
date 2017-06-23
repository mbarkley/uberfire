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
package org.uberfire.client.mvp;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.ioc.client.api.BeanDefProvider;
import org.jboss.errai.ioc.client.container.IOCBeanDef;

@ApplicationScoped
public class ActivityBeansInfo {

    @Inject @Any
    private BeanDefProvider<WorkbenchScreenActivity> screenProvider;

    @Inject @Any
    private BeanDefProvider<PerspectiveActivity> perspectiveProvider;

    @Inject @Any
    private BeanDefProvider<SplashScreenActivity> splashScreenProvider;

    @Inject @Any
    private BeanDefProvider<WorkbenchEditorActivity> editorProvider;

    private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
        @Override
        public int compare(String str1,
                           String str2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1,
                                                            str2);
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        }
    };

    public List<String> getAvailableWorkbenchScreensIds() {
        return lookupBeansId(screenProvider);
    }

    public List<String> getAvailablePerspectivesIds() {
        return lookupBeansId(perspectiveProvider);
    }

    public List<String> getAvailableSplashScreensIds() {
        return lookupBeansId(splashScreenProvider);
    }

    public List<String> getAvailableWorkbenchEditorsIds() {
        return lookupBeansId(editorProvider);
    }

    public void addActivityBean(List<String> activityBeans,
                                String newBean) {
        activityBeans.add(newBean);
        Collections.sort(activityBeans,
                         ALPHABETICAL_ORDER);
    }

    private List<String> lookupBeansId(Iterable<? extends IOCBeanDef<?>> activities) {
        List<String> result = new ArrayList<>();
        for (final IOCBeanDef<?> beanDef : activities) {
            result.add(getId(beanDef));
        }
        Collections.sort(result,
                         ALPHABETICAL_ORDER);
        return result;
    }

    public String getId(final IOCBeanDef<?> beanDef) {
        for (final Annotation annotation : beanDef.getQualifiers()) {
            if (isNamed(annotation)) {
                return ((Named) annotation).value();
            }
        }
        if (hasBeanName(beanDef)) {
            return beanDef.getName();
        }
        return "";
    }

    boolean isNamed(Annotation annotation) {
        return annotation instanceof Named;
    }

    private boolean hasBeanName(IOCBeanDef<?> beanDef) {
        return beanDef.getName() != null && !beanDef.getName().isEmpty();
    }
}
