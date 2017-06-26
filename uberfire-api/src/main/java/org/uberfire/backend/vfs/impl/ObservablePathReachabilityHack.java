package org.uberfire.backend.vfs.impl;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.uberfire.backend.vfs.ObservablePath;

/**
 * <p>
 * This is a hack so that the {@link ObservablePathImpl} is considered reachable. Since it is almost always looked up dynamically,
 * this injection site ensures that the bean is available for lookup, even when reachability is set to "Aggressive" in Errai.
 */
@EntryPoint
public class ObservablePathReachabilityHack {

    @Inject private Instance<ObservablePath> observablePath;

}
