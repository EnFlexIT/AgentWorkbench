package gasmas.resourceallocation;

import java.io.Serializable;

public class StatusData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 827774764094560195L;

	int phase = 0;

	public StatusData(int step) {
		phase = step;
	}

}
