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
package org.dashbuilder.displayer.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Composite;
import org.dashbuilder.dataset.group.ColumnGroup;
import org.dashbuilder.dataset.group.DataSetGroup;
import org.dashbuilder.dataset.group.GroupStrategy;
import org.dashbuilder.dataset.sort.ColumnSort;
import org.dashbuilder.dataset.sort.DataSetSort;
import org.dashbuilder.dataset.sort.SortOrder;
import org.dashbuilder.displayer.DataDisplayer;

/**
 * Base class for implementing data displayer editor widgets.
 */
public abstract class AbstractDisplayerEditor<T extends DataDisplayer> extends Composite implements DisplayerEditor<T> {

    protected T dataDisplayer;
    protected DisplayerEditorListener listener;

    public T getDataDisplayer() {
        return dataDisplayer;
    }

    public void setDataDisplayer(T dataDisplayer) {
        this.dataDisplayer = dataDisplayer;
    }

    public DisplayerEditorListener getListener() {
        return listener;
    }

    public void setListener(DisplayerEditorListener listener) {
        this.listener = listener;
    }

    protected void notityChanges() {
        listener.onDisplayerChanged(this);
    }
}