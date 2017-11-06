package org.iobserve.service.cli;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.iobserve.adaptation.IAdaptationEventListener;
import org.iobserve.adaptation.execution.ActionScript;

public class CLIEventListener implements IAdaptationEventListener {

	private final boolean interactiveMode;

	public CLIEventListener(boolean interactiveMode) {
		this.interactiveMode = interactiveMode;
	}

	@Override
	public void notifyUnsupportedActionsFound(List<ActionScript> unsupportedActions) {
		String unsupportedActionsDesc = unsupportedActions.stream().map(script -> script.getDescription())
				.collect(Collectors.joining("\n"));

		if (!this.interactiveMode) {
			throw new IllegalStateException(
					"Could not execute all actions automatically, aborting.\n Not supported actions were:\n"
							+ unsupportedActionsDesc);
		}

		System.out.println("The following actions can not be executed automatically:");
		System.out.println(unsupportedActionsDesc);

		System.out.println(
				"You will be prompted to execute the tasks manually during the process. Do you want to continue?");

		Scanner scanner = new Scanner(System.in);
		if (!scanner.nextBoolean()) {
			scanner.close();
			throw new RuntimeException("User aborted during adaptation execution.");
		}
		scanner.close();
	}

	@Override
	public void notifyExecutionError(ActionScript script, Throwable e) {
		System.out.println("There was an error executing the following script: ");
		System.out.println(script.getDescription());
		System.out.println(e.getMessage());
		e.printStackTrace();
		System.out.println("You can manually execute the script and continue or abort the adaptation process.");
		System.out.println("Do you want to continue?");

		Scanner scanner = new Scanner(System.in);
		if (!scanner.nextBoolean()) {
			// abort
			scanner.close();
			throw new RuntimeException("User aborted during adaptation execution.");
		}
		scanner.close();
	}

}
