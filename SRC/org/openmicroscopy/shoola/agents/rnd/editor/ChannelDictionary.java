/*
 * org.openmicroscopy.shoola.agents.rnd.editor.ChannelDictionary
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.rnd.editor;


//Java imports
import java.awt.Dimension;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

//Third-party libraries

//Application-internal dependencies

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class ChannelDictionary
	extends JDialog
{
	
	private static final Dimension	DIM = new Dimension(300, 300);
	private static final int		WIN_W = 400, WIN_H = 400;
	
	private List elements;
	public ChannelDictionary(List elements)
	{
		this.elements = elements;
		buildGUI();
	}

	/** Build and lay out the GUI. */
	private	void buildGUI()
	{
		getContentPane().add(buildPanel());
		setSize(WIN_W, WIN_H);
	}
	
	private JPanel buildPanel()
	{
		JPanel p = new JPanel();
		//datasets table
		ChannelTableModel datasetsTM = new ChannelTableModel();
		JTable t = new JTable(datasetsTM);
	  	t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	  	t.setPreferredScrollableViewportSize(DIM);
	  	//wrap table in a scroll pane and add it to the panel
	  	JScrollPane sp = new JScrollPane(t);
	  	p.add(sp);
	  	return p;
	}
	/** 
	 * A <code>3</code>-column table model to view the summary of 
	 * datasets contained in the project.
	 * The first column contains the datasets ID and the 
	 * second column the names. Cells are not editable. 
	 */
	private class ChannelTableModel
		extends AbstractTableModel
	{
		private final String[]	
		columnNames = {"Category", "Fluor", "excitation", "emission", "??",
						 "comment" };
		private final Object[]	channels = elements.toArray();
		private Object[][] 		data = new Object[channels.length][6];
	
	
		private ChannelTableModel() {}
	
		public int getColumnCount() { return 6; }
	
		public int getRowCount() { return channels.length; }
	
		public String getColumnName(int col){ return columnNames[col]; }
		
		public Class getColumnClass(int c)
		{
			return getValueAt(0, c).getClass();
		}

		public Object getValueAt(int row, int col) { return data[row][col]; }

		public boolean isCellEditable(int row, int col) { return false; }
		
		public void setValueAt(Object value, int row, int col)
		{
		}
	}
	
}
