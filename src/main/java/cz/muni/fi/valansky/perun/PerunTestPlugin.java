package cz.muni.fi.valansky.perun;

import cz.metacentrum.perun.core.api.PerunPlugin;
import cz.metacentrum.perun.core.blImpl.PerunBlImpl;
import cz.metacentrum.perun.core.implApi.modules.attributes.AttributesModuleImplApi;
import org.pf4j.Extension;

import java.util.ServiceLoader;

/**
 * @author Daniel Valanský
 */

@Extension
public class PerunTestPlugin implements PerunPlugin {

    @Override
    public void start(PerunBlImpl perun) {
        ServiceLoader<AttributesModuleImplApi> attributeModulesLoader = ServiceLoader.load(AttributesModuleImplApi.class);
        perun.getAttributesManagerImpl().initAttributeModules(attributeModulesLoader);
    }

    @Override
    public void stop() {

    }
}
