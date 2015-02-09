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
package org.dashbuilder.client.metrics.widgets.details;

import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.dashbuilder.backend.ClusterMetricsDataSetGenerator;
import org.dashbuilder.client.metrics.MetricsDashboard;
import org.dashbuilder.client.metrics.MetricsDashboardClientBundle;
import org.dashbuilder.displayer.DisplayerSettingsFactory;
import org.dashbuilder.displayer.client.Displayer;
import org.dashbuilder.displayer.client.DisplayerCoordinator;
import org.dashbuilder.displayer.client.DisplayerHelper;

import static org.dashbuilder.dataset.filter.FilterFactory.equalsTo;
import static org.dashbuilder.dataset.filter.FilterFactory.timeFrame;
import static org.dashbuilder.dataset.group.AggregateFunctionType.MAX;
import static org.dashbuilder.dataset.group.DateIntervalType.MINUTE;
import static org.dashbuilder.dataset.group.DateIntervalType.SECOND;

public class DetailedServerMetrics extends Composite {

    interface DetailedServerMetricsBinder extends UiBinder<Widget, DetailedServerMetrics>{}
    private static final DetailedServerMetricsBinder uiBinder = GWT.create(DetailedServerMetricsBinder.class);

    @UiField(provided = true)
    Displayer serverCPU0;

    @UiField(provided = true)
    Displayer serverCPU1;

    @UiField(provided = true)
    Displayer serverMemory;

    @UiField(provided = true)
    Displayer serverNetwork;

    @UiField(provided = true)
    Displayer serverDisk;

    @UiField(provided = true)
    Displayer serverProcessesRunning;

    @UiField(provided = true)
    Displayer serverTable;

    @UiField(provided = true)
    Displayer serverProcessesSleeping;

    @UiField
    Image modeIcon;

    @UiField
    Tooltip modeIconTooltip;

    @UiField
    HorizontalPanel chartsArea;

    @UiField
    VerticalPanel tableArea;

    DisplayerCoordinator displayerCoordinator = new DisplayerCoordinator();

    private boolean isChartMode;
    private boolean isTableMode;
    
    public String getTitle() {
        return "Server metrics (Vertical)";
    }

