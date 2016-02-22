package org.nthdimenzion.zk.component;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import com.patientadmission.domain.IcdElement;

public class ICDRenderer implements ComboitemRenderer<IcdElement> {

	@Override
	public void render(Comboitem item, IcdElement data, int index) throws Exception {
		item.setLabel(data.getIcdCode() + "						" + data.getDescription());
	}
}
