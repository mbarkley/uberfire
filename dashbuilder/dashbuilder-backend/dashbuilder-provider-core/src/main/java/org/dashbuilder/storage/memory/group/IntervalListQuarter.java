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
package org.dashbuilder.storage.memory.group;

import org.dashbuilder.model.dataset.group.Domain;
import org.dashbuilder.model.date.Month;
import org.dashbuilder.model.date.Quarter;

/**
 * List of the 4-quarter intervals present in a year.
 */
public class IntervalListQuarter extends IntervalListMonth {

    public IntervalListQuarter(Domain domain) {
        super(domain);

        Month firstMonth = domain.getFirstMonthOfYear();
        int monthIndex = firstMonth.getIndex();

        for (int i = 0; i < 4; i++) {
            Quarter quarter = Quarter.getByIndex(i);
            Interval interval = new Interval(quarter.toString());
            this.add(interval);

            for (int j = 0; j < 3; j++) {
                intervalMap.put(monthIndex, interval);
                monthIndex = Month.nextIndex(monthIndex);
            }
        }
    }
}
