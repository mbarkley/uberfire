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
package org.dashbuilder.model.dataset.filter;

import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * A single function filter definition
 */
@Portable
public class FilterCoreFunction extends FilterColumn {

    protected FilterCoreFunctionType type = null;
    protected List<Comparable> parameters = new ArrayList();

    public FilterCoreFunction() {
    }

    public FilterCoreFunction(String columnId, FilterCoreFunctionType type, Comparable... parameters) {
        super(columnId);
        this.type = type;
        this.setParameters(parameters);
    }

    public FilterCoreFunctionType getType() {
        return type;
    }

    public void setType(FilterCoreFunctionType type) {
        this.type = type;
    }

    public List<Comparable> getParameters() {
        return parameters;
    }

    public void setParameters(Comparable... parameters) {
        this.parameters.clear();
        for (Comparable param : parameters) {
            this.parameters.add(param);
        }
    }

    public void setFunctionParams(List<Comparable> functionParams) {
        this.parameters = functionParams;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        if (FilterCoreFunctionType.IS_BETWEEN.equals(type)) {
            out.append(columnId).append(" between (");
            appendParameters(out);
            out.append(")");
        }
        else if (FilterCoreFunctionType.IS_GREATER_THAN.equals(type)) {
            out.append(columnId).append(" > ");
            appendParameters(out);
        }
        else if (FilterCoreFunctionType.IS_GREATER_OR_EQUALS_TO.equals(type)) {
            out.append(columnId).append(" >= ");
            appendParameters(out);
        }
        else if (FilterCoreFunctionType.IS_LOWER_THAN.equals(type)) {
            out.append(columnId).append(" < ");
            appendParameters(out);
        }
        else if (FilterCoreFunctionType.IS_LOWER_OR_EQUALS_TO.equals(type)) {
            out.append(columnId).append(" <= ");
            appendParameters(out);
        }
        else if (FilterCoreFunctionType.IS_EQUALS_TO.equals(type)) {
            out.append(columnId).append(" = ");
            appendParameters(out);
        }
        else if (FilterCoreFunctionType.IS_NOT_EQUALS_TO.equals(type)) {
            out.append(columnId).append(" != ");
            appendParameters(out);
        }
        else if (FilterCoreFunctionType.IS_NULL.equals(type)) {
            out.append(columnId).append(" is_null ");
            appendParameters(out);
        }
        else if (FilterCoreFunctionType.IS_NOT_NULL.equals(type)) {
            out.append(columnId).append(" is_not_null ");
            appendParameters(out);
        }
        return out.toString();
    }

    private StringBuilder appendParameters(StringBuilder out) {
        for (int i=0; i< parameters.size();  i++) {
            if (i > 0) out.append(", ");
            out.append(parameters.get(i));
        }
        return out;
    }
    public boolean equals(Object obj) {
        try {
            FilterCoreFunction other = (FilterCoreFunction) obj;
            if (!super.equals(other)) return false;

            if (type != null && !type.equals(other.type)) return false;
            if (parameters.size() != other.parameters.size()) return false;
            for (Object param : parameters) {
                if (!other.parameters.contains(param)) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
