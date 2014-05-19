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
package org.dashbuilder.model.dataset.sort;

import org.dashbuilder.model.dataset.group.GroupStrategy;
import org.dashbuilder.model.date.DayOfWeek;
import org.dashbuilder.model.date.Month;
import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * A column sort criteria
 */
@Portable
public class SortColumn {

    protected String columnId = null;
    protected SortOrder order = SortOrder.UNSPECIFIED;

    public SortColumn() {
    }

    public SortColumn(String columnId, SortOrder order) {
        this.columnId = columnId;
        this.order = order;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public SortOrder getOrder() {
        return order;
    }

    public void setOrder(SortOrder order) {
        this.order = order;
    }

    public boolean equals(Object obj) {
        try {
            SortColumn other = (SortColumn) obj;
            if (columnId != null && !columnId.equals(other.columnId)) return false;
            if (order != null && !order.equals(other.order)) return false;
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
