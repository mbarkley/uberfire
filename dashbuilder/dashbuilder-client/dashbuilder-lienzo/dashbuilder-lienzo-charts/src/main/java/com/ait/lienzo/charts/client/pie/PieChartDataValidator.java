/*
   Copyright (c) 2014 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.charts.client.pie;

import com.ait.lienzo.client.core.shape.json.validators.*;
import com.google.gwt.json.client.JSONValue;

public final class PieChartDataValidator extends ObjectValidator {

    public static final PieChartDataValidator INSTANCE = new PieChartDataValidator();

    public PieChartDataValidator()
    {
        super("pieChartData");

        addAttribute("dataTable", new IAttributeTypeValidator() {

            @Override
            public void validate(JSONValue jval, ValidationContext ctx) throws ValidationException {
                // TODO
            }
        }, true);
        addAttribute("categoriesProperty", StringValidator.INSTANCE, false);
        addAttribute("valuesProperty", StringValidator.INSTANCE, true);

    }
    
}