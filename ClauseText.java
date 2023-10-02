package sourceViewer;

import java.util.regex.Pattern;

import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Element;

public class ClauseText extends StyledText {
	Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	AClause parent;
	Boolean userInputEnabled=false;
	public ClauseText(AClause parent) {
		super(parent, SWT.NONE);
		this.parent = parent;
		addPaintListener( e-> {
			if (parent.isSelected()) {
				e.gc.setForeground(red);
				e.gc.drawRectangle(e.x,e.y,e.width-1,e.height-1);
			}
			if (parent.isTagged()) {
				e.gc.setBackground (red);
				e.gc.fillPolygon(new int[] {e.width-7,0,e.width,0,e.width,7});
			}
		});
		addListener(SWT.MouseDoubleClick, e->{
			parent.switchTag();
			redraw();
		}); 

		addMenuDetectListener(new MenuDetectListener () {
			@Override
			public void menuDetected(MenuDetectEvent e) {
				ClauseText c = (ClauseText) e.widget;
				AClause aClause = (AClause) c.getParent();
				Element element = aClause.element;
				element.getOwnerDocument().setUserData("CLICKON", element, null);
			}
		});





		//		addVerifyListener(e -> {
		//			if (userInputEnabled)	{
		//				parent.getElement().setAttribute("userinput",parent.getElement().getAttribute("userinput")+e.text+":");
		//				parent.getElement().setAttribute("userinputstart",parent.getElement().getAttribute("userinputstart")+e.start+":");
		//				parent.getElement().setAttribute("userinputend",parent.getElement().getAttribute("userinputend")+e.end+":");
		//			}
		//		});
		loadText();
		setUserInputEnabled(true);

		EMenuService eMenuService = (EMenuService) parent.element.getOwnerDocument().getUserData("EMenuService");
		eMenuService.registerContextMenu(this, "SourceViewer.popupmenu");






	}
	/**
	 * @param userInput the userInput to set
	 */
	public void setUserInputEnabled(Boolean userInputEnabled) {
		this.userInputEnabled = userInputEnabled;
	}
	public void loadText() {

		/// Chiko Khaloucha
		//           012345678 
		String text="lancier\nLancier\nLancier";
		setText(text);
		///setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY)); 
		StyleRange[] style = new StyleRange[1];
		style[0]= new StyleRange();
		style[0].background=Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		int[] ranges = new int[] { 10, 10};
		setStyleRanges(ranges,style);

		if (!parent.getElement().hasAttribute("userinput")) return;

		String userInput[] =parent.getElement().getAttribute("userinput").split(Pattern.quote(":"),-1);
		String userInputstart[] =parent.getElement().getAttribute("userinputstart").split(Pattern.quote(":"));
		String userInputend[] =parent.getElement().getAttribute("userinputend").split(Pattern.quote(":"));

		for (int n=0;n<userInputstart.length;n++) {


			text=text.substring(0,Integer.parseInt(userInputstart[n]))
					+userInput[n]
							+text.substring(Integer.parseInt(userInputend[n]));

		}

		setText(text);

	}
}
