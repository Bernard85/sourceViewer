import static org.eclipse.jface.widgets.WidgetFactory.button;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class StyledTextFrames {
	static Display display = new Display();
	static Button buttonSelected=null; 
	static StyleRange styleSelected, styleChosen;
	static ArrayList<PLB> plbs = new ArrayList();  
	static StyledText styledText;	

	public static void main(String[] args) {

		Font courierNew = new Font(display, "Courier new", 10, SWT.NORMAL);

		Color white = display.getSystemColor(SWT.COLOR_WHITE);
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color gray = display.getSystemColor(SWT.COLOR_GRAY);

		styleSelected=new StyleRange(new TextStyle(courierNew,null,white));
		styleSelected.borderStyle=SWT.BORDER;
		styleSelected.borderColor=red;

		styleChosen=new StyleRange(new TextStyle(courierNew,null,white));

		Button buttons[] = new Button[3];

		Shell shell = new Shell(display);
		shell.setText("Pour tester ");
		shell.setLayout(new FormLayout());

		Group group = new Group(shell, SWT.SHADOW_ETCHED_IN);
		group.setText("Chose context");
		FormData fd = new FormData();
		fd.left=new FormAttachment(0);
		fd.height=20;
		fd.right=new FormAttachment(100);
		fd.bottom=new FormAttachment(100);
		group.setLayoutData(fd);
		group.setLayout(new RowLayout());

		for (int i=0;i<buttons.length;i++) {
			buttons[i] = button(SWT.RADIO)

					.onSelect(e->{
						buttonSelected=(Button)e.widget;
						styledTextRefresh();
					})
					.text("button="+i)
					.create(group);
		}

		styledText = new StyledText(shell, SWT.V_SCROLL|SWT.BORDER);
		styledText.setBlockSelection(true);
		fd=new FormData();
		fd.left=new FormAttachment(0);
		fd.top=new FormAttachment(0);
		fd.right=new FormAttachment(100);
		fd.bottom=new FormAttachment(group);
		styledText.setLayoutData(fd);
		styledText.setFont(courierNew);
		styledText.setBackground(gray);

		styledText.addListener(SWT.KeyUp, new Listener(){
			@Override
			public void handleEvent(Event e) {
				xxxUp(e.stateMask & SWT.CTRL);
			}
		});

		styledText.addMouseListener(new MouseListener() {
			private boolean doubleClick;
			public void mouseDown(MouseEvent e) {
				doubleClick = false;
			}
			public void mouseDoubleClick(MouseEvent e) {
				doubleClick = true;

				int o =styledText.getOffsetAtPoint(new Point(e.x,e.y));

				for (PLB plb:plbs) {
					if (o>=plb.position&&o<=plb.position+plb.length) {
						buttonSelected.setSelection(false);
						plb.button.setSelection(true);
						buttonSelected=plb.button;
						styledTextRefresh();
						return;
					}
				}

			}
			public void mouseUp(MouseEvent e) {
				display.timerExec(Display.getDefault().getDoubleClickTime()
						,new Runnable() {public void run() {if (!doubleClick) 
						{xxxUp(e.stateMask & SWT.CTRL);
						}}}
						);
			}
		});


		try {
			styledText.setText(new String(new FileInputStream("C:/studies/sources/xexecppc2.sqlrpgle").readAllBytes()));
		} catch (Throwable e) {
			System.out.println("plantage lors de la lecture");
		} 

		System.out.println(styledText.getBlockSelectionBounds());

		shell.layout();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}

	static void xxxUp(int ctrl) {
		// 

		if (ctrl==0) {
			for (int i=0;i<plbs.size();) {
				PLB plb = plbs.get(i);
				if (plb.button==buttonSelected) plbs.remove(i); 
				else i++;
		}}

		int pl[]=styledText.getSelectionRanges();

		int plx=0, plbx=0;

		while (plx<pl.length&&plbx<plbs.size()) {
			int pl1=pl[plx];
			int pl2=pl1+pl[plx+1];

			PLB plb=plbs.get(plbx);
			int plb1=plb.position; 
			int plb2=plb1+plb.length;

			if (plb2<pl1) plbx++;
			else if (pl2<plb1) plx+=2;
			else plbs.remove(plbx);
		}

		// ajout des nouvelles lignes sélectionnées
		for (int i=0;i<pl.length;i=i+2) {
			plbs.add(new PLB(pl[i],pl[i+1],buttonSelected));
		}

		Rectangle rect = styledText.getBlockSelectionBounds();
		rect.height=1;rect.width=1;
		styledText.setBlockSelectionBounds(rect);

		styledTextRefresh();		
	}

	static void styledTextRefresh() {


		// tri des  
		Collections.sort(plbs);
		int[] pls         = new int[plbs.size()*2];
		StyleRange[] srs  = new StyleRange[plbs.size()];
		for (int i=0;i<plbs.size();i++) {
			PLB plb=plbs.get(i);

			pls[i*2]=plb.position;
			pls[i*2+1]=plb.length;

			srs[i]=(plb.button==buttonSelected)?styleSelected:styleChosen;
		}

		styledText.setStyleRanges(pls, srs);
		styledText.redraw();

	}


}

class PLB implements Comparable<PLB> {
	int position, length;
	Button button;
	public PLB(int position, int length, Button button) {
		this.position = position;
		this.length = length;
		this.button =  button;
	}
	@Override
	public int compareTo(PLB other) {
		return this.position-other.position;
	}

}