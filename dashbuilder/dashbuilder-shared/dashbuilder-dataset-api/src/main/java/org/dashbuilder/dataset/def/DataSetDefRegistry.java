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
package org.dashbuilder.dataset.def;

import java.util.List;

/**
 * Data set definitions registry service
 */
public interface DataSetDefRegistry {

    /**
     * Get the data set definitions available.
     *
     * @param onlyPublic Get only those public (shareable) data set definition (public flag set to true)
     */
    List<DataSetDef> getDataSetDefs(boolean onlyPublic);

    /**
     * Get the definition for the specified data set.
     *
     * @param uuid The unique universal identifier of the data set
     * @return The data set definition instance or null if the definition does not exist.
     */
    DataSetDef getDataSetDef(String uuid);

    /**
     * Add a data set definition to the registry.
     *
     * @param dataSetDef The data set definition
     */
    void registerDataSetDef(DataSetDef dataSetDef);

    /**
     * Add a data set definition to the registry.
     *
     * @param dataSetDef The data set definition
     * @param subjectId, The identifier of the subject making the request. If null is ignored.
     * @param message, A message top store along the registration request. If null is ignored.
     */
    void registerDataSetDef(DataSetDef dataSetDef, String subjectId, String message);

    /**
     * Removes the specified data set definition.
     *
     * @param uuid The unique universal identifier of the data set
     * @return The removed data set definition or null if the definition does not exist.
     */
    DataSetDef removeDataSetDef(String uuid);

    /**
     * Removes the specified data set definition.
     *
     * @param uuid The unique universal identifier of the data set
     * @param subjectId, The identifier of the subject making the request. If null is ignored.
     * @param message, A message top store along the registration request. If null is ignored.
     * @return The removed data set definition or null if the definition does not exist.
     */
    DataSetDef removeDataSetDef(String uuid, String subjectId, String message);
}
