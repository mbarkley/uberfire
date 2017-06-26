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

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.uberfire.workbench.events.UberfireJSAPIReadyEvent;

@EntryPoint
public class UberfireJSAPIExporter {

    @Inject
    Event<UberfireJSAPIReadyEvent> jsapiReadyEvent;
    @Inject
    @Any
    ManagedInstance<UberfireJSExporter> jsExporterProvider;

    @PostConstruct
    public void export() {
        for (UberfireJSExporter jsAPI : jsExporterProvider) {
            jsAPI.export();
        }
        if (!jsExporterProvider.isUnsatisfied()) {
            jsapiReadyEvent.fire(new UberfireJSAPIReadyEvent());
        }
    }
}
