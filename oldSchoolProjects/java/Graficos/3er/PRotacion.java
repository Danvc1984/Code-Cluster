import java.awt.*;	
import javax.swing.*;
import java.util.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class PRotacion extends JFrame implements KeyListener {
	BufferedImage buffer;
	Pixel pixel;
	Rotation RotationV, growthV;
	ArrayList<Rotation> originalCube, drawingCube;

	public static int degrees;
	
	public static void main(String[] args) {
        new PRotacion();
    }
	
	public PRotacion(){
        setTitle("Rotacion 3D");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable( false );
        setLocationRelativeTo( null );

		buffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		pixel = new Pixel(this, buffer);

		RotationV = new Rotation(0, 0, 0);
		growthV = new Rotation(100, 100, 100);

		originalCube = CubeR.getPoints(RotationV, growthV);
		drawingCube = CubeR.getPoints(RotationV, growthV);
		degrees = 0;

		this.setVisible(true);
		addKeyListener(this);
	
    }

    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { }
    public void keyPressed(KeyEvent evt) {

		
		if(evt.getKeyCode() == KeyEvent.VK_RIGHT) {
			degrees -=10;
			drawingCube = Rotation.doRotationY(originalCube, degrees);
		}
		
		if(evt.getKeyCode() == KeyEvent.VK_LEFT) {
			degrees+=10;
			drawingCube = Rotation.doRotationY(originalCube, degrees);
		}
		
		if(evt.getKeyCode() == KeyEvent.VK_UP) {
			degrees+=10;
			drawingCube = Rotation.doRotationX(originalCube, degrees);
		}
		
		if(evt.getKeyCode() == KeyEvent.VK_DOWN) {
			degrees-=10;
			drawingCube = Rotation.doRotationX(originalCube, degrees);
		}
		
		if(evt.getKeyChar() == 'z' ) {
			degrees-=10;
			drawingCube = Rotation.doRotationZ(originalCube, degrees);
		}
		
		if(evt.getKeyChar() == 'x') {
			degrees+=10;
			drawingCube = Rotation.doRotationZ(originalCube, degrees);
		}
		if(evt.getKeyChar() == 'c') {
			Rotation.reset(RotationV);
			drawingCube = CubeR.getPoints(RotationV, growthV);
			degrees = 0;
		}

		repaint();
    }

	
	
	public void paint(Graphics g){
		Pixel.clear();

		CubeR.drawCube(drawingCube);
		g.drawImage(buffer, 0, 0, this);
		repaint();
		
    }
}
