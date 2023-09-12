package sourceViewer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CurveBraceSnipett {
	private static final String  FILEID="src\\sourceViewer\\CurveBraceSnipett0.xml";
	private static Document dView;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setMaximized(true);
		
		System.out.println("shell.getClientArea().height="+shell.getClientArea().height);
		
		shell.setText("CurveBraceSnipett");
		shell.setLayout(new FormLayout());

		try {
			dView = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(FILEID));
		} catch (Throwable e) {
			System.out.println("xml loading failure");
		}

		display.addFilter(SWT.KeyDown,  e -> {
			if (e.stateMask == SWT.CTRL && e.keyCode == 's') {
				try {
					killCR(dView.getDocumentElement());
					Transformer transformer = TransformerFactory.newInstance().newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");
					transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
					transformer.transform(new DOMSource(dView), new StreamResult(new FileOutputStream(FILEID)));
				} catch (Throwable t) {
					System.out.println("save xml failure");
				}
			}
		});

		Element eView = dView.getDocumentElement();
		Clauses clauses=new Clauses(shell,eView);
		FormData fd=new FormData();
		fd.left=new FormAttachment(0);		
		fd.top=new FormAttachment(0);
		fd.right=new FormAttachment(100);		
		fd.bottom=new FormAttachment(100);
		clauses.setLayoutData(fd);
		
		shell.open();
		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in the event queue
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void killCR(Node parentNode) {
		NodeList nl= parentNode.getChildNodes();
		for (int n=0;n<nl.getLength();) {
			Node node = nl.item(n);
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{killCR(node);
			n++;}
			else parentNode.removeChild(node);
		}
	}

}
