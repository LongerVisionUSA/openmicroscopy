/*
 * org.openmicroscopy.shoola.agents.viewer.canvas.ImageCanvasMng
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

package org.openmicroscopy.shoola.agents.viewer.canvas;


//Java imports
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.viewer.ViewerCtrl;
import org.openmicroscopy.shoola.agents.viewer.ViewerUIF;

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 *              <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 *              <a href="mailto:a.falconi@dundee.ac.uk">
 *                  a.falconi@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class ImageCanvasMng
    implements MouseListener, MouseMotionListener
{

    private ImageCanvas         view;
    
    private Rectangle           drawingArea;
    
    /** Control to handle dragged event. */
    private boolean             dragging, onOff, pin, painting, click;
    
    /** Width of the lens. */
    private int                 width;
    
    /** Magnification factor for the lens image. */
    private double              magFactorLens;
    
    /** Anchor point. */
    private Point               anchor;
    
    /** Color of the lens' border. */
    private Color               c;
    
    private ViewerCtrl          control;
    
    ImageCanvasMng(ImageCanvas view, ViewerCtrl control)
    {
        this.view = view;
        this.control = control;
        width = ViewerUIF.DEFAULT_WIDTH;
        magFactorLens = ViewerUIF.DEFAULT_MAG;
        drawingArea = new Rectangle();
        onOff = false;
        pin = false;
        painting = false;
        click = false;
        attachListeners();
    }
    
    /** Attach mouse listener. */
    private void attachListeners()
    {
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    int getWidth() { return width; }
    
    Color getColor() { return c; }
    
    boolean getPainting() { return painting; }
    
    double getMagFactorLens() { return magFactorLens; }
    
    public void setMagFactorLens(double f)
    {
        magFactorLens = f;
        view.resetLens();
        if (anchor != null) drawLens(anchor);
        //if (view.getLens() != null) drawLens(anchor);
    }
    
    public void setClick(boolean b) { click = b; }
    
    /** Call when the image inspector widget is closed. */
    public void setDefault(boolean b)
    { 
        click = b;
        onOff = !b;
        pin = b;
        painting = b;
        view.resetLens();
    }
    
    /** Set the width of the lens. */
    public void setWidth(int w)
    {
        width = w;
        view.resetDrawingArea();
        if (view.getLens() != null) {
            view.resetLens();
            view.repaint();
        }
    }
    
    public void setOnOff(boolean b)
    { 
        click = true;
        onOff = b;
    }
    
    public void setPin(boolean b)
    { 
        pin = b;
        if (pin && anchor != null)
            drawLens(anchor);
        else if (!pin)
            view.resetLens();
            //view.repaint();
    }
    
    public void setPainting(boolean b, Color c)
    {
        painting = b;
        this.c = c;
        view.resetLens();
        if (onOff && pin && anchor != null) drawLens(anchor);
    }
    
    void setDrawingArea(int x, int y, int w, int h)
    { 
        drawingArea.setBounds(x+width/2, y+width/2, w-width, h-width);
    }
    
    /** Handle Mouse pressed event. */
    public void mousePressed(MouseEvent e)
    {
        if (e.getClickCount() == 1 && click) {
            Point p = new Point(e.getPoint());
            //view.resetLens();
            if (!dragging && onOff && drawingArea.contains(p)) {
                dragging = true;
                drawLens(p);
            }
        } else if (e.getClickCount() == 2 && !click) control.showInspector();
    }

    /** Handle Mouse dragged event. */
    public void mouseDragged(MouseEvent e)
    {
        Point p = new Point(e.getPoint());
        if (dragging && onOff && drawingArea.contains(p)) 
            drawLens(p); 
    }
    
    /** Handle Mouse released event. */
    public void mouseReleased(MouseEvent e)
    { 
        dragging = false;
        if (onOff && !pin) {
            view.resetLens();
            //view.repaint();  
        }
    }
    
    /** Forward event to the view. */
    private void drawLens(Point p)
    {
        anchor = p;
        view.paintLensImage(magFactorLens, p, width, painting, c);
    }

    /** 
     * Required by I/F but not actually needed in our case, 
     * no op implementation.
     */   
    public void mouseClicked(MouseEvent e) {}

    /** 
     * Required by I/F but not actually needed in our case, 
     * no op implementation.
     */   
    public void mouseEntered(MouseEvent e) {}

    /** 
     * Required by I/F but not actually needed in our case,
     * no op implementation.
     */   
    public void mouseExited(MouseEvent e) {}

    /** 
     * Required by I/F but not actually needed in our case,
     * no op implementation.
     */   
    public void mouseMoved(MouseEvent e) {}
    
}
