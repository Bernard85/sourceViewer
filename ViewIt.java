package sourceEditor.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.w3c.dom.Element;

public class ViewIt {

	@Inject MApplication mApplication;
	@Inject IEclipseContext iEclipseContext;
	@Inject EModelService eModelService;
	@Inject EPartService ePartService;
	
	@Execute public void execute(@Named(IServiceConstants.ACTIVE_PART)MPart mPart) throws Throwable {
		String ID = mPart.getElementId();
		
		MWindow window2 = (MWindow) eModelService.find("window2", mApplication);
		window2.setVisible(true);
		MPartStack partstack2 = (MPartStack) eModelService.find("window2.partstack", window2);

		MPart part2 = (MPart) eModelService.find(ID, partstack2);
		if (part2==null) {
			part2 = ePartService.createPart("SourceViewer");
			part2.setToBeRendered(true);
			part2.setVisible(true);
			part2.setElementId(ID);
			part2.setLabel(ID);
			Element element = (Element) mPart.getTransientData().get("ELEMENT");
			part2.getTransientData().put("ELEMENT", element);
			partstack2.getChildren().add(part2);
		}
		partstack2.setSelectedElement(part2);
	}
}
