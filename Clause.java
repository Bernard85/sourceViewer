package sourceViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Sash;
import org.w3c.dom.Element;

public class Clause extends AClause {
	private static final int HMARGIN = 16;
	ClauseText clauseText;
	Sash h1,v1;
	Composite parent;
	Color h1Color=new Color(getDisplay(),10,220,209);
	public Clause(Composite parent, Element element) {
		super(parent, element);
		this.parent=parent; 
		this.element=element;

		setLayout(new FormLayout());
		//
		h1 = new Sash(this,SWT.HORIZONTAL);
		h1.setBackground(h1Color);
		FormData fd = new FormData();
		fd.left= new FormAttachment(0);
		fd.right=new FormAttachment(100,-4);
		fd.bottom=new FormAttachment(100,-4);
		h1.setLayoutData(fd);
		//
		h1.addListener(SWT.Selection, e -> {
			int delta =e.y-h1.getLocation().y;
			setHeight(delta+getHeight());
			parent.layout();
		});
		
		//
		v1 = new Sash(this, SWT.VERTICAL);
		v1.setBackground(h1Color);
		fd = new FormData();
		fd.top=new FormAttachment(0);
		fd.right=new FormAttachment(100,-4);
		fd.bottom=new FormAttachment(100, -4);
		v1.setLayoutData(fd);
		v1.addListener(SWT.Selection, e -> {
			int delta =e.x-v1.getLocation().x;
			v1.setLocation(v1.getLocation().x+delta, SWT.DEFAULT);
			setWidth(getWidth()+delta);
			parent.layout();
		});
		
		clauseText = new ClauseText(this);
		fd = new FormData();
		fd.top=new FormAttachment(0);
		fd.left=new FormAttachment(0);
		fd.right=new FormAttachment(v1);
		fd.bottom=new FormAttachment(h1,-2,SWT.TOP);
		clauseText.setLayoutData(fd);
		
	}

	
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point(getWidth(),getHeight());
	}
}
