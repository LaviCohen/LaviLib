package le.install;

/**
 * The Exception which thrown due an exception during installation.
 * */
public class InstallationException extends Exception{
	private static final long serialVersionUID = 1L;

	public InstallationException(String msg) {
		super(msg);
	}
}
