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
package org.dashbuilder.dataset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dashbuilder.dataset.group.AggregateFunctionType;
import org.dashbuilder.dataset.group.DataSetGroup;
import org.dashbuilder.dataset.impl.DataSetLookupBuilderImpl;

/**
 * A set of constraints over the structure of a DataSetLookup instance.
 */
public class DataSetLookupConstraints extends DataSetConstraints<DataSetLookupConstraints> {

    public static final int ERROR_GROUP_NUMBER = 200;
    public static final int ERROR_GROUP_NOT_ALLOWED = 201;
    public static final int ERROR_GROUP_REQUIRED = 203;

    protected boolean groupAllowed = true;
    protected boolean groupRequired = false;
    protected int maxGroups = -1;
    protected String groupsTitle = "Rows";
    protected String columnsTitle = "Columns";
    protected boolean groupColumn = false;
    protected boolean functionRequired = false;
    protected Map<Integer,String> columnTitleMap = new HashMap<Integer,String>();

    public boolean isGroupAllowed() {
        return groupAllowed;
    }

    public DataSetLookupConstraints setGroupAllowed(boolean groupAllowed) {
        this.groupAllowed = groupAllowed;
        return this;
    }

    public boolean isGroupRequired() {
        return groupRequired;
    }

    public DataSetLookupConstraints setGroupRequired(boolean groupRequired) {
        this.groupRequired = groupRequired;
        return this;
    }

    public int getMaxGroups() {
        return maxGroups;
    }

    public DataSetLookupConstraints setMaxGroups(int maxGroups) {
        this.maxGroups = maxGroups;
        return this;
    }

    public String getGroupsTitle() {
        return groupsTitle;
    }

    public DataSetLookupConstraints setGroupsTitle(String groupsTitle) {
        this.groupsTitle = groupsTitle;
        return this;
    }

    public String getColumnsTitle() {
        return columnsTitle;
    }

    public DataSetLookupConstraints setColumnsTitle(String columnsTitle) {
        this.columnsTitle = columnsTitle;
        return this;
    }

    public DataSetLookupConstraints setColumnTitle(Integer index, String title) {
        columnTitleMap.put(index, title);
        return this;
    }

    public String getColumnTitle(Integer index) {
        return columnTitleMap.get(index);
    }

    public boolean isGroupColumn() {
        return groupColumn;
    }

    public DataSetLookupConstraints setGroupColumn(boolean groupColumn) {
        this.groupColumn = groupColumn;
        return this;
    }

    public boolean isFunctionRequired() {
        return functionRequired;
    }

    public DataSetLookupConstraints setFunctionRequired(boolean functionRequired) {
        this.functionRequired = functionRequired;
        return this;
    }

    public ValidationError check(DataSetLookup lookup) {

        List<DataSetGroup> grOps = lookup.getOperationList(DataSetGroup.class);
        if (!groupAllowed && grOps.size() > 0) {
            return createValidationError(ERROR_GROUP_NOT_ALLOWED);
        }
        if (groupRequired && grOps.size() == 0) {
            return createValidationError(ERROR_GROUP_REQUIRED);
        }
        if (maxGroups != -1 && grOps.size() > maxGroups) {
            return createValidationError(ERROR_GROUP_NUMBER);
        }
        return null;
    }

    protected ValidationError createValidationError(int error) {
        switch (error) {
            case ERROR_GROUP_NOT_ALLOWED:
                return new ValidationError(error, "Group not allowed");
            case ERROR_GROUP_REQUIRED:
                String groupColumn = groupsTitle != null ? groupsTitle : "Group";
                return new ValidationError(error, groupColumn + " column required");
            case ERROR_GROUP_NUMBER:
                return new ValidationError(error, "Max. groups allowed exceeded " + maxGroups);
        }
        return new ValidationError(error);
    }

