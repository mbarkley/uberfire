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
package org.dashbuilder.dataset.client.widgets;

import com.github.gwtbootstrap.client.ui.*;
import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import org.dashbuilder.dataset.client.resources.bundles.DataSetClientResources;
import org.dashbuilder.dataset.client.resources.i18n.DataSetExplorerConstants;
import org.dashbuilder.dataset.def.DataSetDef;

import javax.enterprise.context.Dependent;
import java.util.LinkedList;
import java.util.List;

// TODO
@Dependent
public class DataSetExplorerView extends Composite implements DataSetExplorer.View {

    interface DataSetExplorerViewBinder extends UiBinder<Widget, DataSetExplorerView> {}
    private static DataSetExplorerViewBinder uiBinder = GWT.create(DataSetExplorerViewBinder.class);

    private DataSetExplorer explorer;
    private List<DataSetDef> dataSets;
    
    @UiField
    Accordion dataSetsAccordion;
    
    @UiField
    Label label;
    
    public DataSetExplorerView() {
        initWidget(uiBinder.createAndBindUi(this));
        dataSets = new LinkedList<DataSetDef>();
    }

    @Override
    public void init(DataSetExplorer presenter) {
        this.explorer =  presenter;
        show();
    }   

    @Override
    public void set(List<DataSetDef> dataSetDefs) {
        this.dataSets = dataSetDefs;
    }

    @Override
    public boolean add(DataSetDef dataSetDef) {
        return dataSets.add(dataSetDef);
    }

    @Override
    public boolean remove(DataSetDef dataSetDef) {
        return dataSets.remove(dataSetDef);
    }

    @Override
    public void clear() {
        dataSets.clear();
        clearView();
    }
    
    private void clearView() {
        label.setText("");
        dataSetsAccordion.clear();
    }
    
    @Override
    public void show() {
        clearView();

        if (!dataSets.isEmpty()) {
            label.setText(DataSetExplorerConstants.INSTANCE.availableDataSets());
            for (DataSetDef dataSetDef : dataSets) {
                final AccordionGroup accordionGroup = buildDataSetAccordionGroup(dataSetDef);
                dataSetsAccordion.add(accordionGroup);
            }
            
        } else {
            label.setText(DataSetExplorerConstants.INSTANCE.noDataSets());
        }
        dataSetsAccordion.setVisible(true);
    }
    
    private AccordionGroup buildDataSetAccordionGroup(DataSetDef dataSetDef) {
        final AccordionGroup accordionGroup = new AccordionGroup();

        // Heading.
        accordionGroup.setHeading(dataSetDef.getUUID());
        // CollapseTrigger collapseTrigger = new CollapseTrigger();
        
        // Icon for provider type.
        final Image typeIcon = buildTypeIcon(dataSetDef);
        if (typeIcon != null) accordionGroup.addCustomTrigger(typeIcon);
        
        buildDescription(dataSetDef, accordionGroup);
        
        return accordionGroup;
    } 
    
    private Image buildTypeIcon(DataSetDef dataSetDef) {
        Image typeIcon = null;
        switch (dataSetDef.getProvider()) {
            case BEAN:
                typeIcon = new Image(DataSetClientResources.INSTANCE.images().javaIconSmall());
                break;
            case CSV:
                typeIcon = new Image(DataSetClientResources.INSTANCE.images().csvIconSmall());
                break;
            case SQL:
                typeIcon = new Image(DataSetClientResources.INSTANCE.images().sqlIconSmall());
                break;
            case ELASTICSEARCH:
                typeIcon = new Image(DataSetClientResources.INSTANCE.images().elIconSmall());
                break;
        }
        return typeIcon;
        
    }
    
    private void buildDescription(DataSetDef dataSetDef, Panel parent) {
        if (parent != null) {
            // Labels.
            Label cacheEnabledLabel;
            Label pushEnabledLabel;
            Label refreshEnabledLabel;
            if (dataSetDef.isCacheEnabled())
                cacheEnabledLabel = new Label(DataSetExplorerConstants.INSTANCE.cacheEnabled());
            else cacheEnabledLabel = new Label(DataSetExplorerConstants.INSTANCE.cacheDisabled());
            if (dataSetDef.isPushEnabled())
                pushEnabledLabel = new Label(DataSetExplorerConstants.INSTANCE.pushEnabled());
            else pushEnabledLabel = new Label(DataSetExplorerConstants.INSTANCE.pushDisabled());
            if (dataSetDef.isRefreshAlways())
                refreshEnabledLabel = new Label(DataSetExplorerConstants.INSTANCE.refreshEnabled());
            else refreshEnabledLabel = new Label(DataSetExplorerConstants.INSTANCE.refreshDisabled());

            // Buttons.
            com.github.gwtbootstrap.client.ui.Button editButton = new Button(DataSetExplorerConstants.INSTANCE.edit());
            com.github.gwtbootstrap.client.ui.Button deleteButton = new Button(DataSetExplorerConstants.INSTANCE.delete());
            final boolean isPublic = dataSetDef.isPublic();
            editButton.setEnabled(isPublic);
            deleteButton.setEnabled(isPublic);
            
            // Add into parent container.
            parent.add(cacheEnabledLabel);
            parent.add(pushEnabledLabel);
            parent.add(refreshEnabledLabel);
            parent.add(editButton);
            parent.add(deleteButton);

        }
    }

}
