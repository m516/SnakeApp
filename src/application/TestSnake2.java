package application;
public class TestSnake2 extends Snake {
	int direction = 0;
	int timer = 0;
	LocI target=new LocI(1,1);

	@Override
	public int move() {
		timer--;
		if(timer <= 0){
			timer = 2;
			direction ++;
			direction %= 4;
			Console.addText(target.toString());
		}
		return direction;
	}
	@Override
	public String getName() {
		return "Super Snake";
	}
}