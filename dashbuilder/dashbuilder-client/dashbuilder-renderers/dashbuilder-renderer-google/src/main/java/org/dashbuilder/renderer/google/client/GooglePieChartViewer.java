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
package org.dashbuilder.renderer.google.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.corechart.PieChart;
import com.googlecode.gwt.charts.client.corechart.PieChartOptions;
import org.dashbuilder.displayer.PieChartDisplayerSettings;

public class GooglePieChartViewer extends GoogleXAxisChartViewer<PieChartDisplayerSettings> {

    private PieChart chart;
    protected Panel filterPanel;

    @Override
    public ChartPackage getPackage() {
        return ChartPackage.CORECHART;
    }

    @Override
    public Widget createVisualization() {
        chart = new PieChart();
        chart.addSelectHandler(createSelectHandler(chart));
        chart.draw(createTable(), createOptions());

        HTML titleHtml = new HTML();
        if (displayerSettings.isTitleVisible()) {
            titleHtml.setText(displayerSettings.getTitle());
        }

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(titleHtml);
        verticalPanel.add(filterPanel = new SimplePanel());
        verticalPanel.add(chart);
        return verticalPanel;
    }

    protected void updateVisualization() {
        filterPanel.clear();
        Widget filterReset = createCurrentSelectionWidget();
        if (filterReset != null) filterPanel.add(filterReset);

        chart.draw(createTable(), createOptions());
    }

    private PieChartOptions createOptions() {
        PieChartOptions options = PieChartOptions.create();
        options.setWidth(displayerSettings.getWidth());
        options.setHeight(displayerSettings.getHeight());
        options.setIs3D(true);
        options.setLegend( createChartLegend( displayerSettings ) );
        options.setColors(createColorArray(googleTable));
        options.setChartArea(createChartArea());
        return options;
    }
}
