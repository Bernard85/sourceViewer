package sourceViewer;

import java.io.File;
import java.io.FileOutputStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SourceViewerSetUp {
	Document dView=null;
	@Inject MDirtyable dirty;
	@Inject EMenuService eMenuService;
	String xmlID;
	@PostConstruct public void postConstruct(MPart mPart, Composite parent) {
		Element element = (Element) mPart.getTransientData().get("ELEMENT");
		String arg1 = element.getAttribute("arg1");

		System.out.println(arg1);

		int index = arg1.lastIndexOf(".");

		xmlID=arg1.substring(0, index)+".xml";

		try {
			dView = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(xmlID));
			mPart.getTransientData().put("DOCUMENT", dView);
			} catch (Throwable e) {
			System.out.println("xml loading failure");
			return;
		}
		Element eView = dView.getDocumentElement();
		
		dView.setUserData("IModifierNotifier", new IModifierNotifier() {
			@Override
			public void setDirty(boolean status) {
				dirty.setDirty(status);
			}
		}, null);

		dView.setUserData("EMenuService", eMenuService, null);
		
		Clauses clauses=new Clauses(parent,eView);
		
		FormData fd=new FormData();
		fd.left=new FormAttachment(0);		
		fd.top=new FormAttachment(0);
		fd.right=new FormAttachment(100);		
		fd.bottom=new FormAttachment(100);
		clauses.setLayoutData(fd);
		clauses.layout();
		
	}
	@Persist void saveIt() {
		try {
			killCR(dView.getDocumentElement(), 0);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(dView), new StreamResult(new FileOutputStream(xmlID)));
			dirty.setDirty(false);
		} catch (Throwable t) {
			System.out.println("save xml failure");
		}	
	}

	private void killCR(Node parentNode, int no) {
		((Element)parentNode).setAttribute("NO", Integer.toString(no));
		NodeList nl= parentNode.getChildNodes();
		for (int n=0;n<nl.getLength();) {
			Node node = nl.item(n);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				killCR(node, no+n+1);
				n++;}
			else parentNode.removeChild(node);
		}
	}



}
