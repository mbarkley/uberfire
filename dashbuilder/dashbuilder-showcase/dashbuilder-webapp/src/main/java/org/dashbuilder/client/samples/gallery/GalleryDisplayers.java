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
package org.dashbuilder.client.samples.gallery;

import org.dashbuilder.model.displayer.DataDisplayer;
import org.dashbuilder.model.displayer.DataDisplayerBuilder;

import static org.dashbuilder.model.displayer.DataDisplayerType.*;

public class GalleryDisplayers {

    public static final DataDisplayer MLINE_CHART_SALES_PER_YEAR = new DataDisplayerBuilder()
                .title("Sales Evolution Per Year")
                .type(LINECHART)
                .x("Month")
                .y("Sales in 2012")
                .y("Sales in 2013")
                .y("Sales in 2014")
                .build();
}