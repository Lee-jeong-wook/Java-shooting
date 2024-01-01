package shootingGame;

import java.awt.Point;

public class Missile {
	int x, y;
	public Missile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void move() {
		x += 10;
	}
}
