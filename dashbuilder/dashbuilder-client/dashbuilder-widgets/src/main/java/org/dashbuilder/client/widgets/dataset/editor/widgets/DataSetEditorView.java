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
package org.dashbuilder.client.widgets.dataset.editor.widgets;

import com.github.gwtbootstrap.client.ui.*;
import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.dashbuilder.client.widgets.animations.PanelsSwitchVisibilityAnimation;
import org.dashbuilder.client.widgets.dataset.editor.DataSetDefEditWorkflow;
import org.dashbuilder.client.widgets.dataset.editor.widgets.editors.*;
import org.dashbuilder.client.widgets.dataset.editor.widgets.editors.bean.BeanDataSetDefAttributesEditor;
import org.dashbuilder.client.widgets.dataset.editor.widgets.editors.csv.CSVDataSetDefAttributesEditor;
import org.dashbuilder.client.widgets.dataset.editor.widgets.editors.elasticsearch.ELDataSetDefAttributesEditor;
import org.dashbuilder.client.widgets.dataset.editor.widgets.editors.sql.SQLDataSetDefAttributesEditor;
import org.dashbuilder.client.widgets.resources.i18n.DataSetEditorConstants;
import org.dashbuilder.client.widgets.resources.i18n.DataSetEditorMessages;
import org.dashbuilder.dataprovider.DataSetProviderType;
import org.dashbuilder.dataset.DataColumn;
import org.dashbuilder.dataset.DataSet;
import org.dashbuilder.dataset.def.*;
import org.dashbuilder.displayer.client.DisplayerListener;
import org.dashbuilder.displayer.client.widgets.filter.DataSetFilterEditor;
import javax.enterprise.context.Dependent;
import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Default view for DataSetEditor presenter view.</p> 
 */
@Dependent
public class DataSetEditorView extends Composite implements DataSetEditor.View {

    private static final int ANIMATION_DURATION = 2000;
    
    interface DataSetEditorViewBinder extends UiBinder<Widget, DataSetEditorView> {}
    private static DataSetEditorViewBinder uiBinder = GWT.create(DataSetEditorViewBinder.class);

    interface DataSetEditorViewStyle extends CssResource {
        String well_ghostwhite();
        String disabledBar();
    }

    @UiField
    DataSetEditorViewStyle style;
    
    @UiField
    FlowPanel mainPanel;

    @UiField
    FlowPanel titlePanel;
    
    @UiField
    Heading title;
    
    @UiField
    StackProgressBar progressBar;
    
    @UiField
    Bar providerBar;
    
    @UiField
    Bar columnsFilterBar;

    @UiField
    Bar advancedAttrsBar;
    
    @UiField
    HTMLPanel initialViewPanel;

    @UiField
    HTML dataSetCountText;

    @UiField
    Hyperlink newDataSetLink;

    @UiField
    FlowPanel providerSelectionViewPanel;

    @UiField
    DataSetProviderTypeEditor dataSetProviderTypeEditor;

    @UiField
    com.github.gwtbootstrap.client.ui.TabPanel tabPanel;
    
    @UiField
    FlowPanel tabViewPanel;
    
    @UiField
    Tab dataConfigurationTab;

    @UiField
    Tab dataAdvancedConfigurationTab;
    
    @UiField
    FlowPanel basicAttributesEditionViewPanel;

    @UiField
    DataSetBasicAttributesEditor dataSetBasicAttributesEditor;

    @UiField
    FlowPanel specificProviderAttributesPanel;
    
    @UiField
    FlowPanel sqlAttributesEditionViewPanel;

    @UiField
    SQLDataSetDefAttributesEditor sqlDataSetDefAttributesEditor;
    
    @UiField
    FlowPanel testButtonPanel;
    
    @UiField
    Button testButton;
    
    @UiField
    FlowPanel filterColumnsPreviewTablePanel;
    
    @UiField
    FlowPanel backToSpecificAttrsButtonPanel;
    
    @UiField
    Button backToSpecificAttrsEditionButton;
    
    @UiField
    FlowPanel previewTableEditionViewPanel;

    @UiField
    DataSetPreviewEditor previewTableEditor;

    @UiField
    FlowPanel filterAndColumnsEditionViewPanel;

