package sourceViewer.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sourceViewer.AClause;

public class Insert {
	@Execute public void execute(@Named(IServiceConstants.ACTIVE_PART)MPart active_part
								,@Named("WHAT")String what
								,@Named("WHERE")String where)
	{
		Document document = (Document) active_part.getTransientData().get("DOCUMENT");
		Element element = (Element)document.getUserData("CLICKON");
		AClause aClause = (AClause)element.getUserData("AClause");
		
		Element papa=(Element) element.getParentNode();
	
		if (what.equals("clause") && where.equals("after")) System.out.println("clause+after"); 
		
	}
}
