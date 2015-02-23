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
package org.dashbuilder.renderer.lienzo.client;

import com.ait.lienzo.charts.client.AbstractChart;
import com.ait.lienzo.charts.client.axis.CategoryAxis;
import com.ait.lienzo.charts.client.axis.NumericAxis;
import com.ait.lienzo.charts.client.event.DataReloadedEvent;
import com.ait.lienzo.charts.client.event.DataReloadedEventHandler;
import com.ait.lienzo.charts.client.xy.XYChartData;
import com.ait.lienzo.charts.client.xy.XYChartSerie;
import com.ait.lienzo.charts.client.xy.bar.BarChart;
import com.ait.lienzo.charts.shared.core.types.ChartOrientation;
import com.ait.lienzo.charts.shared.core.types.LabelsPosition;
import com.ait.lienzo.charts.shared.core.types.LegendPosition;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.IColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import org.dashbuilder.dataset.ColumnType;
import org.dashbuilder.dataset.DataColumn;
import org.dashbuilder.dataset.DataSetLookupConstraints;
import org.dashbuilder.displayer.DisplayerAttributeDef;
import org.dashbuilder.displayer.DisplayerAttributeGroupDef;
import org.dashbuilder.displayer.DisplayerConstraints;

public class LienzoBarChartDisplayer extends LienzoDisplayer {

    private static final ColorName[] DEFAULT_SERIE_COLORS = new ColorName[] {
            ColorName.DEEPSKYBLUE, ColorName.RED, ColorName.YELLOWGREEN            
    };
    
    final protected BarChart[] chart = new BarChart[1];

    @Override
    public AbstractChart createVisualization() {

        // Create the data for the chart instance.
        XYChartData chartData = createChartData();

        // Create the BarChart instance.
        chart[0] = new BarChart(new DataReloadedEventHandler() {
            @Override
            public void onDataReloaded(DataReloadedEvent event) {
                GWT.log("LienzoBarChartDisplayer - onDataReloaded");
                configureBarChart(chart[0]);
            }
        });
        chart[0].setData(chartData);

        return chart[0];
    }
    
    private void configureBarChart(BarChart chart) {
        if (displayerSettings.isBarchartHorizontal()) chart.setOrientation(ChartOrientation.HORIZNONAL);
        else chart.setOrientation(ChartOrientation.VERTICAL);

        chart.setX(0);
        chart.setY(0);
        chart.setName(displayerSettings.getTitle());
        chart.setWidth(getChartWidth());
        chart.setHeight(getChartHeight());
        chart.setMarginLeft(displayerSettings.getChartMarginLeft());
        chart.setMarginRight(displayerSettings.getChartMarginRight());
        chart.setMarginTop(displayerSettings.getChartMarginTop());
        chart.setMarginBotom(displayerSettings.getChartMarginBottom());
        chart.setFontFamily("Verdana");
        chart.setFontStyle("bold");
        chart.setFontSize(8);
        chart.setShowTitle(displayerSettings.isTitleVisible());
        chart.setShowCategoriesAxisTitle(false);
        chart.setShowValuesAxisTitle(false);
        //chart.setLegendPosition(LegendPosition.RIGHT); // TODO: Custom displayer parameter.
        //chart.setCategoriesAxisLabelsPosition(LabelsPosition.LEFT); // TODO: Custom displayer parameter.
        //chart.setValuesAxisLabelsPosition(LabelsPosition.BOTTOM); // TODO: Custom displayer parameter.
        chart.setResizable(false); // TODO: Custom displayer parameter.
        chart.setAnimated(true); // TODO: Custom displayer parameter.

        // Events (filtering)
        if (displayerSettings.isFilterEnabled()) {
            chart.addSelectHandler(new SelectHandler());
        }

        // TODO: Category and Number types?
        CategoryAxis categoryAxis = new CategoryAxis(displayerSettings.getXAxisTitle());
        NumericAxis numericAxis = new NumericAxis(displayerSettings.getYAxisTitle());

        chart.setCategoriesAxis(categoryAxis);
        chart.setValuesAxis(numericAxis);
        chart.build();
    }

    @Override
    protected void updateVisualization() {
        filterPanel.clear();
        Widget filterReset = super.createCurrentSelectionWidget();
        if (filterReset != null) filterPanel.add(filterReset);

        if (dataSet.getRowCount() == 0) {
            mainPanel.add(super.createNoDataMsgPanel());
            chart[0] = null;
        } else {
            CategoryAxis categoryAxis = new CategoryAxis(displayerSettings.getXAxisTitle());
            chart[0].setCategoriesAxis(categoryAxis);
            XYChartData newData = createChartData();
            newData.setCategoryAxisProperty(categoriesColumn.getId());
            chart[0].setData(newData);
        }
    }

    protected XYChartData createChartData() {

        // Create data instance and the series to display.
        XYChartData chartData = new XYChartData(lienzoTable);
        DataColumn categoriesColumn = getCategoriesColumn();
        DataColumn[] valuesColumns = getValuesColumns();
        
        if (categoriesColumn != null) {
            chartData.setCategoryAxisProperty(categoriesColumn.getId());
            if (valuesColumns != null) {
                for (int i = 0; i < valuesColumns.length; i++) {
                    DataColumn dataColumn = valuesColumns[i];
                    String columnId = dataColumn.getId();
                    String columnName = dataColumn.getName();
                    if (columnName == null) columnName = columnId;

                    XYChartSerie series = new XYChartSerie(columnName, getSeriesColor(i), columnId);
                    chartData.addSerie(series);
                }
            } else {
                GWT.log("No values columns specified.");
            }
        } else {
            GWT.log("No categories column specified.");
        }
        return chartData;
    }

    @Override
    public DisplayerConstraints createDisplayerConstraints() {

        DataSetLookupConstraints lookupConstraints = new DataSetLookupConstraints()
                .setGroupRequired(true)
                .setGroupColumn(true)
                .setMaxColumns(10)
                .setMinColumns(2)
                .setExtraColumnsAllowed(true)
                .setGroupsTitle("Categories")
                .setColumnsTitle("Series")
                .setColumnTypes(new ColumnType[]{
                        ColumnType.LABEL,
                        ColumnType.NUMBER});

        return new DisplayerConstraints(lookupConstraints)
                   .supportsAttribute( DisplayerAttributeDef.TYPE )
                   .supportsAttribute(DisplayerAttributeDef.COLUMNS)
                   .supportsAttribute( DisplayerAttributeGroupDef.FILTER_GROUP )
                   .supportsAttribute( DisplayerAttributeGroupDef.REFRESH_GROUP)
                   .supportsAttribute( DisplayerAttributeGroupDef.TITLE_GROUP)
                   .supportsAttribute( DisplayerAttributeDef.CHART_WIDTH )
                   .supportsAttribute( DisplayerAttributeDef.CHART_HEIGHT )
                    .supportsAttribute(DisplayerAttributeDef.CHART_BGCOLOR)
                    .supportsAttribute(DisplayerAttributeGroupDef.CHART_MARGIN_GROUP)
                   .supportsAttribute( DisplayerAttributeGroupDef.CHART_LEGEND_GROUP )
                   .supportsAttribute( DisplayerAttributeGroupDef.AXIS_GROUP )
                   .supportsAttribute( DisplayerAttributeGroupDef.BARCHART_GROUP );
    }
    
    private IColor getSeriesColor(int index) {
        int defaultColorsSize = DEFAULT_SERIE_COLORS.length;
        if (index >= defaultColorsSize) return ColorName.getValues().get(90 + index*2);
        return DEFAULT_SERIE_COLORS[index];
    }
}
