/**
 *
 */
package org.iobserve.analysis.service;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.iobserve.analysis.model.ModelProviderPlatform;

/**
 * @author Reiner Jung
 *
 */
public class AnalysisDaemon implements Daemon {

    private AnalysisThread thread;
    private boolean running = false;

    @Override
    public void init(final DaemonContext context) throws DaemonInitException, MalformedURLException {
        final String[] args = context.getArguments();
        final CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(createHelpOptions(), args);

            if (commandLine.hasOption("h")) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("iobserve-service", createOptions());
            } else {
                commandLine = parser.parse(createOptions(), args);

                final int listenPort = Integer.parseInt(commandLine.getOptionValue("i"));
                final String outputHostname = commandLine.getOptionValues("o")[0];
                final String outputPort = commandLine.getOptionValues("o")[1];

                final File correspondenceMappingFile = new File(commandLine.getOptionValue("c"));
                final File pcmModelsDirectory = new File(commandLine.getOptionValue("p"));

                final String systemId = commandLine.getOptionValue("s");

                if (correspondenceMappingFile.exists()) {
                    if (correspondenceMappingFile.canRead()) {
                        if (pcmModelsDirectory.exists()) {
                            if (pcmModelsDirectory.isDirectory()) {
                                final ModelProviderPlatform modelProvider = new ModelProviderPlatform(
                                        pcmModelsDirectory.getPath());
                                this.thread = new AnalysisThread(this, listenPort, outputHostname, outputPort, systemId,
                                        modelProvider);
                            } else {
                                throw new DaemonInitException("CLI error: PCM directory " + pcmModelsDirectory.getPath()
                                        + " is not a directory.");
                            }
                        } else {
                            throw new DaemonInitException(
                                    "CLI error: PCM directory " + pcmModelsDirectory.getPath() + " does not exist.");
                        }
                    } else {
                        throw new DaemonInitException("CLI error: Access denied to the correspondence file "
                                + correspondenceMappingFile.getPath());
                    }
                } else {
                    throw new DaemonInitException("CLI error: Correspondence file "
                            + correspondenceMappingFile.getPath() + " does not exist.");
                }

            }
        } catch (final ParseException exp) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("iobserve-analysis", createOptions());
            throw new DaemonInitException("CLI error: " + exp.getMessage());
        }
    }

    @Override
    public void start() throws Exception {
        this.running = true;
        this.thread.start();
    }

    @Override
    public void stop() throws Exception {
        this.running = false;
        try {
            this.thread.join(1000);
        } catch (final InterruptedException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void destroy() {
        this.thread = null;
    }

    public boolean isRunning() {
        return this.running;
    }

    /**
     * Create the command line parameter setup.
     *
     * @return options for the command line parser
     */
    private static Options createOptions() {
        final Options options = new Options();

        options.addOption(Option.builder("i").required(true).longOpt("input").hasArg()
                .desc("port number to listen for new connections of Kieker writers").build());
        options.addOption(
                Option.builder("o").required(true).longOpt("output").hasArgs().numberOfArgs(2).valueSeparator(':')
                        .desc("hostname and port of the iobserve visualization, e.g., visualization:80").build());
        options.addOption(Option.builder("s").required(true).longOpt("system").hasArg().desc("system").build());
        options.addOption(Option.builder("c").required(true).longOpt("correspondence").hasArg()
                .desc("correspondence model").build());
        options.addOption(Option.builder("p").required(true).longOpt("pcm").hasArg()
                .desc("directory containing all PCM models").build());

        /** help */
        options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

        return options;
    }

    /**
     * Create a command line setup with only the help option.
     *
     * @return returns simplified options
     */
    private static Options createHelpOptions() {
        final Options options = new Options();

        /** help */
        options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

        return options;
    }
}
