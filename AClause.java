package sourceViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

public abstract class AClause extends Composite {
	protected Element element;
	ClauseText clauseText;
	
	IModifierNotifier modifierNotifier;
	
	public AClause(Composite clauses2, Element element) {
		super(clauses2, SWT.NONE);
		this.element=element;
		element.setUserData("AClause", this, null);
		modifierNotifier=(IModifierNotifier)element.getOwnerDocument().getUserData("IModifierNotifier");

		System.out.println(element.getAttribute("NO"));
		
		
	}
	public Element getElement() {
		return element;
	}
	public boolean isTagged() {
		return element.hasAttribute("tag");
	}
	public void switchTag() {
		modifierNotifier.setDirty(true);
		if (element.hasAttribute("tag")) 
			element.removeAttribute("tag"); 
		else 
			element.setAttribute("tag", "true");
	}
	public boolean isSelected() {
		return element==element.getOwnerDocument().getUserData("SELECTED");
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
	protected abstract int getRealWidth();
	protected abstract int getRealHeight();
	public void redraw() {
		clauseText.redraw();
	}
}
