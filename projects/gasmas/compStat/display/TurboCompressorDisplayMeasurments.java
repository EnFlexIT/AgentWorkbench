package gasmas.compStat.display;

import gasmas.compStat.CompressorStationModel;
import gasmas.ontology.TurboCompressor;

public class TurboCompressorDisplayMeasurments extends ParameterDisplay {

	private static final long serialVersionUID = 2985435239542481036L;

	private CompressorStationModel compressorStationModel = null;
	private String turboCompressorID = null;
	private TurboCompressor myTurboCompressor = null;  //  @jve:decl-index=0:

	
	public TurboCompressorDisplayMeasurments(CompressorStationModel compressorStationModel, String turboCompressorID) {
		this.compressorStationModel = compressorStationModel;	
		this.turboCompressorID = turboCompressorID;
		this.initialize();
	}

	private void initialize() {
		
	}
}
