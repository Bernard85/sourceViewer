package sourceViewer.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sourceViewer.AClause;

public class DeleteIt {

	@Inject MApplication mApplication;
	@Inject IEclipseContext iEclipseContext;
	@Inject EModelService eModelService;
	@Inject EPartService ePartService;


	@Execute public void execute(@Named(IServiceConstants.ACTIVE_PART)MPart active_part
			) throws Throwable {
		Document document = (Document) active_part.getTransientData().get("DOCUMENT");
		Element element = (Element)document.getUserData("CLICKON");
		AClause aClause = (AClause)element.getUserData("AClause");
		
		Element papa=(Element) element.getParentNode();
		
		papa.removeChild(element);
		aClause.setParent(null);
	}
}
