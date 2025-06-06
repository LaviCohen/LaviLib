package lb.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.LinkedList;

public class ListLayout implements LayoutManager2{

	public LinkedList<Component> components = new LinkedList<Component>();
	public int hgap;
	public int vgap;
	
	public ListLayout() {
		this(0, 0);
	}
	public ListLayout(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
	}
	@Override
	public void addLayoutComponent(String name, Component comp) {
		components.add(comp);
	}
	
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints instanceof Integer) {
			components.add((Integer) constraints, comp);
		} else {
			components.add(comp);
		}
	}

	@Override
	public void layoutContainer(Container parent) {
		int totalHeight = 0;
		for (int i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			int componentHeight = c.getPreferredSize().height;
			c.setBounds(0, totalHeight, parent.getWidth(), componentHeight);
			totalHeight += componentHeight + vgap;
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int width = 150;
		int totalHeight = 0;
		for (Component c:components) {
			totalHeight += c.getPreferredSize().height + vgap;
			if (c.getPreferredSize().width > width) {
				width = c.getPreferredSize().width;
			}
		}
		return new Dimension(width, totalHeight);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		if (components.contains(comp)) {
			components.remove(comp);
		}
	}
	
	@Override
	public float getLayoutAlignmentX(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		// TODO Auto-generated method stub
		return null;
	}
}