package main;
import java.awt.*;

public class Animation {
	
	private Image[] image;
	private int next; //keeps track of next image 
	
	//variables for handling time each image is shown before going to next
	private int duration;
	private int delay;
	
	public Animation(String name, int count, int duration) {
		image = new Image[count];
		
		//populating image array with our sprite images 
		for(int i = 0; i < count; i++) {
			image[i] = Toolkit.getDefaultToolkit().getImage(name + "_" + i + ".png");
		}
		
		this.duration = duration;
		
		delay = duration;
	}
	
	public Image startImage() {
		return image[0];
	}
	
	public Image nextImage() {
		//will wait before moving/drawing next image
		if(delay == 0) {
			next++;
			if(next == image.length) next = 1;
			delay = duration;
		}
		delay--;
		
		return image[next];
	}
}