    @UiField
    com.github.gwtbootstrap.client.ui.TabPanel filterAndColumnsTabPanel;

    @UiField
    Tab columnsTab;

    @UiField
    Tab filterTab;
    
    @UiField
    DataSetColumnsEditor columnsEditor;

    @UiField
    FlowPanel csvAttributesEditionViewPanel;

    @UiField
    CSVDataSetDefAttributesEditor csvDataSetDefAttributesEditor;

    @UiField
    FlowPanel beanAttributesEditionViewPanel;

    @UiField
    BeanDataSetDefAttributesEditor beanDataSetDefAttributesEditor;

    @UiField
    FlowPanel elAttributesEditionViewPanel;

    @UiField
    ELDataSetDefAttributesEditor elDataSetDefAttributesEditor;
    
    @UiField
    FlowPanel advancedAttributesEditionViewPanel;

    @UiField
    DataSetAdvancedAttributesEditor dataSetAdvancedAttributesEditor;

    @UiField
    FlowPanel buttonsPanel;

    @UiField
    Button cancelButton;

    @UiField
    Button nextButton;
    
    @UiField
    Popover nextButtonPopover;
    
    private DataSetDef dataSetDef = null;
    
    private boolean isEditMode = true;
    private DataSetDefEditWorkflow workflow;
    
    private HandlerRegistration nextButtonHandlerRegistration = null;
    private HandlerRegistration cancelButtonHandlerRegistration = null;
    private HandlerRegistration testButtonHandlerRegistration = null;

    /**
     * <p>The animation for switching between specific provider edition view and filter, columns and table preview view.</p> 
     */
    private PanelsSwitchVisibilityAnimation animation;
    
