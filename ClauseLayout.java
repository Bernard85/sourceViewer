package sourceViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;

public class ClauseLayout extends Layout {
    public static final int MARGIN = 5;
    int height;
	Point [] sizes;

	@Override
	protected Point computeSize(Composite clausesContent, int wHint, int hHint, boolean flushCache) {
		Clauses clausesParent =  (Clauses) clausesContent.getParent();
		int y = MARGIN;
		
		Control children[] = clausesContent.getChildren();
		initialize(children);
		return new Point(85,height);
	}

	@Override
	protected void layout(Composite clausesContent, boolean flushCache) {
		
		Clauses clausesParent =  (Clauses) clausesContent.getParent();
		int y = -clausesParent.getPosition();
		
		Control children[] = clausesContent.getChildren();
		if (flushCache || sizes == null || sizes.length != children.length) {
			initialize(children);
		}
		
	    int x = MARGIN;
	    y +=MARGIN;

	    for (int i = 0; i < children.length; i++) {
	       int height = sizes[i].y;
	       children[i].setBounds(x, y, sizes[i].x, height);
	       y += height + MARGIN;
	    }
	}

	private void initialize(Control[] children) {
		sizes = new Point [children.length];
		height=0;
		for (int i = 0; i < children.length; i++) {
			sizes[i] = children[i].computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			height +=sizes[i].y+MARGIN;
		}
	}

}
