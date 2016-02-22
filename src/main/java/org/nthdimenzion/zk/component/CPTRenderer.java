package org.nthdimenzion.zk.component;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import com.patientadmission.domain.Procedure;

public class CPTRenderer implements ComboitemRenderer<Procedure>{

	@Override
	public void render(Comboitem item, Procedure data, int index)
			throws Exception {
		item.setLabel(data.getCptCode() + "						" + data.getDescription());
	}

}
