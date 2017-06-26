/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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
package org.uberfire.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import org.jboss.errai.bus.client.api.ClientMessageBus;
import org.jboss.errai.ioc.client.QualifierUtil;
import org.jboss.errai.ioc.client.api.BeanDefProvider;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.uberfire.client.mvp.ActivityManager;
import org.uberfire.client.mvp.PerspectiveActivity;
import org.uberfire.client.mvp.PerspectiveManager;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.WorkbenchScreenActivity;
import org.uberfire.client.screen.JSWorkbenchScreenActivity;
import org.uberfire.client.workbench.events.ApplicationReadyEvent;
import org.uberfire.client.workbench.widgets.menu.WorkbenchMenuBar;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.PanelDefinition;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;

import static org.uberfire.workbench.model.menu.MenuFactory.newTopLevelMenu;

/**
 * GWT's Entry-point for Uberfire-showcase
 */
@EntryPoint
public class ShowcaseEntryPoint {

    @Inject
    private WorkbenchMenuBar menubar;

    @Inject
    private PlaceManager placeManager;

    @Inject
    private ActivityManager activityManager;

    @Inject
    private PerspectiveManager perspectiveManager;

    @Inject
    private ClientMessageBus bus;

    @Inject
    @Any
    private ManagedInstance<PerspectiveActivity> perspectives;

    @Inject
    @Any
    private BeanDefProvider<WorkbenchScreenActivity> screens;

    @Inject
    private Event<DumpLayout> dumpLayoutEvent;

    public static native void redirect(String url)/*-{
        $wnd.location = url;
    }-*/;

    ;

    @PostConstruct
    public void startApp() {
        hideLoadingPopup();
    }

    private void setupMenu(@Observes final ApplicationReadyEvent event) {
        final PerspectiveActivity defaultPerspective = getDefaultPerspectiveActivity();

        final Menus menus =
                newTopLevelMenu("Home")
                        .respondsWith(new Command() {
                            @Override
                            public void execute() {
                                if (defaultPerspective != null) {
                                    placeManager.goTo(new DefaultPlaceRequest(defaultPerspective.getIdentifier()));
                                } else {
                                    Window.alert("Default perspective not found.");
                                }
                            }
                        })
                        .endMenu()
                        .newTopLevelMenu("Perspectives")
                        .withItems(getPerspectives())
                        .endMenu()
                        .newTopLevelMenu("Screens")
                        .withItems(getScreens())
                        .endMenu()
                        .newTopLevelMenu("Dump Layout").respondsWith(new Command() {
                    @Override
                    public void execute() {
                        dumpLayoutEvent.fire(new DumpLayout());
                    }
                }).endMenu()
                        .build();

        menubar.addMenus(menus);
    }

    private List<MenuItem> getScreens() {
        final List<MenuItem> screens = new ArrayList<>();
        final List<String> names = new ArrayList<>();

        for (final IOCBeanDef<WorkbenchScreenActivity> _menuItem : this.screens) {
            final String name;
            if (_menuItem.getBeanClass().equals(JSWorkbenchScreenActivity.class)) {
                name = _menuItem.getName();
            } else {
                Class<? extends WorkbenchScreenActivity> beanClass = (Class) _menuItem.getBeanClass();
                name = this.screens.select(beanClass, QualifierUtil.DEFAULT_ANNOTATION).get().getName();
            }
            names.add(name);
        }

        Collections.sort(names);

        for (final String name : names) {
            final MenuItem item = MenuFactory.newSimpleItem(name)
                    .respondsWith(new Command() {
                        @Override
                        public void execute() {
                            List<PanelDefinition> panelsUnderRoot = perspectiveManager.getLivePerspectiveDefinition().getRoot().getChildren();
                            if (panelsUnderRoot.isEmpty()) {
                                Window.alert("Sorry, can't find anywhere to launch the requested screen");
                            } else {
                                placeManager.goTo(new DefaultPlaceRequest(name),
                                                  panelsUnderRoot.get(0));
                            }
                        }
                    }).endMenu().build().getItems().get(0);
            screens.add(item);
        }

        return screens;
    }

    private List<MenuItem> getPerspectives() {
        final List<MenuItem> perspectives = new ArrayList<>();
        for (final PerspectiveActivity perspective : getPerspectiveActivities()) {
            final String name = perspective.getDefaultPerspectiveLayout().getName();
            final Command cmd = new Command() {

                @Override
                public void execute() {
                    placeManager.goTo(new DefaultPlaceRequest(perspective.getIdentifier()));
                }
            };
            final MenuItem item = MenuFactory.newSimpleItem(name).respondsWith(cmd).endMenu().build().getItems().get(0);
            perspectives.add(item);
        }

        return perspectives;
    }

    private PerspectiveActivity getDefaultPerspectiveActivity() {
        PerspectiveActivity defaultPerspective = null;
        final Iterator<PerspectiveActivity> perspectivesIterator = perspectives.iterator();

        while (perspectivesIterator.hasNext()) {
            final PerspectiveActivity perspective = perspectivesIterator.next();
            final PerspectiveActivity instance = perspective;
            if (instance.isDefault()) {
                defaultPerspective = instance;
                break;
            } else {
                // Destroys bean
                perspectivesIterator.remove();
            }
        }
        return defaultPerspective;
    }

    private List<PerspectiveActivity> getPerspectiveActivities() {

        //Get Perspective Providers
        final Set<PerspectiveActivity> activities = activityManager.getActivities(PerspectiveActivity.class);

        //Sort Perspective Providers so they're always in the same sequence!
        List<PerspectiveActivity> sortedActivities = new ArrayList<>(activities);
        Collections.sort(sortedActivities,
                         new Comparator<PerspectiveActivity>() {

                             @Override
                             public int compare(PerspectiveActivity o1,
                                                PerspectiveActivity o2) {
                                 return o1.getDefaultPerspectiveLayout().getName().compareTo(o2.getDefaultPerspectiveLayout().getName());
                             }
                         });

        return sortedActivities;
    }

    //Fade out the "Loading application" pop-up
    private void hideLoadingPopup() {
        final Element e = RootPanel.get("loading").getElement();

        new Animation() {

            @Override
            protected void onUpdate(double progress) {
                e.getStyle().setOpacity(1.0 - progress);
            }

            @Override
            protected void onComplete() {
                e.getStyle().setVisibility(Style.Visibility.HIDDEN);
            }
        }.run(500);
    }

    public static class DumpLayout {

    }
}