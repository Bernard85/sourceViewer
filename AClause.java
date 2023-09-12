package sourceViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

public abstract class AClause extends Composite {
	protected Element element;
	boolean selected;

	public AClause(Composite clauses2, Element element) {
		super(clauses2, SWT.NONE);
		this.element=element;
	}
	public Element getElement() {
		return element;
	}
	public boolean isTagged() {
		return element.hasAttribute("tag");
	}
	public void switchTag() {
		if (element.hasAttribute("tag")) 
			element.removeAttribute("tag"); 
		else 
			element.setAttribute("tag", "true");
	}
	public boolean isSelected() {
		return selected;
	}
	public void switchSelected(boolean selected) {
		this.selected=!selected;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return Integer.valueOf(element.getAttribute("width"));
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		element.setAttribute("width",String.valueOf(width));
	}
	/**
	 * @return the height
	 */
	public int getHeight() {
		return Integer.valueOf(element.getAttribute("height"));
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		element.setAttribute("height",String.valueOf(height));
	}
}