    public DataSetLookup newDataSetLookup(DataSetMetadata metatada) {
        DataSetLookupBuilder<DataSetLookupBuilderImpl> builder = DataSetFactory.newDataSetLookupBuilder();
        builder.dataset(metatada.getUUID());

        Set<Integer> exclude = new HashSet<Integer>();
        int startIndex = 0;

        // A group lookup requires to add a group-ready column
        if (groupRequired) {
            int groupIdx = getGroupColumn(metatada);
            if (groupIdx == -1) {
                throw new IllegalStateException("The data set does not contains group-able columns (label or date)");
            }
            // Add the group column
            exclude.add(groupIdx);
            builder.group(metatada.getColumnId(groupIdx));
            builder.column(metatada.getColumnId(groupIdx));
            startIndex = 1;
        }
        // If no target columns has been specified then take them all
        ColumnType[] types = getColumnTypes();
        if (types == null || types.length == 0) {

            if (maxColumns > 0 && maxColumns < metatada.getNumberOfColumns()) types = new ColumnType[maxColumns];
            else types = new ColumnType[metatada.getNumberOfColumns()];

            for (int i = 0; i < types.length; i++) {
                types[i] = metatada.getColumnType(i);
            }
        }
        // Add the columns to the lookup
        for (int i=startIndex; i<types.length; i++) {
            ColumnType targetType = types[i];

            // Do the best to get a new (not already added) column for the targetType.
            int idx = getTargetColumn(metatada, targetType, exclude);

            // Otherwise, get the first column available.
            if (idx == -1) idx = getTargetColumn(metatada, exclude);

            String columnId = metatada.getColumnId(idx);
            ColumnType columnType = metatada.getColumnType(idx);
            exclude.add(idx);

            if (ColumnType.LABEL.equals(targetType)) {
                if (functionRequired) builder.column(AggregateFunctionType.COUNT, "#items");
                else builder.column(columnId);
            }
            else if (ColumnType.TEXT.equals(targetType)) {
                if (functionRequired) builder.column(AggregateFunctionType.COUNT, "#items");
                else builder.column(columnId);
            }
            else if (ColumnType.DATE.equals(targetType)) {
                if (functionRequired) builder.column(AggregateFunctionType.COUNT, "#items");
                else builder.column(columnId);
            }
            else if (ColumnType.NUMBER.equals(targetType)) {
                if (groupRequired || functionRequired) {
                    if (ColumnType.LABEL.equals(columnType)) {
                        builder.column(AggregateFunctionType.COUNT, "#items");
                    } else if (ColumnType.LABEL.equals(columnType)) {
                        builder.column(AggregateFunctionType.COUNT, "#items");
                    } else if (ColumnType.NUMBER.equals(columnType)) {
                        builder.column(columnId, AggregateFunctionType.SUM);
                    }
                } else {
                    builder.column(columnId);
                }
            }
        }
        return builder.buildLookup();
    }

    private int getGroupColumn(DataSetMetadata metatada) {
        for (int i=0; i<metatada.getNumberOfColumns(); i++) {
            ColumnType type = metatada.getColumnType(i);
            if (type.equals(ColumnType.LABEL)) return i;
        }
        for (int i=0; i<metatada.getNumberOfColumns(); i++) {
            ColumnType type = metatada.getColumnType(i);
            if (type.equals(ColumnType.DATE)) return i;
        }
        return -1;
    }

    private int getTargetColumn(DataSetMetadata metatada, ColumnType type, Set<Integer> exclude) {
        int target = -1;
        for (int i=0; i<metatada.getNumberOfColumns(); i++) {
            if (type.equals(metatada.getColumnType(i))) {
                if (target == -1) {
                    target = i;
                }
                if (!exclude.contains(i)) {
                    return i;
                }
            }
        }
        return target;
    }

    private int getTargetColumn(DataSetMetadata metatada, Set<Integer> exclude) {
        for (int i=0; i<metatada.getNumberOfColumns(); i++) {
            if (!exclude.contains(i)) {
                return i;
            }
        }
        return 0;
    }
}