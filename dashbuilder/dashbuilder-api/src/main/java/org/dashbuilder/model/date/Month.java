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
package org.dashbuilder.model.date;

import org.jboss.errai.common.client.api.annotations.Portable;


@Portable
public enum Month {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;

    private static Month[] _monthArray = values();

    public int getIndex() {
        for (int i = 0; i < _monthArray.length; i++) {
            Month month = _monthArray[i];
            if (this.equals(month)) return i;
        }
        return -1;
    }

    public static int nextIndex(int index) {
        index++;
        if (index < _monthArray.length) return index;
        return 0;
    }

    public static Month[] getAll() {
        return _monthArray;
    }

    public static Month getByName(String name) {
        return valueOf(name.toUpperCase());
    }

    public static Month getByIndex(int index) {
        return _monthArray[index];
    }
}
