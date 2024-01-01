package shootingGame;

public class Enemy {
	int x;
	int y;
	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void move() {
		x -= 3;
	}
}
