
public interface ProgressMonitorView {
	
	/**
	 * Updates the ProgressMonitor.
	 * 
	 * @param progressPercentage The percentage to be displayed on the ProgressMonitor.
	 * @return Returns false if the task has been cancelled
	 */
	public boolean updateProgress(int progressPercentage);

}
