package org.iobserve.service.behavior.analysis;

import java.io.File;

import com.beust.jcommander.JCommander;

import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import teetime.framework.Configuration;

public class BehaviorAnalysisServiceMain extends AbstractService {

    @Override
    protected Configuration createTeetimeConfiguration() throws ConfigurationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected File getConfigurationFile() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
            final JCommander commander) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void shutdownService() {
        // TODO Auto-generated method stub

    }

}
