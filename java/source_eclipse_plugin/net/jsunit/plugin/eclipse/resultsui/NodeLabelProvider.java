package net.jsunit.plugin.eclipse.resultsui;

import net.jsunit.plugin.eclipse.JsUnitPlugin;
import net.jsunit.plugin.eclipse.resultsui.node.Node;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class NodeLabelProvider extends LabelProvider implements ITableLabelProvider {
	
	private boolean fullyQualified;
	
	public NodeLabelProvider() {
		fullyQualified = false;
	}

	public String getColumnText(Object obj, int index) {
		return getText(obj);
	}
	
	public String getText(Object obj) {
		Node node = (Node) obj;
		return node.getDisplayString(fullyQualified);		
	}
	
	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}
	
	public Image getImage(Object obj) {
		Node node = (Node) obj;
		return JsUnitPlugin.soleInstance().createImage(node.getImageName());
	}

	public boolean isFullyQualified() {
		return fullyQualified;
	}

	public void setFullyQualified(boolean b) {
		fullyQualified = b;
	}
	
}