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
package org.dashbuilder.client.displayer;

import com.google.gwt.user.client.ui.Composite;
import org.dashbuilder.client.dataset.DataSet;

public class DataDisplayerViewer extends Composite {

    protected DataSet dataSet;
    protected DataDisplayer dataDisplayer;

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public DataDisplayer getDataDisplayer() {
        return dataDisplayer;
    }

    public void setDataDisplayer(DataDisplayer dataDisplayer) {
        this.dataDisplayer = dataDisplayer;
    }
}
