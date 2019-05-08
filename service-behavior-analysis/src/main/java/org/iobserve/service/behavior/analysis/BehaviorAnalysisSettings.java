package org.iobserve.service.behavior.analysis;

import java.io.File;

import com.beust.jcommander.Parameter;

public class BehaviorAnalysisSettings {
    @Parameter(names = { "-c", "--configuration" }, required = true, description = "Configuration file")

    private File configurationFile;

    public final File getConfigurationFile() {
        return this.configurationFile;
    }

    public final void setConfigurationFile(final File configurationFile) {
        this.configurationFile = configurationFile;
    }

}
