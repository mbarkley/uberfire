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
package org.dashbuilder.client.widgets.dataset.editor.widgets.editors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.dashbuilder.dataset.DataColumn;
import org.dashbuilder.dataset.DataSet;
import org.dashbuilder.dataset.client.validation.editors.DataSetDefEditor;
import org.dashbuilder.dataset.def.DataSetDef;
import org.dashbuilder.dataset.group.DataSetGroup;
import org.dashbuilder.displayer.DisplayerSettingsFactory;
import org.dashbuilder.displayer.client.Displayer;
import org.dashbuilder.displayer.client.DisplayerCoordinator;
import org.dashbuilder.displayer.client.DisplayerHelper;
import org.dashbuilder.displayer.client.DisplayerListener;

import javax.enterprise.context.Dependent;
import java.util.List;

/**
 * <p>This is the view implementation widget for Data Set Editor widget for editing data set columns, initial filter and testing the checking in a table displayer.</p>
 */
@Dependent
public class DataSetColumnsAndFilterEditor extends AbstractDataSetDefEditor implements DataSetDefEditor {

    interface DataSetColumnsAndFilterEditorBinder extends UiBinder<Widget, DataSetColumnsAndFilterEditor> {}
    private static DataSetColumnsAndFilterEditorBinder uiBinder = GWT.create(DataSetColumnsAndFilterEditorBinder.class);

    @UiField
    FlowPanel columnFilterTablePanel;

    @UiField
    FlowPanel tablePanel;
    
    Displayer tableDisplayer = null;
    private DataSet dataSet = null;

    private boolean isEditMode;

    public DataSetColumnsAndFilterEditor() {
        // Initialize the widget.
        initWidget(uiBinder.createAndBindUi(this));
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    @Override
    public void showErrors(List<EditorError> errors) {
        consumeErrors(errors);
    }

    @Override
    public void set(DataSetDef dataSetDef) {
        super.set(dataSetDef);
    }
    
    public void build() {
        
        showTableDisplayer();
        
        // TODO: Show loading screen...
       
    }
    
    private void showTableDisplayer() {
        
        // Clear current view.
        clearView();
        
        // Build the table displayer.
        tableDisplayer = buildTableDisplayer();
        if (tableDisplayer != null) {

            // Create and draw the preview table.
            final DisplayerCoordinator coordinator = new DisplayerCoordinator();
            coordinator.addDisplayer(tableDisplayer);
            tablePanel.add(tableDisplayer);
            coordinator.drawAll();

            final TableListener tableListener = new TableListener();
            tableDisplayer.addListener(tableListener);
        }

    }
    
    private void showColumnsView() {
        if (dataSet != null) {
            List<DataColumn> columns = dataSet.getColumns();
            if (columns != null) {
                for (DataColumn column : columns) {
                    GWT.log("Found column");
                    GWT.log("************");
                    GWT.log("id="+column.getId());
                    GWT.log("name="+column.getName());
                    GWT.log("type="+column.getColumnType());
                }
            }
        }
        
    }

    private Displayer buildTableDisplayer() {
        if (dataSetDef != null) {
            return  DisplayerHelper.lookupDisplayer(
                    DisplayerSettingsFactory.newTableSettings()
                            .dataset(dataSetDef.getUUID())
                            .titleVisible(false)
                            .tablePageSize(10)
                            .tableOrderEnabled(false)
                            .filterOn(false, false, false)
                            .buildSettings());
        }
        
        return null;
    }

    private void clearView() {
        tablePanel.clear();
    }

    private class TableListener implements DisplayerListener {

        public void onDraw(Displayer displayer) {
            DataSetColumnsAndFilterEditor.this.dataSet = displayer.getDataSetHandler().getLastDataSet();
            if (DataSetColumnsAndFilterEditor.this.dataSet != null) {
                showColumnsView();
            }
        }

        public void onRedraw(Displayer displayer) {

        }

        public void onClose(Displayer displayer) {

        }

        public void onGroupIntervalsSelected(Displayer displayer, DataSetGroup groupOp) {

        }

        public void onGroupIntervalsReset(Displayer displayer, List<DataSetGroup> groupOps) {

        }
    }
}
