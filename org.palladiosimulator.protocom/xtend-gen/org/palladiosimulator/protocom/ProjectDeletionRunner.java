package org.palladiosimulator.protocom;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.protocom.ProjectDeletionChoice;

/**
 * Helper class that allows a message box to appear from a non user
 * interface thread because the workbench shell is otherwise not accessible.
 * 
 * @author Philipp Meier
 */
@SuppressWarnings("all")
public class ProjectDeletionRunner implements Runnable {
  private ProjectDeletionChoice projectDeletionChoice = ProjectDeletionChoice.NONSELECTED;
  
  private String myProjectId;
  
  private int usersChoice;
  
  public ProjectDeletionRunner(final String myProjectId) {
    super();
    this.myProjectId = myProjectId;
  }
  
  public ProjectDeletionChoice shouldDelete() {
    return this.projectDeletionChoice;
  }
  
  @Override
  public void run() {
    final String[] options = { "Delete Project and Regenerate all Files", "Abort" };
    IWorkbench _workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
    Shell _shell = _activeWorkbenchWindow.getShell();
    final MessageDialog dlg = new MessageDialog(_shell, 
      "Temporary analysis project folder already exists", null, 
      (("The project used for the analysis already exists. Should " + this.myProjectId) + " and all of its contents be deleted?"), 
      MessageDialog.QUESTION, options, 1);
    int _open = dlg.open();
    this.usersChoice = _open;
    if ((this.usersChoice == 0)) {
      this.projectDeletionChoice = ProjectDeletionChoice.DELETION;
    } else {
      if ((this.usersChoice == 1)) {
        this.projectDeletionChoice = ProjectDeletionChoice.ABORT;
      }
    }
  }
}