    private final ClickHandler backToSpecificAttrsEditionButtonHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            showSpecificProviderAttrsEditionView(null);
        }
    };

    public DataSetEditorView() {
        initWidget(uiBinder.createAndBindUi(this));

        // Configure back to provider settings button's click handler.
        backToSpecificAttrsEditionButton.addClickHandler(backToSpecificAttrsEditionButtonHandler);
        
        // Configure animations.
        animation = new PanelsSwitchVisibilityAnimation(specificProviderAttributesPanel,
                filterColumnsPreviewTablePanel);
        
        showEmptyView();
    }

    @Override
    public DataSetEditor.View setEditMode(final boolean editMode) {
        this.isEditMode = editMode;
        
        return this;
    }

    private void showEmptyView() {
        clearView();
    }
    
    public DataSetEditor.View showHomeView(final int dsetCount, final ClickHandler newDataSetHandler) {
        clearView();
        
        // View title.
        showTitle();
        
        dataSetCountText.setText(DataSetEditorMessages.INSTANCE.dataSetCount(dsetCount));
        newDataSetLink.addClickHandler(newDataSetHandler);
        initialViewPanel.setVisible(true);
        mainPanel.addStyleName(style.well_ghostwhite());

        return this;
    }
    
    private boolean isHomeViewVisible() {
        return initialViewPanel.isVisible();
    }

    @Override
    public DataSetEditor.View edit(final DataSetDef dataSetDef, final DataSetDefEditWorkflow workflow) {
        this.dataSetDef = dataSetDef;
        this.workflow = workflow;

        // Reset current view.
        clearView();
        mainPanel.removeStyleName(style.well_ghostwhite());

        // Clear current workflow state.
        this.workflow.clear();
        
        // Set the definition to be edited in to sub-editors.
        setDataSetDefIntoEditor();
        
        return this;
    }


    @Override
    public DataSetEditor.View showProviderSelectionView() {
        workflow.edit(dataSetProviderTypeEditor, dataSetDef);

        // View title.
        showTitle();
        
        // Progress bar.
        progressStep1();

        providerSelectionViewPanel.setVisible(true);
        dataSetProviderTypeEditor.setEditMode(!isEditMode);

        return this;
    }

    private boolean isProviderSelectionViewVisible() {
        return providerSelectionViewPanel.isVisible();
    }


    @Override
    public DataSetEditor.View showBasicAttributesEditionView() {
        workflow.edit(dataSetBasicAttributesEditor, dataSetDef);

        // View title.
        showTitle();

        // Progress bar.
        progressStep1();

        basicAttributesEditionViewPanel.setVisible(true);
        dataSetBasicAttributesEditor.setEditMode(true);
        activeDataConfigurationTab();
        return this;
    }

    private boolean isBasicAttributesEditionViewVisible() {
        return basicAttributesEditionViewPanel.isVisible();
    }

    @Override
    public DataSetEditor.View showSQLAttributesEditorView(final ClickHandler testHandler) {
        workflow.edit(sqlDataSetDefAttributesEditor, (SQLDataSetDef) dataSetDef);
        sqlAttributesEditionViewPanel.setVisible(true);
        sqlDataSetDefAttributesEditor.setEditMode(true);
        showSpecificProviderAttrsEditionView(testHandler);
        return this;
    }

    private boolean isSQLAttributesEditorViewVisible() {
        return sqlAttributesEditionViewPanel.isVisible();
    }

    @Override
    public DataSetEditor.View showBeanAttributesEditorView(final ClickHandler testHandler) {
        workflow.edit(beanDataSetDefAttributesEditor, (BeanDataSetDef) dataSetDef);
        beanAttributesEditionViewPanel.setVisible(true);
        beanDataSetDefAttributesEditor.setEditMode(true);
        showSpecificProviderAttrsEditionView(testHandler);
        return this;
    }

    private boolean isBeanAttributesEditorViewVisible() {
        return beanAttributesEditionViewPanel.isVisible();
    }

    @Override
    public DataSetEditor.View showCSVAttributesEditorView(final ClickHandler testHandler) {
        workflow.edit(csvDataSetDefAttributesEditor, (CSVDataSetDef) dataSetDef);
        csvAttributesEditionViewPanel.setVisible(true);
        csvDataSetDefAttributesEditor.setEditMode(true);
        showSpecificProviderAttrsEditionView(testHandler);
        return this;
    }

    private boolean isCSVAttributesEditorViewVisible() {
        return csvAttributesEditionViewPanel.isVisible();
    }

    @Override
    public DataSetEditor.View showELAttributesEditorView(final ClickHandler testHandler) {
        workflow.edit(elDataSetDefAttributesEditor, (ElasticSearchDataSetDef) dataSetDef);
        elAttributesEditionViewPanel.setVisible(true);
        elDataSetDefAttributesEditor.setEditMode(true);
        showSpecificProviderAttrsEditionView(testHandler);
        return this;
    }

    private boolean isELAttributesEditorViewVisible() {
        return elAttributesEditionViewPanel.isVisible();
    }
    
    private void addTestButtonHandler(final ClickHandler testHandler) {
        if (testHandler != null)
        {
            removetestButtonHandler();
            testButtonHandlerRegistration = testButton.addClickHandler(testHandler);
        }
    }
    
    private void showSpecificProviderAttrsEditionView(final ClickHandler testHandler) 
    {
        showTab(dataConfigurationTab);
        tabViewPanel.setVisible(true);
        addTestButtonHandler(testHandler);
        animation.showA(ANIMATION_DURATION);
    }

    @Override
    public DataSetEditor.View showPreviewTableEditionView(final DisplayerListener tableListener) {
        // Table is not a data set editor component, just a preview data set widget.
        // So not necessary to use the editor workflow this instance.
        
        // View title.
        showTitle();

        // Progress bar.
        progressStep2();
        
        // Configure tabs and visibility.
        previewTableEditor.setVisible(true);
        previewTableEditor.setEditMode(true);
        previewTableEditor.build(tableListener);
        showTab(dataConfigurationTab);
        previewTableEditionViewPanel.setVisible(true);
        showFilterColumnsPreviewEditionView();
        return this;
    }

    private boolean isPreviewTableEditionViewVisible() {
        return previewTableEditionViewPanel.isVisible();
    }

    @Override
    public DataSetEditor.View showColumnsEditorView(final List<DataColumn> columns, final DataSet dataSet, final DataSetColumnsEditor.ColumnsChangedEventHandler columnsChangedEventHandler) {
        // Columns editor is not a data set editor component, just a widget to handle DataColumnEditor instances.
        // So not necessary to use the editor workflow this instance.

        // Data Set Columns editor.
        columnsEditor.setVisible(true);
        columnsEditor.setEditMode(true);
        columnsEditor.build(columns, dataSet, workflow);
        columnsEditor.addColumnsChangeHandler(columnsChangedEventHandler);

        // Panels and tab visibility.
        filterAndColumnsEditionViewPanel.setVisible(true);
        filterAndColumnsTabPanel.setVisible(true);
        
        return this;
    }

    private boolean isColumnsEditorViewVisible() {
        return filterAndColumnsEditionViewPanel.isVisible();
    }

    @Override
    public DataSetEditor.View showFilterEditionView(final DataSet dataSet, final DataSetFilterEditor.Listener filterListener) {
        filterTab.clear();
        
        // Data Set Filter editor.
        final DataSetFilterEditor filterEditor = new DataSetFilterEditor();
        filterEditor.init(dataSet.getMetadata(), dataSetDef.getDataSetFilter(), filterListener);
        filterTab.add(filterEditor);

        // Panels and tab visibility.
        filterAndColumnsEditionViewPanel.setVisible(true);
        filterAndColumnsTabPanel.setVisible(true);
        
        return this;
    }

    private boolean isFilterEditorViewVisible() {
        return filterAndColumnsEditionViewPanel.isVisible();
    }

    private void showFilterColumnsPreviewEditionView()
    {
        activeDataConfigurationTab();
        tabViewPanel.setVisible(true);
        animation.showB(ANIMATION_DURATION);
    }
    
    @Override
    public DataSetEditor.View showAdvancedAttributesEditionView() {
        workflow.edit(dataSetAdvancedAttributesEditor, dataSetDef);

        // View title.
        showTitle();

        // Progress bar.
        progressStep3();

        advancedAttributesEditionViewPanel.setVisible(true);
        dataSetAdvancedAttributesEditor.setEditMode(true);
        showTab(dataConfigurationTab);
        showTab(dataAdvancedConfigurationTab);
        activeDataAdvancedConfigurationTab();
        tabViewPanel.setVisible(true);
        return this;
    }

    private boolean isAdvancedAttributesEditionViewVisible() {
        return advancedAttributesEditionViewPanel.isVisible();
    }

    @Override
    public DataSetEditor.View showNextButton(final String title, final String helpText, final ClickHandler nextHandler) {
        nextButton.setVisible(nextHandler != null);
        if (title != null) {
            nextButton.setText(title);
            nextButton.setTitle(title);
            nextButtonPopover.setHeading(title);
            nextButtonPopover.setText(helpText != null ? helpText : "");
        }
        if (nextHandler != null) {
            removeNextButtonHandler();
            nextButtonHandlerRegistration = nextButton.addClickHandler(nextHandler);
        }
        buttonsPanel.setVisible(true);
        return this;
    }

    @Override
    public DataSetEditor.View showCancelButton(final ClickHandler cancelHandler) {
        cancelButton.setVisible(cancelHandler!= null);
        if (cancelHandler != null) {
            removeCancelButtonHandler();
            cancelButtonHandlerRegistration = cancelButton.addClickHandler(cancelHandler);
        }
        buttonsPanel.setVisible(true);
        return this;
    }

    @Override
    public DataSetEditor.View onSave() {
        
        // Update title if necessary.
        showTitle();
        
        // Check editor errors. If any, mark error in parent Tab.
        resetTabErrors();
        DataSetProviderType type = dataSetDef.getProvider();
        if (type != null) {
            switch (type) {
                case BEAN:
                    if (isBeanAttributesEditorViewVisible() && hasViolations(beanDataSetDefAttributesEditor.getViolations())) tabErrors(dataConfigurationTab);
                    break;
                case CSV:
                    if (isCSVAttributesEditorViewVisible() && hasViolations(csvDataSetDefAttributesEditor.getViolations())) tabErrors(dataConfigurationTab);
                    break;
                case SQL:
                    if (isSQLAttributesEditorViewVisible() && hasViolations(sqlDataSetDefAttributesEditor.getViolations())) tabErrors(dataConfigurationTab);
                    break;
                case ELASTICSEARCH:
                    if (isELAttributesEditorViewVisible()) {
                        // Save attributes not handled by editor framework.
                        elDataSetDefAttributesEditor.save();

                        // Check violations.
                        if (hasViolations(elDataSetDefAttributesEditor.getViolations())) tabErrors(dataConfigurationTab);
                    }
                    break;
            }
        }

        if (hasViolations(previewTableEditor.getViolations())) tabErrors(dataAdvancedConfigurationTab);
        if (hasViolations(dataSetAdvancedAttributesEditor.getViolations())) tabErrors(dataAdvancedConfigurationTab);
        if (hasViolations(columnsEditor.getViolations())) tabErrors(dataAdvancedConfigurationTab);

        return this;
    }
    
    public Set getViolations() {
        final Set violations = new LinkedHashSet<ConstraintViolation<? extends DataSetDef>>();
        if (dataSetProviderTypeEditor.getViolations() != null) violations.addAll((Collection) dataSetProviderTypeEditor.getViolations());
        if (dataSetBasicAttributesEditor.getViolations() != null) violations.addAll((Collection) dataSetBasicAttributesEditor.getViolations());
        if (beanDataSetDefAttributesEditor.getViolations() != null) violations.addAll((Collection) beanDataSetDefAttributesEditor.getViolations());
        if (csvDataSetDefAttributesEditor.getViolations() != null) violations.addAll((Collection) csvDataSetDefAttributesEditor.getViolations());
        if (sqlDataSetDefAttributesEditor.getViolations() != null) violations.addAll((Collection) sqlDataSetDefAttributesEditor.getViolations());
        if (elDataSetDefAttributesEditor.getViolations() != null) violations.addAll((Collection) elDataSetDefAttributesEditor.getViolations());
        if (dataSetAdvancedAttributesEditor.getViolations() != null) violations.addAll((Collection) dataSetAdvancedAttributesEditor.getViolations());
        if (previewTableEditor.getViolations() != null) violations.addAll((Collection) previewTableEditor.getViolations());
        if (columnsEditor.getViolations() != null) violations.addAll((Collection) columnsEditor.getViolations());
        return violations;
    }
    
    private boolean hasViolations(Iterable<ConstraintViolation<?>> violations) {
        return violations != null && violations.iterator().hasNext();
    }

    @Override
    public DataSetEditor.View clear() {
        clearView();
                
        // Clear violations.
        clearEditorViolations();
        
        // Remove current table displayer.
        previewTableEditor.clear();

        this.dataSetDef = null;
        this.workflow = null;
        return this;
    }
    
    private void activeDataConfigurationTab() {
        dataConfigurationTab.setActive(true);
        dataAdvancedConfigurationTab.setActive(false);
    }

    private void activeDataAdvancedConfigurationTab() {
        dataConfigurationTab.setActive(false);
        dataAdvancedConfigurationTab.setActive(true);
    }
    
    private void progressStep1() {
        providerBar.removeStyleName(style.disabledBar());
        columnsFilterBar.addStyleName(style.disabledBar());
        advancedAttrsBar.addStyleName(style.disabledBar());
    }

    private void progressStep2() {
        providerBar.removeStyleName(style.disabledBar());
        columnsFilterBar.removeStyleName(style.disabledBar());
        advancedAttrsBar.addStyleName(style.disabledBar());
    }


    private void progressStep3() {
        providerBar.removeStyleName(style.disabledBar());
        columnsFilterBar.removeStyleName(style.disabledBar());
        advancedAttrsBar.removeStyleName(style.disabledBar());
    }


    private void clearView() {
        titlePanel.setVisible(false);
        title.setVisible(false);
        progressBar.setVisible(false);
        initialViewPanel.setVisible(false);
        providerSelectionViewPanel.setVisible(false);
        tabViewPanel.setVisible(false);
        hideTab(dataConfigurationTab);
        hideTab(dataAdvancedConfigurationTab);
        basicAttributesEditionViewPanel.setVisible(false);
        advancedAttributesEditionViewPanel.setVisible(false);
        sqlAttributesEditionViewPanel.setVisible(false);
        specificProviderAttributesPanel.setVisible(false);
        filterColumnsPreviewTablePanel.setVisible(false);
        csvAttributesEditionViewPanel.setVisible(false);
        beanAttributesEditionViewPanel.setVisible(false);
        elAttributesEditionViewPanel.setVisible(false);
        previewTableEditionViewPanel.setVisible(false);
        // filterAndColumnsEditionViewPanel.setVisible(false);
        nextButton.setVisible(false);
        nextButtonPopover.setHeading("");
        nextButtonPopover.setText("");
        cancelButton.setVisible(false);
        buttonsPanel.setVisible(false);
    }
    
    private void clearEditorViolations() {
        dataSetProviderTypeEditor.setViolations(null);
        dataSetBasicAttributesEditor.setViolations(null);
        beanDataSetDefAttributesEditor.setViolations(null);
        csvDataSetDefAttributesEditor.setViolations(null);
        sqlDataSetDefAttributesEditor.setViolations(null);
        elDataSetDefAttributesEditor.setViolations(null);
        previewTableEditor.setViolations(null);
        dataSetAdvancedAttributesEditor.setViolations(null);
    }

    private void setDataSetDefIntoEditor() {
        dataSetProviderTypeEditor.set(dataSetDef);
        dataSetBasicAttributesEditor.set(dataSetDef);
        beanDataSetDefAttributesEditor.set(dataSetDef);
        csvDataSetDefAttributesEditor.set(dataSetDef);
        sqlDataSetDefAttributesEditor.set(dataSetDef);
        elDataSetDefAttributesEditor.set(dataSetDef);
        previewTableEditor.set(dataSetDef);
        dataSetAdvancedAttributesEditor.set(dataSetDef);
    }
    
    private void showTitle() {

        String _name = null;
        DataSetProviderType _provider = null;
        
        if (dataSetDef != null) {
            
            if (dataSetDef.getName() != null && dataSetDef.getName().length() > 0) {
                _name = dataSetDef.getName();
            }
            
            if (dataSetDef.getProvider() != null) {
                _provider =  dataSetDef.getProvider();
            }
            
            if (_name == null && _provider == null) {
                title.setText(DataSetEditorMessages.INSTANCE.newDataSet(""));
            } else if (_provider != null && _name == null) {
                title.setText(DataSetEditorMessages.INSTANCE.newDataSet(getProviderName(_provider)));
            } else if (_provider == null) {
                title.setText(_name);
            } else {
                title.setText(_name + " (" + getProviderName(_provider) + ")");
            }
            
            title.setVisible(true);
            titlePanel.setVisible(true);
            if (!isEditMode) progressBar.setVisible(true);

        } else {

            title.setVisible(false);
            titlePanel.setVisible(false);
        }
        
    }
    
    private static String getProviderName(DataSetProviderType type) {
        String s = null;
        switch (type) {
            case BEAN:
                s = DataSetEditorConstants.INSTANCE.bean();
                break;
            case CSV:
                s = DataSetEditorConstants.INSTANCE.csv();
                break;
            case SQL:
                s = DataSetEditorConstants.INSTANCE.sql();
                break;
            case ELASTICSEARCH:
                s = DataSetEditorConstants.INSTANCE.elasticSearch();
                break;
        }
        return s;
    }
    
    private void showTab(Tab tab) {
        tab.asWidget().setVisible(true);        
    }

    private void hideTab(Tab tab) {
        tab.asWidget().setVisible(false);
    }
    
    private void removeNextButtonHandler() {
        if (nextButtonHandlerRegistration != null) nextButtonHandlerRegistration.removeHandler();;
    }

    private void removeCancelButtonHandler() {
        if (cancelButtonHandlerRegistration != null) cancelButtonHandlerRegistration.removeHandler();;
    }

    private void removetestButtonHandler() {
        if (testButtonHandlerRegistration != null) testButtonHandlerRegistration.removeHandler();;
    }
    
    private void tabErrors(final Tab tab) {
        if (tab != null) {
            Node first = tab.asWidget().getElement().getFirstChild();
            if (first.getNodeType() == Node.ELEMENT_NODE) {
                Element anchor =(Element)first;
                anchor.getStyle().setColor("red");
            }
        }
    }

    private void tabNoErrors(final Tab tab) {
        if (tab != null) {
            final boolean isActive = tab.isActive();
            Node first = tab.asWidget().getElement().getFirstChild();
            if (first.getNodeType() == Node.ELEMENT_NODE) { 
                Element anchor =(Element)first;
                anchor.getStyle().setColor("#0088CC");
            }
        }
    }
    
    private void resetTabErrors() {
        tabNoErrors(dataConfigurationTab);
        tabNoErrors(dataAdvancedConfigurationTab);
    }

}
