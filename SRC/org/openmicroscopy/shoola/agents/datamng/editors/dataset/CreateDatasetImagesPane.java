/*
 * org.openmicroscopy.shoola.agents.datamng.editors.dataset.CreateDatasetImagesPane
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

package org.openmicroscopy.shoola.agents.datamng.editors.dataset;

//Java imports
import java.awt.Cursor;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.datamng.DataManagerUIF;
import org.openmicroscopy.shoola.agents.datamng.IconManager;
import org.openmicroscopy.shoola.env.data.model.ImageSummary;
import org.openmicroscopy.shoola.util.ui.table.TableHeaderTextAndIcon;
import org.openmicroscopy.shoola.util.ui.table.TableIconRenderer;
import org.openmicroscopy.shoola.util.ui.table.TableSorter;
import org.openmicroscopy.shoola.util.ui.UIUtilities;

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
class CreateDatasetImagesPane
	extends JPanel
{
	
	/** Action id. */
	private static final int               NAME = 0, SELECT = 1;
	
	protected static final String[]        columnNames;

	static {
		columnNames  = new String[2];
		columnNames[NAME] = "Name";
		columnNames[SELECT] = "Select";
	}
		
	/** Reference to the manager. */
	private CreateDatasetEditorManager     manager;

	JButton                                selectButton, resetButton;
	
	private ImagesTableModel               imagesTM;
	
	private TableSorter                    sorter;
	
	CreateDatasetImagesPane(CreateDatasetEditorManager manager)
	{
		this.manager = manager;
		buildGUI();
	}

	/** Select or not all images. */
	void setSelection(Object val)
	{
		int countCol = imagesTM.getColumnCount()-1;
		for (int i = 0; i < imagesTM.getRowCount(); i++)
				imagesTM.setValueAt(val, i, countCol); 
	}
	
	/** Build and lay out the GUI. */
	private void buildGUI()
	{
		setLayout(new GridLayout(1, 1));
		add(buildDatasetsPanel());
		Border b = BorderFactory.createEmptyBorder(0, 0, 10, 10);
		setBorder(b);
	}
	
	private JPanel buildDatasetsPanel()
	{
		//select button
		selectButton = new JButton("Select All");
		selectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		selectButton.setToolTipText(
			UIUtilities.formatToolTipText("Select all images."));
		//cancel button
		resetButton = new JButton("Reset");
		resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		resetButton.setToolTipText(
			UIUtilities.formatToolTipText("Cancel selection."));
		JPanel controls = new JPanel(), p = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
		controls.add(resetButton);
		controls.add(Box.createRigidArea(DataManagerUIF.HBOX));
		controls.add(selectButton);
		controls.setOpaque(false); //make panel transparent
	  	
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	  
		//images table
		imagesTM = new ImagesTableModel();
		JTable t = new JTable();
		sorter = new TableSorter(imagesTM);  
		t.setModel(sorter);
		sorter.addMouseListenerToHeaderInTable(t);
		setTableLayout(t);
		
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.setPreferredScrollableViewportSize(DataManagerUIF.VP_DIM);
		//wrap table in a scroll pane and add it to the panel
		JScrollPane sp = new JScrollPane(t);
		p.add(sp);
		p.add(Box.createRigidArea(DataManagerUIF.VBOX));
		p.add(controls);
		
		return p;
	}

	/** Set icons in the tableHeader. */
	private void setTableLayout(JTable table)
	{
		IconManager im = IconManager.getInstance(
							manager.getView().getRegistry());
		TableIconRenderer iconHeaderRenderer = new TableIconRenderer();
		TableColumnModel tcm = table.getTableHeader().getColumnModel();
		TableColumn tc = tcm.getColumn(NAME);
		tc.setHeaderRenderer(iconHeaderRenderer);
		TableHeaderTextAndIcon 
		txt = new TableHeaderTextAndIcon(columnNames[NAME], 
				im.getIcon(IconManager.ORDER_BY_NAME_UP),
				im.getIcon(IconManager.ORDER_BY_NAME_DOWN), 
				"Order images by name.");
		tc.setHeaderValue(txt);
		tc = tcm.getColumn(SELECT);
		tc.setHeaderRenderer(iconHeaderRenderer); 
		txt = new TableHeaderTextAndIcon(columnNames[SELECT], 
				im.getIcon(IconManager.ORDER_BY_SELECTED_UP), 
				im.getIcon(IconManager.ORDER_BY_SELECTED_DOWN),
				"Order by selected images.");
		tc.setHeaderValue(txt);
	}

	/** 
	 * A <code>3</code>-column table model to view the summary of 
	 * image to add to a new dataset.
	 * The first column contains the images ID and the 
	 * second column the names, the third one a check box.
	 * The first two cells are not editable, the third one is. 
	 */
	private class ImagesTableModel
		extends AbstractTableModel
	{
		private final Object[]	images = manager.getImages().toArray();
		private Object[][]		data = new Object[images.length][2];

		private ImagesTableModel()
		{
			for (int i = 0; i < images.length; i++) {
				data[i][0] = (ImageSummary) images[i];
				data[i][1] = new Boolean(false);
			}
		}

		public int getColumnCount() { return 2; }

		public int getRowCount() { return images.length; }

		public String getColumnName(int col) { return columnNames[col]; }
	
		public Class getColumnClass(int c)
		{
			return getValueAt(0, c).getClass();
		}

		public Object getValueAt(int row, int col) { return data[row][col]; }
	
		public boolean isCellEditable(int row, int col) {  return (col == 1); }
		
		public void setValueAt(Object value, int row, int col)
		{
			data[row][col] = value;
			ImageSummary is = (ImageSummary) sorter.getValueAt(row, NAME);
			fireTableCellUpdated(row, col);
			manager.addImage(((Boolean) value).booleanValue(), is);
		}
	}
	
}
