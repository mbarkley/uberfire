/**
 * Copyright (C) 2014 JBoss Inc
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
package org.dashbuilder.displayer.client;

import org.dashbuilder.dataset.DataSetRef;
import org.dashbuilder.displayer.DisplayerSettings;

/**
 * Helper methods for dealing with Displayer instances.
 */
public class DataViewerHelper {

    /**
     * Get a Displayer instance for displaying the specified data set with the given display configuration.
     *
     * @param dataSetRef A reference to the data set.
     * @param displayerSettings The given display configuration.
     */
    public static Displayer lookup(DataSetRef dataSetRef, DisplayerSettings displayerSettings ) {
        return DataViewerLocator.get().lookupViewer(dataSetRef, displayerSettings );
    }

    /**
     * Issues a draw request for the given Displayer instances.
     */
    public static void draw(Displayer... displayers ) {
        DataViewerCoordinator coordinator = new DataViewerCoordinator();
        for (Displayer displayer : displayers ) {
            coordinator.addViewer( displayer );
        }
        coordinator.drawAll();
    }

    /**
     * Issues a redraw request for the given Displayer instances.
     */
    public static void redraw(Displayer... displayers ) {
        DataViewerCoordinator coordinator = new DataViewerCoordinator();
        for (Displayer displayer : displayers ) {
            coordinator.addViewer( displayer );
        }
        coordinator.redrawAll();
    }
}