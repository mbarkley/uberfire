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
package org.dashbuilder.model.dataset.group;

import java.util.List;
import java.util.ArrayList;

import org.dashbuilder.model.dataset.DataSetOp;
import org.dashbuilder.model.dataset.DataSetOpType;
import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * A data set group operation.
 */
@Portable
public class DataSetGroup implements DataSetOp {

    protected ColumnGroup columnGroup = null;
    protected List<GroupFunction> groupFunctionList = new ArrayList<GroupFunction>();
    protected List<String> selectedIntervalNames = new ArrayList<String>();
    protected NestedGroupType nestedGroupType = null;

    public DataSetOpType getType() {
        return DataSetOpType.GROUP;
    }

    public void setColumnGroup(ColumnGroup columnGroup) {
        this.columnGroup = columnGroup;
    }

    public void addGroupFunction(GroupFunction... groupFunctions) {
        for (GroupFunction groupFunction : groupFunctions) {
            groupFunctionList.add(groupFunction);
        }
    }

    public ColumnGroup getColumnGroup() {
        return columnGroup;
    }

    public List<GroupFunction> getGroupFunctions() {
        return groupFunctionList;
    }

    public NestedGroupType getNestedGroupType() {
        return nestedGroupType;
    }

    public void setNestedGroupType(NestedGroupType nestedGroupType) {
        this.nestedGroupType = nestedGroupType;
    }

    public void addSelectedIntervalNames(String... names) {
        for (String name : names) {
            selectedIntervalNames.add(name);
        }
    }

    public List<String> getSelectedIntervalNames() {
        return selectedIntervalNames;
    }

    public void setSelectedIntervalNames(List<String> names) {
        selectedIntervalNames = names;
    }

    public DataSetGroup cloneInstance() {
        DataSetGroup clone = new DataSetGroup();
        clone.nestedGroupType = nestedGroupType;
        if (columnGroup != null) clone.columnGroup = columnGroup.cloneInstance();
        clone.selectedIntervalNames.addAll(selectedIntervalNames);
        for (GroupFunction groupFunction : groupFunctionList) {
            clone.groupFunctionList.add(groupFunction.cloneInstance());
        }
        return clone;
    }

    public boolean equals(Object obj) {
        try {
            DataSetGroup other = (DataSetGroup) obj;
            if (columnGroup != null && !columnGroup.equals(other.columnGroup)) return false;
            if (groupFunctionList.size() != other.groupFunctionList.size()) return false;
            if (selectedIntervalNames.size() != other.selectedIntervalNames.size()) return false;

            for (int i = 0; i < groupFunctionList.size(); i++) {
                GroupFunction el = groupFunctionList.get(i);
                if (!other.groupFunctionList.contains(el)) return false;
            }
            for (int i = 0; i < selectedIntervalNames.size(); i++) {
                String el = selectedIntervalNames.get(i);
                if (!other.selectedIntervalNames.contains(el)) return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
