package org.uberfire.ext.properties.editor.client;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.uberfire.ext.properties.editor.client.fields.AbstractField;

/**
 * <p>
 * This entrypoint exists to make {@link AbstractField} subtypes reachable.
 *
 * <p>
 * This should be removed when all types that programmatically look this up are turned into proper Errai beans that can
 * inject this instance on their own.
 */
@EntryPoint
public class AbstractFieldInstanceProvider {

    @Inject
    @Any
    private Instance<AbstractField> fieldProvider;

    public Instance<AbstractField> provider() {
        return fieldProvider;
    }

}
