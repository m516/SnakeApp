public class TestSnake extends SnakeRoot {
	int direction = 0;
	int timer = 0;
	LocI target=new LocI(1,1);

	@Override
	public int move() {
		timer--;
		if(timer <= 0){
			target = findFruit();
			timer = target.distanceEstimate(getHead());
		}
		//Are conditions favorable?
		boolean isRight 	= target.getX()>getHead().getX() && canGoRight();
		boolean isLeft 		= target.getX()<getHead().getX() && canGoLeft();
		boolean isUp 		= target.getY()<getHead().getY() && canGoUp();
		boolean isDown 		= target.getY()>getHead().getY() && canGoDown();
		if(isRight) return RIGHT;
		if(isLeft) return LEFT;
		if(isUp) return UP;
		if(isDown) return DOWN;
		//Okay, the conditions are not favorable, so make the best of it
		for(int i = 0; i < 4; i ++){
			if(isGood(getNextHeadPos(i))) {
				return i;
			}
		}
		return LEFT;
	}
	/**
	 * Finds a fruit
	 * @return the fruit closest to the snake
	 */
	private LocI findFruit(){
		LocI h = getHead();
		int s = Math.max(Arena.getXSize(), Arena.getYSize());
		for(int i = 1; i < s; i ++){
			for(int j = 0; j < i; j ++){
				//Check horizontal
				int y = h.getY()+i;
				int x = h.getX()+j;
				if(isFruit(x,y))return new LocI(x,y);
				x = h.getX()-j;
				if(isFruit(x,y))return new LocI(x,y);
				y = h.getY()-i;
				x = h.getX()+j;
				if(isFruit(x,y))return new LocI(x,y);
				x = h.getX()-j;
				if(isFruit(x,y))return new LocI(x,y);
				//Check vertical
				y = h.getY()+j;
				x = h.getX()+i;
				if(isFruit(x,y))return new LocI(x,y);
				x = h.getX()-i;
				if(isFruit(x,y))return new LocI(x,y);
				y = h.getY()-j;
				x = h.getX()+i;
				if(isFruit(x,y))return new LocI(x,y);
				x = h.getX()-i;
				if(isFruit(x,y))return new LocI(x,y);
			}
		}
		return new LocI(1,1);
	}
}