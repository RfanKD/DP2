import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
//TODO: Represent image as a grayscale matrix
//Convolve this matrix with a filter
//Output the resultant matrix to a new image

public class Logic {
	
public static void main (String[] args){
	BufferedImage color_image = null;
	try{
	File file = new File("image.jpg");
	color_image = ImageIO.read(file);
	InputStream is = new BufferedInputStream(new FileInputStream("image.jpg")); 
	color_image = ImageIO.read(is); 
	}
	catch(IOException e){};
	
	int height = color_image.getHeight();
	int width = color_image.getWidth();
	//Convert image to grayscale
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
	Graphics2D graphics = image.createGraphics();
	graphics.drawImage(color_image, 0, 0, null);  
	graphics.dispose();
	try {
		ImageIO.write(image, "jpg", new File("grayscale.jpg"));
	} catch (IOException e) {
		e.printStackTrace();
	}
	int imageMatrix[][] = getGrayscaleMatrix(image);
	BufferedImage out = drawImage(imageMatrix);
	//Perform image processing
	BufferedImage bf_image = null;
	try {
		bf_image = bilateralGS(image);
	} catch (IOException e2) {
		e2.printStackTrace();
	}
	
	BufferedImage ed_image = null;
	try {
		ed_image = cannyED(bf_image);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	try {
		ImageIO.write(ed_image, "jpg", new File("edge_detected.jpg"));
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	
	
}
//http://people.csail.mit.edu/sparis/bf_course/ Reference this
//http://www.cs.duke.edu/~tomasi/papers/tomasi/tomasiIccv98.pdf - use these for final imp

//Temporary implementation - http://www.cse.iitd.ernet.in/~pkalra/csl783/canny.pdf
public static BufferedImage bilateralGS(BufferedImage image) throws IOException{
	BufferedImage output = null;
	float[] matrix = {
			2.0f, 4.0f, 5.0f, 4.0f, 2.0f,
			4.0f, 9.0f, 12.0f, 9.0f, 4.0f,
			5.0f, 12.0f, 15.0f, 12.0f, 5.0f,
			4.0f, 9.0f, 12.0f, 9.0f, 4.0f,
			2.0f, 4.0f, 5.0f, 4.0f, 2.0f
	};
	
	for(int i = 0; i < 25; i++){
		matrix[i] = matrix[i]/ 159.0f;
	}
	
	  BufferedImageOp op = new ConvolveOp( new Kernel(3, 3, matrix) );
		output = op.filter(image, output);
		try {
			ImageIO.write(output, "jpg", new File("gaussian.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	return image;
}

public static BufferedImage cannyED(BufferedImage image) throws IOException{
	BufferedImage output = null;
	float[] matrix = {
			-1.0f, 0.0f, 1.0f,
			-2.0f, 0.0f, 2.0f,
			-1.0f, 0.0f, 2.0f,
	};
	
	
	  BufferedImageOp op = new ConvolveOp( new Kernel(3, 3, matrix) );
		output = op.filter(image, output);
		
		try {
			ImageIO.write(output, "jpg", new File("edintermed.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		float[] matrix2 = {
				1.0f, 2.0f, 1.0f,
				0.0f, 0.0f, 0.0f,
				-1.0f, -2.0f, -1.0f,
		};
		
		
		  BufferedImageOp op2 = new ConvolveOp( new Kernel(3, 3, matrix2) );
			output = op2.filter(image, output);
		
		try {
			ImageIO.write(output, "jpg", new File("ednew.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	return output;
}

//Returns the image as a matrix of values from 0-255
public static int[][] getGrayscaleMatrix(BufferedImage image){
	int height = image.getHeight();
	int width = image.getWidth();
	Color color;
	int [][] matrix = new int [width][height];
	
	for (int i = 0; i < width; i++){
		for (int j = 0; j < height; j++){
			color = new Color(image.getRGB(i, j));
			int avg = (color.getRed() + color.getGreen()+color.getBlue())/3;
			matrix[i][j] = avg;
		}
	}
	
	return matrix;
}

//Given a matrix of values from 0-255, returns a BufferedImage representation
public static BufferedImage drawImage(int[][] matrix){
	int width = matrix.length;
	int height = matrix[0].length;
	BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
	for (int i = 0; i<width; i++){
		for (int j = 0; j<height; j++){
			int col = matrix[i][j];
			int pixel = new Color(col,col,col).getRGB();
			output.setRGB(i, j, pixel);
		}		
	}
	return output;	
}

//Given a kernel and an image matrix, perform convolution
public static int[][] convolution(float[][]kernel, int[][]image){
	int width = image.length;
	int height = image[0].length;
	int[][] output = new int[width][height];
	
	
	
	return output;
}

}
