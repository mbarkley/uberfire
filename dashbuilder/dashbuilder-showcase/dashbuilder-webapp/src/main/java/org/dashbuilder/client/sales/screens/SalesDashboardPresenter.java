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
package org.dashbuilder.client.sales.screens;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.dashbuilder.displayer.client.DisplayerCoordinator;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;

@WorkbenchScreen(identifier = "SalesDashboardScreen")
@ApplicationScoped
public class SalesDashboardPresenter {

    public interface SalesDashboardView extends UberView<SalesDashboardPresenter> {

    }

    @Inject
    SalesDashboardView view;

    DisplayerCoordinator displayerCoordinator = new DisplayerCoordinator();

    @PostConstruct
    protected void init() {

    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Sales Dashboard";
    }

    @WorkbenchPartView
    public UberView<SalesDashboardPresenter> getView() {
        return view;
    }
}
