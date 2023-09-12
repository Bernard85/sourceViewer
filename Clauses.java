package sourceViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Slider;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Clauses extends Composite {
	Composite parent;
	Slider slider;
	Composite clauses2;
	public Clauses(Composite parent,Element element) {
		super(parent, SWT.NONE);
		this.parent=parent;
		setLayout(new FormLayout());

		slider= new Slider(this, SWT.VERTICAL);
		FormData fd = new FormData();
		fd.top=new FormAttachment(0);
		fd.left=new FormAttachment(0);
		fd.bottom=new FormAttachment(100);
		fd.width=20;
		slider.setLayoutData(fd);

		clauses2 = new Composite(this,SWT.NONE);
		fd = new FormData();
		fd.left=new FormAttachment(slider);
		fd.top=new FormAttachment(0);
		fd.right=new FormAttachment(100);
		fd.bottom=new FormAttachment(100);
		clauses2.setLayoutData(fd);
		clauses2.setLayout(new ClauseLayout());
		// 
		NodeList nl = element.getChildNodes();
		for (int n=0;n<nl.getLength();n++) {
			Node wN = nl.item(n);
			if (wN.getNodeType() == Node.ELEMENT_NODE) {
				if (wN.getNodeName()=="bracket") new ClauseConditional(clauses2, (Element) wN);
				else if (wN.getNodeName()=="clause") new Clause(clauses2, (Element) wN);
			}
		}
		//
		slider.setMinimum(0);
		slider.setIncrement(1);
		slider.setThumb(10);


		layout();
		
		addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				int preferedHeight=clauses2.computeSize(SWT.DEFAULT,SWT.DEFAULT).y;
				int maximum = (preferedHeight<getHeight()-10)?0:preferedHeight-getHeight()-10;
				slider.setMaximum(maximum);	
			}
		});
		
		int preferedHeight=clauses2.computeSize(SWT.DEFAULT,SWT.DEFAULT).y;
		int maximum = (preferedHeight<getHeight())?0:preferedHeight-getHeight();
		
		System.out.println("preferedHeight/getHeight"+preferedHeight+"/"+getHeight());
		
		slider.setMaximum(getHeight()-10);	

		slider.addListener(SWT.Selection, e-> {
			clauses2.redraw();
			clauses2.layout();
		});
		
		layout();

	}
	public int getPosition() {
		return slider.getSelection();
	}

	public int getHeight() {
		return (parent instanceof ClauseConditional)?((ClauseConditional)parent).getHeight():parent.getClientArea().height;
	}
	
}
