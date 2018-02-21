package org.iobserve.peropteryx.rcp;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import de.uka.ipd.sdq.dsexplore.launch.DSELaunch;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {


    public static final String INPUT_WORKING_DIR_OPTION = "working-dir";
    public static final String INPUT_WORKING_DIR_OPTION_SHORT = "w";

    public static final String INPUT_MODEL_NAME_OPTION = "model-name";
    public static final String INPUT_MODEL_NAME_OPTION_SHORT = "m";

    public static final String INPUT_PRODUCT_OPTION = "product";

    private IWorkspaceRoot workspaceRoot;
    private IProject project;

    @Override
    public Object start(final IApplicationContext context) throws Exception {
        final String[] args = Platform.getCommandLineArgs();

        final CommandLineParser parser = new BasicParser();
        try {
            final CommandLine commandLine = parser.parse(Application.createOptions(), args);
            final String workingDir = commandLine.getOptionValue(Application.INPUT_WORKING_DIR_OPTION);

            this.launchPeropteryx(workingDir);
        } catch (final ParseException exp) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("peropteryx-headless", Application.createOptions());
            return -1;
        }

        this.project.delete(true, new NullProgressMonitor());

        return IApplication.EXIT_OK;
    }

    private void launchPeropteryx(final String workingDir) throws CoreException {
        final DSELaunch launch = new DSELaunch();

        this.configureInternalWorkspace(workingDir);

        final String projectDir = PerOpteryxLaunchConfigurationBuilder.DEFAULT_PROJECT_NAME + "/"
                + PerOpteryxLaunchConfigurationBuilder.DEFAULT_PROJECT_WORKING_DIR;

        final ILaunchConfiguration launchConfig = PerOpteryxLaunchConfigurationBuilder
                .getDefaultLaunchConfiguration(projectDir, workingDir);
        final ILaunch currentLaunch = new Launch(launchConfig, ILaunchManager.RUN_MODE, null);

        DebugPlugin.getDefault().getLaunchManager().addLaunch(currentLaunch);

        launch.launch(launchConfig, ILaunchManager.RUN_MODE, currentLaunch, new NullProgressMonitor());
    }

    private void configureInternalWorkspace(final String modelDir) throws CoreException {
        this.workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        this.project = this.workspaceRoot.getProject(PerOpteryxLaunchConfigurationBuilder.DEFAULT_PROJECT_NAME);

        if (this.project.exists()) {
            this.project.close(null);
            this.project.delete(true, null);
        }

        this.project.create(null);
        this.project.open(null);

        final IFolder modelFolder = this.project
                .getFolder(PerOpteryxLaunchConfigurationBuilder.DEFAULT_PROJECT_WORKING_DIR);

        modelFolder.createLink(new Path(modelDir), IResource.BACKGROUND_REFRESH | IResource.REPLACE,
                new NullProgressMonitor());
    }

    @Override
    public void stop() {
        try {
            ResourcesPlugin.getWorkspace().save(true, null);
        } catch (final CoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the command line parameter setup.
     *
     * @return options for the command line parser
     */
    private static Options createOptions() {
        final Options options = new Options();

        final Option workDirOption = new Option(Application.INPUT_WORKING_DIR_OPTION_SHORT,
                Application.INPUT_WORKING_DIR_OPTION, true,
                "Working directory containing the model files. Note that the files may be changed in the process.");
        workDirOption.setRequired(true);

        final Option productOption = new Option(Application.INPUT_PRODUCT_OPTION, true,
                "Eclipse product description");

        final Option helpOption = new Option("h", "help", false, "Show usage information");

        options.addOption(workDirOption);
        options.addOption(productOption);

        /** help */
        options.addOption(helpOption);

        return options;
    }
}
