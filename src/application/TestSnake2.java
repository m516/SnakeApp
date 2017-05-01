package application;

public class TestSnake2 extends Snake {
	int direction = 0;
	int timer = 0;

	@Override
	public int move() {
		timer--;
		if(timer <= 0){
			direction = (int)(Math.random()*4);
			timer = 5;
		}
		return direction;
	}
	@Override
	public String name() {
		return super.toString();
	}
}