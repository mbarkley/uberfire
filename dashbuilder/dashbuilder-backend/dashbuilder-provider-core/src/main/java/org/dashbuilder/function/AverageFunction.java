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
package org.dashbuilder.function;

import java.util.List;

/**
 * It calculates the average value of a set of numbers.
 */
public class AverageFunction extends SumFunction {

    /**
     * The code of the function.
     */
    public static final String CODE = "average";
    
    public AverageFunction() {
        super();
    }

    public String getCode() {
        return CODE;
    }

    public double scalar(List values) {
        if (values == null || values.isEmpty()) return 0;
        double average = super.scalar(values) / values.size();
        double ret = round(average, precission);
        return ret;
    }

    public double scalar(List values, List<Integer> rows) {
        if (values == null || values.isEmpty()) return 0;
        double average = super.scalar(values, rows) / rows.size();
        double ret = round(average, precission);
        return ret;
    }

}