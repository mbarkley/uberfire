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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.mvp.impl.DefaultPlaceRequest;

@ApplicationScoped
public class PlaceManagerJSExporter implements UberfireJSExporter {

    @Inject
    private PlaceManager placeManager;

    public void goTo(final String place) {
        placeManager.goTo(new DefaultPlaceRequest(place));
    }

    @Override
    public void export() {
        publish(this);
    }

    private native void publish(PlaceManagerJSExporter exporter) /*-{
        $wnd.$goToPlace = exporter.@org.uberfire.client.exporter.PlaceManagerJSExporter::goTo(Ljava/lang/String;);
    }-*/;
}
