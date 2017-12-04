import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class keyboard implements KeyListener {
	
	private boolean[] keys = new boolean [120];
	public boolean left,right,space;
	
	public void update() {
		
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		space = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_W];
		
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		
	}

	
	public void keyReleased(KeyEvent e) {
		
		keys[e.getKeyCode()] = false;
	}

	
	public void keyTyped(KeyEvent e) {
		
		
	}

}
