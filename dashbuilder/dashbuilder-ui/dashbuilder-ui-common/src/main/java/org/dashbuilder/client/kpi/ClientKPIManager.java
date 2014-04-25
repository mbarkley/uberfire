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
package org.dashbuilder.client.kpi;

import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.dashbuilder.client.dataset.ClientDataSetManager;
import org.dashbuilder.model.dataset.DataSet;
import org.dashbuilder.model.dataset.DataSetLookup;
import org.dashbuilder.model.dataset.DataSetRef;
import org.dashbuilder.model.displayer.DataDisplayer;
import org.dashbuilder.model.kpi.*;
import org.dashbuilder.model.kpi.impl.KPIImpl;

@ApplicationScoped
public class ClientKPIManager {

    private List<KPI> kpiList = new ArrayList<KPI>();

    public List<KPI> getAllKPIs() {
        return kpiList;
    }

    public KPI createKPI(DataSetRef dataSetRef, DataDisplayer dataDisplayer) {
        KPIImpl kpi = new KPIImpl();
        kpi.setDataSetRef(dataSetRef);
        kpi.setDataDisplayer(dataDisplayer);
        return kpi;
    }

    public KPI createKPI(String uuid, DataSetRef dataSetRef, DataDisplayer dataDisplayer) {
        KPIImpl kpi = new KPIImpl(uuid);
        kpi.setDataSetRef(dataSetRef);
        kpi.setDataDisplayer(dataDisplayer);
        return kpi;
    }

    public KPI addKPI(KPI kpi) {
        kpiList.add(kpi);
        return kpi;
    }

    public KPI getKPI(String uid) {
        for (KPI kpi : kpiList) {
            if (kpi.getUUID().equals(uid)){
                return kpi;
            }
        }
        return null;
    }
}
