package sata.metastock.bovespa;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class TransparentBackground extends JComponent { 
    private JFrame frame; 
    private Image background;

public TransparentBackground(JFrame frame) {
    this.frame = frame;
    updateBackground( );
}

public void updateBackground( ) {
    try {
        Robot rbt = new Robot( );
        Toolkit tk = Toolkit.getDefaultToolkit( );
        Dimension dim = tk.getScreenSize( );
        background = rbt.createScreenCapture(
        new Rectangle(0,0,(int)dim.getWidth( ),
                          (int)dim.getHeight( )));
    } catch (Exception ex) {
        //p(ex.toString( ));
        ex.printStackTrace( );
    }
}
public void paintComponent(Graphics g) {
    Point pos = this.getLocationOnScreen( );
    Point offset = new Point(-pos.x,-pos.y);
    g.drawImage(background,offset.x,offset.y,null);
}
}
