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
package org.dashbuilder.dataprovider;

import org.dashbuilder.dataset.def.CSVDataSetDef;
import org.dashbuilder.dataset.def.DataSetDef;
import org.dashbuilder.dataset.def.StaticDataSetDef;
import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * An enumeration of the available data set provider types.
 */
@Portable
public enum DataSetProviderType {

    /**
     * For accessing statically registered data set which are created by calling directly to the data set API.
     */
    STATIC,

    /**
     * For accessing data sets defined as an SQL query over an existing data source.
     */
    SQL,

    /**
     * For accessing data sets that are the result of loading all the rows of a CSV file.
     */
    CSV;

    public static DataSetProviderType getByName(String name) {
        return valueOf(name.toUpperCase());
    }

    public static DataSetDef createDataSetDef(DataSetProviderType type) {
        switch (type) {
            case STATIC: return new StaticDataSetDef();
            case CSV: return new CSVDataSetDef();
        }
        throw new RuntimeException("Unknown type: " + type);
    }
}
