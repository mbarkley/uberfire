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

package org.uberfire.client.workbench;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.ManagedInstance;

/**
 * Used to discover alternative {@link org.uberfire.client.workbench.WorkbenchLayout}'s.
 * If no alternatives can be found, the default {@link org.uberfire.client.workbench.WorkbenchLayoutImpl} is used.
 * If several implementations are found the first one will be chosen.
 */
@ApplicationScoped
public class LayoutSelection {

    @Inject
    private ManagedInstance<WorkbenchLayout> defaultLayoutProvider;

    @Inject @AlternativeLayout
    private ManagedInstance<WorkbenchLayout> alternativeLayoutProvider;

    public WorkbenchLayout get() {
        //FIXME: this alternatives process doesn't work
        WorkbenchLayout layout = null;

        if (!alternativeLayoutProvider.isUnsatisfied()) {
            layout = alternativeLayoutProvider.iterator().next();
        } else {
            layout = defaultLayoutProvider.get();
        }
        return layout;
    }
}
