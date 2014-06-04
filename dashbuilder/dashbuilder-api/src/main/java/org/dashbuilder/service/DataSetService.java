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
package org.dashbuilder.service;

import org.dashbuilder.model.dataset.DataSetLookup;
import org.dashbuilder.model.dataset.DataSet;
import org.dashbuilder.model.dataset.DataSetMetadata;
import org.dashbuilder.model.dataset.DataSetOp;
import org.jboss.errai.bus.server.annotations.Remote;

/**
 * Interface for requesting access to data sets stored in the backend.
 */
@Remote
public interface DataSetService {

    DataSetMetadata fetchMetadata(String uid);
    DataSet lookupDataSet(DataSetLookup lookup);
}