    public DetailedServerMetrics(MetricsDashboard metricsDashboard, String server) {

        buildServerDetailsDisplayers(metricsDashboard, server);
        
        // Init the dashboard from the UI Binder template
        initWidget(uiBinder.createAndBindUi(this));
        
        modeIcon.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (isChartMode) enableTableMode();
                else enableChartMode();
            }
        });
        
        // By default use charts mode.
        enableChartMode();
        
        // Draw the charts
        displayerCoordinator.drawAll();
    }

    protected void buildServerDetailsDisplayers(MetricsDashboard metricsDashboard, String server) {
        serverCPU0 = DisplayerHelper.lookupDisplayer(
                DisplayerSettingsFactory.newMeterChartSettings()
                        .dataset(metricsDashboard.getDataSetUUID())
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_SERVER, equalsTo(server))
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP, timeFrame("1second"))
                        .column(ClusterMetricsDataSetGenerator.COLUMN_CPU0, MAX, "CPU0")
                        .title("CPU0")
                        .width(400).height(250)
                        .meter(0, 25, 50, 100)
                        .refreshOn(1, false)
                        .buildSettings());

        serverCPU1 = DisplayerHelper.lookupDisplayer(
                DisplayerSettingsFactory.newMeterChartSettings()
                        .dataset(metricsDashboard.getDataSetUUID())
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_SERVER, equalsTo(server))
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP, timeFrame("1second"))
                        .column(ClusterMetricsDataSetGenerator.COLUMN_CPU1, MAX, "CPU1")
                        .title("CPU1")
                        .width(400).height(250)
                        .meter(0, 25, 50, 100)
                        .refreshOn(1, false)
                        .buildSettings());

        serverMemory = DisplayerHelper.lookupDisplayer(
                DisplayerSettingsFactory.newAreaChartSettings()
                        .dataset(metricsDashboard.getDataSetUUID())
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_SERVER, equalsTo(server))
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP, timeFrame("60second"))
                        .group(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP).fixed(SECOND, true)
                        .column(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP)
                        .column(ClusterMetricsDataSetGenerator.COLUMN_MEMORY_USED, MAX, "Used memory")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_MEMORY_FREE, MAX, "Free memory")
                        .title("Memory consumption")
                        .titleVisible(false)
                        .width(800).height(250)
                        .refreshOn(2, false)
                        .buildSettings());

        serverNetwork = DisplayerHelper.lookupDisplayer(
                DisplayerSettingsFactory.newAreaChartSettings()
                        .dataset(metricsDashboard.getDataSetUUID())
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_SERVER, equalsTo(server))
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP, timeFrame("10second"))
                        .group(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP).dynamic(10, SECOND, true)
                        .column(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP)
                        .column(ClusterMetricsDataSetGenerator.COLUMN_NETWORK_RX, MAX, "Downstream")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_NETWORK_TX, MAX, "Upstream")
                        .title("Network bandwidth")
                        .titleVisible(false)
                        .width(500).height(250)
                        .refreshOn(2, false)
                        .buildSettings());

        serverDisk = DisplayerHelper.lookupDisplayer(
                DisplayerSettingsFactory.newPieChartSettings()
                        .dataset(metricsDashboard.getDataSetUUID())
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_SERVER, equalsTo(server))
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP, timeFrame("10second"))
                        .group(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP).dynamic(1, SECOND, true)
                        .column(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP)
                        .column(ClusterMetricsDataSetGenerator.COLUMN_DISK_FREE, MAX, "Free disk space")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_DISK_USED, MAX, "Used disk space")
                        .title("Disk usage")
                        .titleVisible(false)
                        .legendOff()
                        .width(200).height(200)
                        .refreshOn(2, false)
                        .buildSettings());

        serverProcessesRunning = DisplayerHelper.lookupDisplayer(
                DisplayerSettingsFactory.newTableSettings()
                        .dataset(metricsDashboard.getDataSetUUID())
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_SERVER, equalsTo(server))
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP, timeFrame("1second"))
                        .column(ClusterMetricsDataSetGenerator.COLUMN_PROCESSES_RUNNING, "Running processes")
                        .title("Running processes")
                        .titleVisible(false)
                        .tableWidth(100)
                        .refreshOn(1, false)
                        .buildSettings());

        serverProcessesSleeping = DisplayerHelper.lookupDisplayer(
                DisplayerSettingsFactory.newTableSettings()
                        .dataset(metricsDashboard.getDataSetUUID())
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_SERVER, equalsTo(server))
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP, timeFrame("1second"))
                        .column(ClusterMetricsDataSetGenerator.COLUMN_PROCESSES_SLEEPING, "Sleeping processes")
                        .title("Sleeping processes")
                        .titleVisible(false)
                        .tableWidth(100)
                        .refreshOn(1, false)
                        .buildSettings());

        serverTable = DisplayerHelper.lookupDisplayer(
                DisplayerSettingsFactory.newTableSettings()
                        .dataset(metricsDashboard.getDataSetUUID())
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_SERVER, equalsTo(server))
                        .filter(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP, timeFrame("1minute"))
                        .group(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP).fixed(MINUTE, true)
                        .column(ClusterMetricsDataSetGenerator.COLUMN_TIMESTAMP, "Timestamp")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_CPU0, "CPU0")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_CPU1, "CPU1")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_MEMORY_USED, "Used memory (Gb)")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_MEMORY_FREE, "Free memory (Gb)")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_NETWORK_TX, "Upstream (kbps)")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_NETWORK_RX, "Downstream (kbps)")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_PROCESSES_RUNNING, "Running processes")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_PROCESSES_SLEEPING, "Sleeping processes")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_DISK_USED, "Used disk space (Mb)")
                        .column(ClusterMetricsDataSetGenerator.COLUMN_DISK_FREE, "Free disk space (Mb)")
                        .title("Real-time " + server + " metrics")
                        .titleVisible(false)
                        .tableWidth(1200)
                        .refreshOn(60, false)
                        .buildSettings());

        displayerCoordinator.addDisplayer(serverCPU0);
        displayerCoordinator.addDisplayer(serverCPU1);
        displayerCoordinator.addDisplayer(serverMemory);
        displayerCoordinator.addDisplayer(serverNetwork);
        displayerCoordinator.addDisplayer(serverDisk);
        displayerCoordinator.addDisplayer(serverProcessesRunning);
        displayerCoordinator.addDisplayer(serverProcessesSleeping);
        displayerCoordinator.addDisplayer(serverTable);
        serverCPU0.refreshOn();
        serverCPU1.refreshOn();
        serverMemory.refreshOn();
        serverNetwork.refreshOn();
        serverDisk.refreshOn();
        serverProcessesRunning.refreshOn();
        serverProcessesSleeping.refreshOn();
        serverTable.refreshOn();
    }
    
    private void enableChartMode() {
        isChartMode = true;
        isTableMode = false;
        // TODO: Animate.
        chartsArea.setVisible(true);
        tableArea.setVisible(false);
        modeIcon.setResource(MetricsDashboardClientBundle.INSTANCE.tableIcon());
        modeIconTooltip.setText("Use table perspective");
    }

    private void enableTableMode() {
        isChartMode = false;
        isTableMode = true;
        // TODO: Animate.
        chartsArea.setVisible(false);
        tableArea.setVisible(true);
        modeIcon.setResource(MetricsDashboardClientBundle.INSTANCE.chartIcon());
        modeIconTooltip.setText("Use charting perspective");
    }
    
}