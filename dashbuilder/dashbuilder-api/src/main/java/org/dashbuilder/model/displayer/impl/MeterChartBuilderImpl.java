/**
 * Copyright (C) 2012 JBoss Inc
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
package org.dashbuilder.model.displayer.impl;

import org.dashbuilder.model.displayer.ChartBuilder;
import org.dashbuilder.model.displayer.DataDisplayer;
import org.dashbuilder.model.displayer.MeterChartBuilder;
import org.dashbuilder.model.displayer.MeterChartDisplayer;

public class MeterChartBuilderImpl extends AbstractChartBuilder<BarChartBuilderImpl> implements MeterChartBuilder {

    protected DataDisplayer createDisplayer() {
        return new MeterChartDisplayer();
    }

    public MeterChartBuilder meter(long start, long warning, long critical, long end) {
        MeterChartDisplayer meterDisplayer = (MeterChartDisplayer) dataDisplayer;
        meterDisplayer.setMeterStart(start);
        meterDisplayer.setMeterWarning(warning);
        meterDisplayer.setMeterCritical(critical);
        meterDisplayer.setMeterEnd(end);
        return this;
    }
}
