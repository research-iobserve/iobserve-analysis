package org.iobserve.newplanning;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.spi.IRegistryProvider;

/**
 *
 * @author LarsBlumke
 *
 */
public class MyRegistryProvider implements IRegistryProvider {

    private final IExtensionRegistry reg;

    public MyRegistryProvider() {
        this.reg = RegistryFactory.createRegistry(null, null, null);
    }

    @Override
    public IExtensionRegistry getRegistry() {
        return this.reg;
    }

}
